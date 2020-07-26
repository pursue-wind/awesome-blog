package cn.mirrorming.blog.aop.cache;

import cn.mirrorming.blog.utils.RedisOperator;
import cn.mirrorming.blog.domain.common.Tuple;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Mireal
 */
@Component
@Aspect
public class CacheAspect {
    @Autowired
    private RedisOperator redisOperator;
    private static final String SPLIT_EXPRESS = "=>";
    private static final String SPLITOR = ",";
    private SpelExpressionParser parse = new SpelExpressionParser();
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    /**
     * 通过前缀的删除
     */
    private CacheHandler deleteByPrefixHandler = params -> {
        params.forEach(redisOperator::deleteByPrex);
    };
    /**
     * 通过Key的删除
     */
    private CacheHandler deleteByKeyHandler = params -> {
        params.forEach(redisOperator::delete);
    };
    private Map<String, CacheHandler> handlerMap = new ConcurrentHashMap<>() {
        {
            put("delete", deleteByKeyHandler);
            put("delete_by_prefix", deleteByPrefixHandler);
        }
    };
    private SecurityExpressionParser mySecurityExpressionParser = (point, value) -> {
        Assert.isTrue(value.contains(SPLIT_EXPRESS),
                () -> String.format("表达式语法错误: %s, 例：delete_by_prefix => 'prefix' + #id, #param + 'suffix'", value));
        Assert.isTrue(value.split(SPLIT_EXPRESS).length == 2,
                () -> String.format("表达式语法错误: %s, 例：delete => 'prefix' + #id, #param + 'suffix'", value));
        String m = value.split(SPLIT_EXPRESS)[0].trim();
        Assert.isTrue(handlerMap.containsKey(m), () -> String.format("@Cache注解参数错误，处理器未找到: %s, 例：delete => 'prefix' + #id, #param + 'suffix'", value));
        String params = value.split(SPLIT_EXPRESS)[1].trim();
        return Tuple.of(m, Arrays.stream(params.split(SPLITOR)).map(s -> parse(s, point)).collect(Collectors.toList()));
    };

    @Around("@annotation(cn.mirrorming.blog.aop.cache.Cache)")
    public Object around(ProceedingJoinPoint p) throws Throwable {

        MethodSignature sign = (MethodSignature) p.getSignature();
        Method method = sign.getMethod();

        Cache cache = method.getAnnotation(Cache.class);

        String value = cache.value();

        Tuple<String, List<String>> tuple = mySecurityExpressionParser.parse(p, value);
        handlerMap.get(tuple.getFirst()).handler(tuple.getSecond());
        return p.proceed();
    }

    /**
     * Spel 解析器
     *
     * @param spElString
     * @param p
     * @return
     */
    private String parse(String spElString, ProceedingJoinPoint p) {
        MethodSignature methodSignature = (MethodSignature) p.getSignature();
        //获得方法参数名字列表
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        Expression expression = parse.parseExpression(spElString);
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = p.getArgs();
        IntStream.range(0, args.length).forEach(i -> {
            context.setVariable(Objects.requireNonNull(paramNames)[i], args[i]);
        });
        return Objects.requireNonNull(expression.getValue(context)).toString();
    }

    @FunctionalInterface
    interface SecurityExpressionParser {
        /**
         * 缓存注解 Value 解析器
         */
        Tuple<String, List<String>> parse(ProceedingJoinPoint p, String value);
    }

    @FunctionalInterface
    interface CacheHandler {
        /**
         * 缓存处理器
         *
         * @param keys 键列表
         */
        void handler(List<String> keys);
    }
}
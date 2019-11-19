package cn.mirrorming.blog.exception;

import cn.mirrorming.blog.domain.dto.base.ResultData;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author mireal
 * @date 2018/5/11 上午11:56
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResultData bindExceptionHandler(BindException bindException) {
        ResultData res = new ResultData();
        StringBuffer sb = new StringBuffer();
        List<FieldError> errors = bindException.getFieldErrors();
        doAppend(res, sb, errors);
        return res;
    }

    private void doAppend(ResultData res, StringBuffer sb, List<FieldError> errors) {
        for (FieldError error : errors) {
            sb.append(error.getField());
            sb.append("字段");
            String msg = error.getDefaultMessage();
            sb.append("[").append(StringUtils.isNotBlank(msg) ? msg : error.getDefaultMessage()).append("]");
        }
        res.setMessage(sb.toString());
        res.setStatusCode(400);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultData MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ResultData res = new ResultData();
        StringBuffer sb = new StringBuffer();
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        doAppend(res, sb, errors);
        return res;
    }

    @ExceptionHandler(BusinessException.class)
    public ResultData businessExceptionHandler(BusinessException e) {
        return ResultData.fail(e.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ResultData userExceptionHandler(UserException e) {
        return ResultData.fail(e.getMessage());
    }

    @ExceptionHandler(ArticleException.class)
    public ResultData articleExceptionHandler(ArticleException e) {
        return ResultData.fail(e.getMessage());
    }
}

package cn.mirrorming.blog.exception;


import cn.mirrorming.blog.domain.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * @author Mireal Chan
 * @date 2018/5/11 上午11:56
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BindException.class)
    public R bindExceptionHandler(BindException bindException) {
        R res = new R();
        StringBuffer sb = new StringBuffer();
        List<FieldError> errors = bindException.getFieldErrors();
        doAppend(res, sb, errors);
        return res;
    }

    private void doAppend(R res, StringBuffer sb, List<FieldError> errors) {
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
    public R MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        R res = new R();
        StringBuffer sb = new StringBuffer();
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        doAppend(res, sb, errors);
        return res;
    }

    @ExceptionHandler(BusinessException.class)
    public R businessExceptionHandler(BusinessException e) {
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public R userExceptionHandler(UserException e) {
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(ArticleException.class)
    public R articleExceptionHandler(ArticleException e) {
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(AppException.class)
    public R appExceptionHandler(AppException e) {
        return R.build(e.getMessage(), e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public R ExceptionHandler(AppException e) {
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R ExceptionHandler(SQLIntegrityConstraintViolationException e) {
        return R.fail(e.getMessage());
    }
}

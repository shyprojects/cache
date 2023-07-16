package com.shy.cache.core.exception;

/***
 * 缓存运行时异常类
 * @author shy
 * @date 2023-07-16 1:38
 */
public class CacheRuntimeException extends RuntimeException {
    public CacheRuntimeException(String message) {
        super(message);
    }

    public CacheRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheRuntimeException(Throwable cause) {
        super(cause);
    }

    public CacheRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

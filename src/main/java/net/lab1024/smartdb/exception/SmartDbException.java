package net.lab1024.smartdb.exception;

/**
 * smartdb自身的异常
 * @author zhuoluodada@qq.com
 */
public class SmartDbException extends  RuntimeException{

    public SmartDbException() {
    }

    public SmartDbException(String message) {
        super(message);
    }

    public SmartDbException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartDbException(Throwable cause) {
        super(cause);
    }

    public SmartDbException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

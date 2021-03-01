package me.ed333.easyBot;

public class CodeErrException extends Exception{
    public CodeErrException(String msg) { super(msg); }

    public CodeErrException(Throwable cause) {
        super(cause);
    }

    public CodeErrException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

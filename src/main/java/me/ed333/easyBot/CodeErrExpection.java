package me.ed333.easyBot;

public class CodeErrExpection extends Exception{
    public CodeErrExpection(String msg) { super(msg); }

    public CodeErrExpection(Throwable cause) {
        super(cause);
    }

    public CodeErrExpection(String msg, Throwable cause) {
        super(msg, cause);
    }
}

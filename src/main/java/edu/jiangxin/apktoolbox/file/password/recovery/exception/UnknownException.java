package edu.jiangxin.apktoolbox.file.password.recovery.exception;

import java.io.Serial;

public class UnknownException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnknownException() {
    }

    public UnknownException(Throwable cause) {
        super(cause);
    }
}

package edu.jiangxin.apktoolbox.file.password.recovery.exception;

import java.io.Serial;

public class UnsupportedVersionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnsupportedVersionException() {
    }

    public UnsupportedVersionException(Throwable cause) {
        super(cause);
    }
}

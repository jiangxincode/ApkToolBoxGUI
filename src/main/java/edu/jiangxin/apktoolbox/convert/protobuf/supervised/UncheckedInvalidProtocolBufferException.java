package edu.jiangxin.apktoolbox.convert.protobuf.supervised;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Objects;

final class UncheckedInvalidProtocolBufferException extends RuntimeException {

    private static final long serialVersionUID = 4733167503348537421L;

    UncheckedInvalidProtocolBufferException(final String message, final InvalidProtocolBufferException cause) {
        super(message, Objects.requireNonNull(cause));
    }

    UncheckedInvalidProtocolBufferException(final InvalidProtocolBufferException cause) {
        super(Objects.requireNonNull(cause));
    }

    @Override
    public synchronized InvalidProtocolBufferException getCause() {
        return (InvalidProtocolBufferException) super.getCause();
    }
}

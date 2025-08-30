package edu.jiangxin.apktoolbox.convert.protobuf.supervised;

import com.google.protobuf.Descriptors;

import java.util.Objects;

final class UncheckedDescriptorValidationException extends RuntimeException {

    private static final long serialVersionUID = -7092503313047011977L;

    UncheckedDescriptorValidationException(final String message,
                                           final Descriptors.DescriptorValidationException cause) {
        super(message, Objects.requireNonNull(cause));
    }

    UncheckedDescriptorValidationException(final Descriptors.DescriptorValidationException cause) {
        super(Objects.requireNonNull(cause));
    }

    @Override
    public synchronized Descriptors.DescriptorValidationException getCause() {
        return (Descriptors.DescriptorValidationException) super.getCause();
    }
}

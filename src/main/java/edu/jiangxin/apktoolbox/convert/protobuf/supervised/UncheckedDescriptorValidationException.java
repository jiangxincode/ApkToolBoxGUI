package edu.jiangxin.apktoolbox.convert.protobuf.supervised;

import com.google.protobuf.Descriptors;

import java.util.Objects;

/**
 * Unchecked wrapper for {@link com.google.protobuf.Descriptors.DescriptorValidationException}.
 *
 * @author Daniel Tischner {@literal <zabuza.dev@gmail.com>}
 * @see com.google.protobuf.Descriptors.DescriptorValidationException
 */
@SuppressWarnings("SerializableHasSerializationMethods")
final class UncheckedDescriptorValidationException extends RuntimeException {

	private static final long serialVersionUID = -7092503313047011977L;

	/**
	 * Creates a new instance with given message and cause
	 *
	 * @param message The error message, can be null
	 * @param cause   The cause of the exception, not null
	 */
	UncheckedDescriptorValidationException(final String message,
			final Descriptors.DescriptorValidationException cause) {
		super(message, Objects.requireNonNull(cause));
	}

	/**
	 * Creates a new instance with default error message
	 *
	 * @param cause The cause of the exception, not null
	 */
	UncheckedDescriptorValidationException(final Descriptors.DescriptorValidationException cause) {
		super(Objects.requireNonNull(cause));
	}

	@Override
	public synchronized Descriptors.DescriptorValidationException getCause() {
		return (Descriptors.DescriptorValidationException) super.getCause();
	}
}

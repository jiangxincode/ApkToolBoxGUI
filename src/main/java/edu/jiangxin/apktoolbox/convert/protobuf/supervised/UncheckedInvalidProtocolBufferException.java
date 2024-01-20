package edu.jiangxin.apktoolbox.convert.protobuf.supervised;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Objects;

/**
 * Unchecked wrapper for {@link InvalidProtocolBufferException}.
 *
 * @author Daniel Tischner {@literal <zabuza.dev@gmail.com>}
 * @see InvalidProtocolBufferException
 */
@SuppressWarnings("SerializableHasSerializationMethods")
final class UncheckedInvalidProtocolBufferException extends RuntimeException {

	private static final long serialVersionUID = 4733167503348537421L;

	/**
	 * Creates a new instance with given message and cause
	 *
	 * @param message The error message, can be null
	 * @param cause   The cause of the exception, not null
	 */
	UncheckedInvalidProtocolBufferException(final String message, final InvalidProtocolBufferException cause) {
		super(message, Objects.requireNonNull(cause));
	}

	/**
	 * Creates a new instance with default error message
	 *
	 * @param cause The cause of the exception, not null
	 */
	UncheckedInvalidProtocolBufferException(final InvalidProtocolBufferException cause) {
		super(Objects.requireNonNull(cause));
	}

	@Override
	public synchronized InvalidProtocolBufferException getCause() {
		return (InvalidProtocolBufferException) super.getCause();
	}
}

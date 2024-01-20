package edu.jiangxin.apktoolbox.convert.protobuf.supervised;

/**
 * Thrown when no suitable protobuf descriptor could be found in the {@link DescriptorCache}
 * in order to decode the given binary protobuf message.
 *
 * @author Daniel Tischner {@literal <zabuza.dev@gmail.com>}
 */
@SuppressWarnings("SerializableHasSerializationMethods")
public final class NoDescriptorFoundException extends RuntimeException {

	private static final long serialVersionUID = -5797106115993481443L;

	/**
	 * Creates an instance with a default message.
	 */
	public NoDescriptorFoundException() {
	}

	/**
	 * Creates an instance with given error details.
	 *
	 * @param messageTypeName The name of the message type of the binary protobuf message that could not be decoded, can
	 *                        be null
	 */
	public NoDescriptorFoundException(final String messageTypeName) {
		super("No descriptor found for message type with name " + messageTypeName);
	}
}

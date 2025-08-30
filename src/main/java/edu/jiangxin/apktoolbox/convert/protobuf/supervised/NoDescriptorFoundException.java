package edu.jiangxin.apktoolbox.convert.protobuf.supervised;

public final class NoDescriptorFoundException extends RuntimeException {
    private static final long serialVersionUID = -5797106115993481443L;

    public NoDescriptorFoundException() {
    }

    public NoDescriptorFoundException(final String messageTypeName) {
        super("No descriptor found for message type with name " + messageTypeName);
    }
}

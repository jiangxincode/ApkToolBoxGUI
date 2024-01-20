package edu.jiangxin.apktoolbox.convert.protobuf.supervised;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.InflaterInputStream;

/**
 * API to decode binary protobuf messages to readable JSON, based on protobuf descriptors given as {@link
 * DescriptorCache}.
 * <p>
 * Instances can be created using the static factory methods {@link #fromCache(DescriptorCache)} and {@link
 * #fromEmptyCache()}. In order to add additional descriptors to the cache, it can be get using {@link #getCache()}.
 * <p>
 * Conversion can be done using one of the various {@code toJson} overloads, for example {@link #toJson(Path)}. Variants
 * accepting a {@code String messageTypeName}, like {@link #toJson(Path, String)} will use a descriptor suitable for the
 * given message type. Other variants will attempt to automatically find a best match among all available descriptors.
 * <p>
 * The conversion methods throw
 * <ul>
 *     <li>{@link UncheckedIOException} if an I/O error occurs during conversion,</li>
 *     <li>{@link NoDescriptorFoundException} if no suitable descriptor could be found and</li>
 *     <li>{@link UncheckedInvalidProtocolBufferException} if a message could not be parsed with the demanded descriptor.</li>
 * </ul>
 * <p>
 * Usage example:
 * <pre>{@code
 * DescriptorCache cache = DescriptorCache.fromDirectory(Path.of("descriptorCache"));
 * ProtoToJson protoToJson = ProtoToJson.fromCache(cache);
 *
 * String json = protoToJson.toJson(Path.of("someProtoMessage.message"));
 * }</pre>
 * Descriptors can be obtained by applying a {@code protoc} command on the protobuf schema {@code .proto}, for example:
 * <pre>{@code
 * protoc --descriptor_set_out foo.desc foo.proto
 * }</pre>
 *
 * @author Daniel Tischner {@literal <zabuza.dev@gmail.com>}
 */
public final class ProtoToJson {
	/**
	 * Creates an API from a given descriptor cache.
	 *
	 * @param cache The cache to use, not null
	 *
	 * @return The API instance
	 */
	public static ProtoToJson fromCache(final DescriptorCache cache) {
		Objects.requireNonNull(cache);

		return new ProtoToJson(cache);
	}

	/**
	 * Creates an API from an empty descriptor cache. The cache can be get using {@link #getCache()}.
	 *
	 * @return The API instance
	 */
	public static ProtoToJson fromEmptyCache() {
		return new ProtoToJson(DescriptorCache.emptyCache());
	}

	/**
	 * Decompresses the given data using ZLib (see {@link InflaterInputStream}).
	 *
	 * @param compressed The data to decompress, not null
	 *
	 * @return The decompressed data
	 *
	 * @throws IOException If an I/O error occurs during the decompression or if the given data is not ZLib encoded
	 */
	private static byte[] decompressZLib(final byte[] compressed) throws IOException {
		Objects.requireNonNull(compressed);
		try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(compressed);
				final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				final InflaterInputStream zLibOutputStream = new InflaterInputStream(inputStream)) {
			zLibOutputStream.transferTo(outputStream);
			return outputStream.toByteArray();
		}
	}

	/**
	 * The cache used by the API, not null.
	 */
	private final DescriptorCache cache;
	/**
	 * The printer used to convert protobuf messages to JSOn, not null.
	 */
	private final JsonFormat.Printer printer = JsonFormat.printer();

	/**
	 * Creates a new instance that uses the given descriptor cache.
	 *
	 * @param cache The cache to use, not null
	 */
	private ProtoToJson(final DescriptorCache cache) {
		this.cache = Objects.requireNonNull(cache);
	}

	/**
	 * Gets the descriptor cache used by the API. For example, to add more descriptors to the cache.
	 *
	 * @return The cache used by the API
	 */
	public DescriptorCache getCache() {
		return cache;
	}

	/**
	 * Converts the protobuf message given as a file to JSON.
	 * <p>
	 * The method will attempt to find a suitable descriptor to parse the message from the cache.
	 *
	 * @param messageFile Path to the protobuf message file, not null
	 *
	 * @return The JSON decoded message
	 *
	 * @throws NoDescriptorFoundException              If no suitable descriptor to parse the message could be found in
	 *                                                 the descriptor cache
	 * @throws UncheckedInvalidProtocolBufferException If the message is in an invalid format or not in the format
	 *                                                 expected by the descriptor
	 * @throws UncheckedIOException                    If an I/O error occurs during reading the file
	 */
	public String toJson(final Path messageFile) {
		Objects.requireNonNull(messageFile);

		try {
			return toJson(Files.readAllBytes(messageFile), (String) null);
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Converts the given raw protobuf message to JSON.
	 * <p>
	 * The method will attempt to find a suitable descriptor to parse the message from the cache.
	 *
	 * @param messageRaw The raw protobuf message, not null
	 *
	 * @return The JSON decoded message
	 *
	 * @throws NoDescriptorFoundException              If no suitable descriptor to parse the message could be found in
	 *                                                 the descriptor cache
	 * @throws UncheckedInvalidProtocolBufferException If the message is in an invalid format or not in the format
	 *                                                 expected by the descriptor
	 */
	public String toJson(final byte[] messageRaw) {
		Objects.requireNonNull(messageRaw);

		return toJson(messageRaw, (String) null);
	}

	/**
	 * Converts the protobuf message given as a file to JSON.
	 * <p>
	 * If {@code messageTypeName} is null, it will attempt to find a suitable descriptor from the cache. Otherwise it
	 * will use a descriptor for the given message type.
	 *
	 * @param messageFile     Path to the protobuf message file, not null
	 * @param messageTypeName The name of the messages type or null if the API should pick a suitable descriptor itself
	 *
	 * @return The JSON decoded message
	 *
	 * @throws NoDescriptorFoundException              If no suitable descriptor to parse the message could be found in
	 *                                                 the descriptor cache
	 * @throws UncheckedInvalidProtocolBufferException If the message is in an invalid format or not in the format
	 *                                                 expected by the descriptor
	 * @throws UncheckedIOException                    If an I/O error occurs during reading the file
	 */
	public String toJson(final Path messageFile, final String messageTypeName) {
		Objects.requireNonNull(messageFile);

		try {
			return toJson(Files.readAllBytes(messageFile), messageTypeName);
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Converts the given raw protobuf message to JSON.
	 * <p>
	 * If {@code messageTypeName} is null, it will attempt to find a suitable descriptor from the cache. Otherwise it
	 * will use a descriptor for the given message type.
	 *
	 * @param messageRaw      The raw protobuf message, not null
	 * @param messageTypeName The name of the messages type or null if the API should pick a suitable descriptor itself
	 *
	 * @return The JSON decoded message
	 *
	 * @throws NoDescriptorFoundException              If no suitable descriptor to parse the message could be found in
	 *                                                 the descriptor cache
	 * @throws UncheckedInvalidProtocolBufferException If the message is in an invalid format or not in the format
	 *                                                 expected by the descriptor
	 */
	@SuppressWarnings("WeakerAccess")
	public String toJson(final byte[] messageRaw, final String messageTypeName) {
		Objects.requireNonNull(messageRaw);

		if (messageTypeName == null) {
			final Optional<DynamicMessage> message = parseWithBestMatchingDescriptor(messageRaw);
			return toJson(message.orElseThrow(NoDescriptorFoundException::new));
		}

		final Optional<Descriptors.Descriptor> descriptor = cache.getByTypeName(messageTypeName);
		return toJson(messageRaw, descriptor.orElseThrow(() -> new NoDescriptorFoundException(messageTypeName)));
	}

	/**
	 * Converts the given raw protobuf message to JSON using the given descriptor.
	 *
	 * @param messageRaw The raw protobuf message, not null
	 * @param descriptor The descriptor to use for parsing the message, not null
	 *
	 * @return The JSON decoded message
	 *
	 * @throws UncheckedInvalidProtocolBufferException If the message is in an invalid format or not in the format
	 *                                                 expected by the given descriptor
	 */
	@SuppressWarnings("WeakerAccess")
	public String toJson(final byte[] messageRaw, final Descriptors.Descriptor descriptor) {
		Objects.requireNonNull(messageRaw);
		Objects.requireNonNull(descriptor);

		try {
			return toJson(DynamicMessage.parseFrom(descriptor, messageRaw));
		} catch (final InvalidProtocolBufferException e) {
			throw new UncheckedInvalidProtocolBufferException(e);
		}
	}

	/**
	 * Converts the given protobuf message to JSON.
	 *
	 * @param message The protobuf message, not null
	 *
	 * @return The JSON decoded message
	 *
	 * @throws UncheckedInvalidProtocolBufferException If the message is in an invalid format
	 */
	@SuppressWarnings("WeakerAccess")
	public String toJson(final DynamicMessage message) {
		Objects.requireNonNull(message);

		try {
			return printer.print(message);
		} catch (final InvalidProtocolBufferException e) {
			throw new UncheckedInvalidProtocolBufferException(e);
		}
	}

	/**
	 * Attempts to parse the given binary protobuf message with a suitable descriptor from the APIs descriptor cache.
	 *
	 * @param messageRaw The binary protobuf message to parse, not null
	 *
	 * @return The parsed protobuf message, if a suitable descriptor was found, otherwise empty
	 */
	private Optional<DynamicMessage> parseWithBestMatchingDescriptor(final byte[] messageRaw) {
		Objects.requireNonNull(messageRaw);

		return cache.getDescriptors()
				.stream()
				.map(descriptor -> {
					try {
						return DynamicMessage.parseFrom(descriptor, messageRaw);
					} catch (final InvalidProtocolBufferException e) {
						//noinspection ReturnOfNull
						return null;
					}
				})
				.filter(Objects::nonNull)
				.min(Comparator.comparingInt(message -> message.getUnknownFields()
						.asMap()
						.size()));
	}
}

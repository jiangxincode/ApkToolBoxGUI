package edu.jiangxin.apktoolbox.convert.protobuf.supervised;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * Cache containing protobuf descriptors used by {@link ProtoToJson} in order to decode binary protobuf messages.
 * <p>
 * An instance can be created using the static factory methods {@link #emptyCache()}, {@link #fromDirectory(Path)} and
 * {@link #fromFile(Path)}.
 * <p>
 * Additional descriptors can be added using {@link #addDescriptor(Descriptors.Descriptor)}, {@link
 * #addDescriptors(Path)} and {@link #addDescriptors(byte[])}. They can be received using {@link #getByTypeName(String)}
 * and {@link #getDescriptors()}.
 * <p>
 * Descriptors can be obtained by applying a {@code protoc} command on the protobuf schema {@code .proto}, for example:
 * <pre>{@code
 * protoc --descriptor_set_out foo.desc foo.proto
 * }</pre>
 *
 * @author Daniel Tischner {@literal <zabuza.dev@gmail.com>}
 */
public final class DescriptorCache {
	private static final Descriptors.FileDescriptor[] DEPENDENCIES = new Descriptors.FileDescriptor[0];

	/**
	 * Creates an instance that initially has no descriptors.
	 *
	 * @return The created instance
	 */
	public static DescriptorCache emptyCache() {
		return new DescriptorCache();
	}

	/**
	 * Creates an instance from a directory containing descriptor files.
	 * <p>
	 * The directory must not contain files that are not valid descriptor files.
	 * <p>
	 * Descriptor files can be obtained by applying a {@code protoc} command on the protobuf schema {@code .proto}, for
	 * example:
	 * <pre>{@code
	 * protoc --descriptor_set_out foo.desc foo.proto
	 * }</pre>
	 *
	 * @param directory The directory containing the descriptor files, not null.
	 *
	 * @return The created instance that has all descriptors available in the given directory
	 *
	 * @throws IllegalArgumentException                If the {@code directory} is not a directory
	 * @throws UncheckedIOException                    If an I/O error occurs during reading the files
	 * @throws UncheckedInvalidProtocolBufferException If a file is not a valid descriptor file
	 * @throws UncheckedDescriptorValidationException  If a file contains malformed descriptors
	 */
	public static DescriptorCache fromDirectory(final Path directory) {
		Objects.requireNonNull(directory);

		if (!Files.isDirectory(directory)) {
			throw new IllegalArgumentException("Path must be a directory: " + directory);
		}

		final DescriptorCache cache = new DescriptorCache();
		try (final Stream<Path> walk = Files.walk(directory)) {
			walk.filter(Files::isRegularFile)
					.forEach(cache::addDescriptors);
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
		return cache;
	}

	/**
	 * Creates an instance from a single descriptor file.
	 * <p>
	 * Descriptor files can be obtained by applying a {@code protoc} command on the protobuf schema {@code .proto}, for
	 * example:
	 * <pre>{@code
	 * protoc --descriptor_set_out foo.desc foo.proto
	 * }</pre>
	 *
	 * @param descriptorsFile The descriptor file, not null.
	 *
	 * @return The created instance that has all descriptors available in the given file
	 *
	 * @throws IllegalArgumentException                If the {@code descriptorsFile} is not a regular file
	 * @throws UncheckedIOException                    If an I/O error occurs during reading the files
	 * @throws UncheckedInvalidProtocolBufferException If the file is not a valid descriptor file
	 * @throws UncheckedDescriptorValidationException  If the file contains malformed descriptors
	 */
	public static DescriptorCache fromFile(final Path descriptorsFile) {
		Objects.requireNonNull(descriptorsFile);

		if (!Files.isRegularFile(descriptorsFile)) {
			throw new IllegalArgumentException("Path must be a regular file: " + descriptorsFile);
		}
		final DescriptorCache cache = new DescriptorCache();
		cache.addDescriptors(descriptorsFile);
		return cache;
	}

	/**
	 * Maps message type names to the descriptor suitable for parsing messages of that type, not null.
	 */
	private final Map<String, Descriptors.Descriptor> typeNameToDescriptor = new HashMap<>();

	/**
	 * Creates a new instance with no descriptors.
	 */
	private DescriptorCache() {

	}

	/**
	 * Adds the given descriptor to the cache.
	 * <p>
	 * Overwriting any descriptor previously registered for the same message type.
	 *
	 * @param descriptor The descriptor to add, not null
	 *
	 * @return The descriptor previously associated to the message type, if any
	 */
	@SuppressWarnings({ "WeakerAccess", "UnusedReturnValue" })
	public Optional<Descriptors.Descriptor> addDescriptor(final Descriptors.Descriptor descriptor) {
		Objects.requireNonNull(descriptor);
		final String typeName = Objects.requireNonNull(descriptor.getName());

		return Optional.ofNullable(typeNameToDescriptor.put(typeName, descriptor));
	}

	/**
	 * Adds all descriptors given in a descriptor file to the cache.
	 * <p>
	 * Overwriting any descriptors previously registered for the same message types.
	 * <p>
	 * Descriptor files can be obtained by applying a {@code protoc} command on the protobuf schema {@code .proto}, for
	 * example:
	 * <pre>{@code
	 * protoc --descriptor_set_out foo.desc foo.proto
	 * }</pre>
	 *
	 * @param descriptorsFile The descriptor file, not null.
	 *
	 * @throws IllegalArgumentException                If the {@code descriptorsFile} is not a regular file
	 * @throws UncheckedIOException                    If an I/O error occurs during reading the file
	 * @throws UncheckedInvalidProtocolBufferException If the file is not a valid descriptor file
	 * @throws UncheckedDescriptorValidationException  If the file contains malformed descriptors
	 */
	@SuppressWarnings("WeakerAccess")
	public void addDescriptors(final Path descriptorsFile) {
		Objects.requireNonNull(descriptorsFile);

		if (!Files.isRegularFile(descriptorsFile)) {
			throw new IllegalArgumentException("Path must be a regular file: " + descriptorsFile);
		}

		try {
			addDescriptors(Files.readAllBytes(descriptorsFile));
		} catch (final IOException e) {
			throw new UncheckedIOException("While reading: " + descriptorsFile.toAbsolutePath(), e);
		}
	}

	/**
	 * Adds all descriptors given as a raw descriptor set to the cache.
	 * <p>
	 * Overwriting any descriptors previously registered for the same message types.
	 *
	 * @param descriptorsRaw The raw descriptor set, not null.
	 *
	 * @throws UncheckedInvalidProtocolBufferException If the file is not a valid descriptor file
	 * @throws UncheckedDescriptorValidationException  If the file contains malformed descriptors
	 */
	@SuppressWarnings("WeakerAccess")
	public void addDescriptors(final byte[] descriptorsRaw) {
		Objects.requireNonNull(descriptorsRaw);

		try {
			final DescriptorProtos.FileDescriptorSet descriptorSet =
					DescriptorProtos.FileDescriptorSet.parseFrom(descriptorsRaw);
			for (final DescriptorProtos.FileDescriptorProto descriptorFile : descriptorSet.getFileList()) {
				final Descriptors.FileDescriptor fileDescriptor =
						Descriptors.FileDescriptor.buildFrom(descriptorFile, DescriptorCache.DEPENDENCIES);
				for (final Descriptors.Descriptor descriptor : fileDescriptor.getMessageTypes()) {
					addDescriptor(descriptor);
				}
			}
		} catch (final InvalidProtocolBufferException e) {
			throw new UncheckedInvalidProtocolBufferException(e);
		} catch (final Descriptors.DescriptorValidationException e) {
			throw new UncheckedDescriptorValidationException(e);
		}
	}

	/**
	 * Gets the descriptor registered for the given message type name, if any.
	 *
	 * @param typeName The message type name, not null
	 *
	 * @return The descriptor registered for the given message type name, if any
	 */
	public Optional<Descriptors.Descriptor> getByTypeName(final String typeName) {
		Objects.requireNonNull(typeName);

		return Optional.ofNullable(typeNameToDescriptor.get(typeName));
	}

	/**
	 * Gets all descriptors registered by this cache.
	 *
	 * @return An unmodifiable collection of all registered descriptors
	 */
	public Collection<Descriptors.Descriptor> getDescriptors() {
		return Collections.unmodifiableCollection(typeNameToDescriptor.values());
	}

	/**
	 * Gets all by this cache registered mappings of message type names to their descriptors.
	 *
	 * @return An unmodifiable collection of all registered message type name to descriptor mappings
	 */
	public Collection<Map.Entry<String, Descriptors.Descriptor>> getEntries() {
		return Collections.unmodifiableCollection(typeNameToDescriptor.entrySet());
	}

	/**
	 * Whether the cache has no descriptors registered.
	 *
	 * @return True if the cache has no descriptors registered, false otherwise
	 */
	public boolean isEmpty() {
		return typeNameToDescriptor.isEmpty();
	}

	/**
	 * Gets how many descriptors are currently registered to the cache.
	 *
	 * @return The amount of descriptors registered to the cache
	 */
	public int size() {
		return typeNameToDescriptor.size();
	}
}

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

public final class DescriptorCache {
    private static final Descriptors.FileDescriptor[] DEPENDENCIES = new Descriptors.FileDescriptor[0];

    public static DescriptorCache emptyCache() {
        return new DescriptorCache();
    }

    public static DescriptorCache fromDirectory(final Path directory) {
        Objects.requireNonNull(directory);

        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Path must be a directory: " + directory);
        }

        final DescriptorCache cache = new DescriptorCache();
        try (Stream<Path> walk = Files.walk(directory)) {
            walk.filter(Files::isRegularFile)
                    .forEach(cache::addDescriptors);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return cache;
    }

    public static DescriptorCache fromFile(final Path descriptorsFile) {
        Objects.requireNonNull(descriptorsFile);

        if (!Files.isRegularFile(descriptorsFile)) {
            throw new IllegalArgumentException("Path must be a regular file: " + descriptorsFile);
        }
        final DescriptorCache cache = new DescriptorCache();
        cache.addDescriptors(descriptorsFile);
        return cache;
    }

    private final Map<String, Descriptors.Descriptor> typeNameToDescriptor = new HashMap<>();

    private DescriptorCache() {

    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public Optional<Descriptors.Descriptor> addDescriptor(final Descriptors.Descriptor descriptor) {
        Objects.requireNonNull(descriptor);
        final String typeName = Objects.requireNonNull(descriptor.getName());

        return Optional.ofNullable(typeNameToDescriptor.put(typeName, descriptor));
    }

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

    @SuppressWarnings("WeakerAccess")
    public void addDescriptors(final byte[] descriptorsRaw) {
        Objects.requireNonNull(descriptorsRaw);

        try {
            final DescriptorProtos.FileDescriptorSet descriptorSet =
                    DescriptorProtos.FileDescriptorSet.parseFrom(descriptorsRaw);
            for (final DescriptorProtos.FileDescriptorProto descriptorFile : descriptorSet.getFileList()) {
                final Descriptors.FileDescriptor fileDescriptor =
                        Descriptors.FileDescriptor.buildFrom(descriptorFile, DEPENDENCIES);
                for (final Descriptors.Descriptor descriptor : fileDescriptor.getMessageTypes()) {
                    addDescriptor(descriptor);
                }
            }
        } catch (final InvalidProtocolBufferException | Descriptors.DescriptorValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Descriptors.Descriptor> getByTypeName(final String typeName) {
        Objects.requireNonNull(typeName);

        return Optional.ofNullable(typeNameToDescriptor.get(typeName));
    }

    public Collection<Descriptors.Descriptor> getDescriptors() {
        return Collections.unmodifiableCollection(typeNameToDescriptor.values());
    }

    public Collection<Map.Entry<String, Descriptors.Descriptor>> getEntries() {
        return Collections.unmodifiableCollection(typeNameToDescriptor.entrySet());
    }

    public boolean isEmpty() {
        return typeNameToDescriptor.isEmpty();
    }

    public int size() {
        return typeNameToDescriptor.size();
    }
}

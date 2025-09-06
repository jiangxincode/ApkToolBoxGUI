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

public final class ProtoToJson {
    public static ProtoToJson fromCache(final DescriptorCache cache) {
        Objects.requireNonNull(cache);

        return new ProtoToJson(cache);
    }

    public static ProtoToJson fromEmptyCache() {
        return new ProtoToJson(DescriptorCache.emptyCache());
    }

    private static byte[] decompressZLib(final byte[] compressed) throws IOException {
        Objects.requireNonNull(compressed);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(compressed);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             InflaterInputStream zLibOutputStream = new InflaterInputStream(inputStream)) {
            zLibOutputStream.transferTo(outputStream);
            return outputStream.toByteArray();
        }
    }

    private final DescriptorCache cache;
    private final JsonFormat.Printer printer = JsonFormat.printer();

    private ProtoToJson(final DescriptorCache cache) {
        this.cache = Objects.requireNonNull(cache);
    }

    public DescriptorCache getCache() {
        return cache;
    }

    public String toJson(final Path messageFile) {
        Objects.requireNonNull(messageFile);

        try {
            return toJson(Files.readAllBytes(messageFile), (String) null);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String toJson(final byte[] messageRaw) {
        Objects.requireNonNull(messageRaw);

        return toJson(messageRaw, (String) null);
    }

    public String toJson(final Path messageFile, final String messageTypeName) {
        Objects.requireNonNull(messageFile);

        try {
            return toJson(Files.readAllBytes(messageFile), messageTypeName);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String toJson(final byte[] messageRaw, final String messageTypeName) {
        Objects.requireNonNull(messageRaw);

        if (messageTypeName == null) {
            final Optional<DynamicMessage> message = parseWithBestMatchingDescriptor(messageRaw);
            return toJson(message.orElseThrow(RuntimeException::new));
        }

        final Optional<Descriptors.Descriptor> descriptor = cache.getByTypeName(messageTypeName);
        return toJson(messageRaw, descriptor.orElseThrow(() -> new RuntimeException(messageTypeName)));
    }

    @SuppressWarnings("WeakerAccess")
    public String toJson(final byte[] messageRaw, final Descriptors.Descriptor descriptor) {
        Objects.requireNonNull(messageRaw);
        Objects.requireNonNull(descriptor);

        try {
            return toJson(DynamicMessage.parseFrom(descriptor, messageRaw));
        } catch (final InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public String toJson(final DynamicMessage message) {
        Objects.requireNonNull(message);

        try {
            return printer.print(message);
        } catch (final InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

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

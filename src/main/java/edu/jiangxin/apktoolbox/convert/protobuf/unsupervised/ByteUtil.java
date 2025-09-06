package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import java.nio.ByteOrder;

public class ByteUtil {

    public static int bytesToInt(byte[] bytes) {
        return bytesToInt(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static int bytesToInt(byte[] bytes, ByteOrder byteOrder) {
        return bytesToInt(bytes, 0, byteOrder);
    }

    public static float bytesToFloat(byte[] bytes) {
        return bytesToFloat(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    private static float bytesToFloat(byte[] bytes, ByteOrder byteOrder) {
        return Float.intBitsToFloat(bytesToInt(bytes, byteOrder));
    }

    public static double bytesToDouble(byte[] bytes) {
        return bytesToDouble(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static double bytesToDouble(byte[] bytes, ByteOrder byteOrder) {
        return Double.longBitsToDouble(bytesToLong(bytes, byteOrder));
    }

    public static long bytesToLong(byte[] bytes) {
        return bytesToLong(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static long bytesToLong(byte[] bytes, ByteOrder byteOrder) {
        return bytesToLong(bytes, 0, byteOrder);
    }

    public static long bytesToLong(byte[] bytes, int start, ByteOrder byteOrder) {
        long values = 0;
        if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
            for (int i = Long.BYTES - 1; i >= 0; i--) {
                values <<= Byte.SIZE;
                values |= (bytes[i + start] & 0xff);
            }
        } else {
            for (int i = 0; i < Long.BYTES; i++) {
                values <<= Byte.SIZE;
                values |= (bytes[i + start] & 0xff);
            }
        }

        return values;
    }

    public static int bytesToInt(byte[] bytes, int start, ByteOrder byteOrder) {
        if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
            return bytes[start] & 0xFF | //
                    (bytes[1 + start] & 0xFF) << 8 | //
                    (bytes[2 + start] & 0xFF) << 16 | //
                    (bytes[3 + start] & 0xFF) << 24; //
        } else {
            return bytes[3 + start] & 0xFF | //
                    (bytes[2 + start] & 0xFF) << 8 | //
                    (bytes[1 + start] & 0xFF) << 16 | //
                    (bytes[start] & 0xFF) << 24; //
        }

    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }

    public static byte[] hex2bytes(String hexString) {
        String fixedHexString;
        if (hexString.length() % 2 != 0) {
            fixedHexString = hexString + "0";
        } else {
            fixedHexString = hexString;
        }
        int len = fixedHexString.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len-1; i += 2) {
            data[i / 2] = (byte) ((Character.digit(fixedHexString.charAt(i), 16) << 4)
                    + Character.digit(fixedHexString.charAt(i + 1), 16));
        }

        return data;
    }
}

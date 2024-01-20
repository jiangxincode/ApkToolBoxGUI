package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.Map;

public class BufferReader {

    private byte[] buffer;
    private int offset;
    private int savedOffset;

    public BufferReader(byte[] buffer) {
        this.buffer = buffer;
        offset = 0;
    }

    public int getOffset() {
        return offset;
    }

    public BigInteger readVarInt() {
        Map<String, Object> result = VarintUtils.decodeVarint(buffer, offset);
        offset += (int) result.get("length");
        return (BigInteger) result.get("value");
    }

    public byte[] readBuffer(int length) {
        this.checkByte(length);
        byte[] result = ArrayUtils.subarray(buffer, offset, offset + length);
        offset += length;
        return result;
    }

    public void trySkipGrpcHeader() {
        int backupOffset = offset;
        if (buffer.length > 0 && buffer[offset] == 0 && leftBytes() >= 5) {
            offset++;
            int length = ByteUtil.bytesToInt(buffer, offset, ByteOrder.BIG_ENDIAN);
            offset += 4;

            if (length > leftBytes()) {
                offset = backupOffset;
            }
        }
    }

    public int leftBytes() {
        return buffer.length - offset;
    }

    public void checkByte(int length) {
        int bytesAvailable = leftBytes();
        if (length > bytesAvailable) {
            throw new RuntimeException("Not enough bytes left. Requested: " + length + " left: " + bytesAvailable);
        } else if (length < 0) {
            throw new RuntimeException("The length should be greater than 0");
        }
    }

    public void checkpoint() {
        this.savedOffset = offset;
    }

    public void resetToCheckpoint() {
        offset = this.savedOffset;
    }

}

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
        this.offset = 0;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSavedOffset() {
        return savedOffset;
    }

    public void setSavedOffset(int savedOffset) {
        this.savedOffset = savedOffset;
    }

    public BigInteger readVarInt() {
        Map<String, Object> result = VarintUtils.decodeVarint(this.buffer, this.offset);
        this.offset += (int) result.get("length");
        return (BigInteger) result.get("value");
    }

    public byte[] readBuffer(int length) {
        this.checkByte(length);
        byte[] result = ArrayUtils.subarray(this.buffer, this.offset, this.offset + length);
        this.offset += length;
        return result;
    }

    public void trySkipGrpcHeader() {
        int backupOffset = this.offset;

        if (this.buffer.length > 0 && this.buffer[this.offset] == 0 && this.leftBytes() >= 5) {
            this.offset++;
            int length = ByteUtil.bytesToInt(this.buffer, this.offset, ByteOrder.BIG_ENDIAN);
            this.offset += 4;

            if (length > this.leftBytes()) {
                this.offset = backupOffset;
            }
        }
    }

    public int leftBytes() {
        return this.buffer.length - this.offset;
    }

    public void checkByte(int length) {
        int bytesAvailable = this.leftBytes();
        if (length > bytesAvailable) {
            throw new RuntimeException("Not enough bytes left. Requested: " + length + " left: " + bytesAvailable);
        } else if (length < 0) {
            throw new RuntimeException("The length should be greater than 0");
        }
    }

    public void checkpoint() {
        this.savedOffset = this.offset;
    }

    public void resetToCheckpoint() {
        this.offset = this.savedOffset;
    }

}

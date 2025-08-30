package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import java.util.List;

public class DecoderResult {

    /**
     * 字节范围
     */
    private List<Integer> byteRange;
    /**
     * 字段数字
     */
    private int fieldNumber;
    /**
     * 字段类型
     */
    private int type;
    /**
     * 字段对应的值
     */
    private Object content;
    /**
     * 字段对应的值的解码结果
     */
    private List<DecoderResult> subResults;

    public List<Integer> getByteRange() {
        return byteRange;
    }

    public void setByteRange(List<Integer> byteRange) {
        this.byteRange = byteRange;
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

    public void setFieldNumber(int fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public List<DecoderResult> getSubResults() {
        return subResults;
    }

    public void setSubResults(List<DecoderResult> subResults) {
        this.subResults = subResults;
    }
}

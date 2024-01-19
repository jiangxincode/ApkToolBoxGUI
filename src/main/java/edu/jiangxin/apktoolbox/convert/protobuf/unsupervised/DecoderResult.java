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
    private String fieldNumber;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 字段对应的值
     */
    private String content;
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

    public String getFieldNumber() {
        return fieldNumber;
    }

    public void setFieldNumber(String fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<DecoderResult> getSubResults() {
        return subResults;
    }

    public void setSubResults(List<DecoderResult> subResults) {
        this.subResults = subResults;
    }
}

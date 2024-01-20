package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import com.google.protobuf.WireFormat;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONObject;

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

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("byteRange", byteRange);
        json.put("fieldNumber", fieldNumber);
        json.put("type", typeToString(type));
        if (CollectionUtils.isEmpty(subResults)) {
            json.put("content", content);
        } else {
            json.put("content", subResults);
        }
        return json;
    }

    /**
     * 将字段类型转成字符串
     *
     * @param type
     * @return
     */
    private static String typeToString(int type) {
        switch (type) {
            case WireFormat.WIRETYPE_VARINT:
                return "varint";
            case WireFormat.WIRETYPE_LENGTH_DELIMITED:
                return "length_delimited";
            case WireFormat.WIRETYPE_FIXED32:
                return "fixed32";
            case WireFormat.WIRETYPE_FIXED64:
                return "fixed64";
            default:
                return "unknown";
        }
    }
}

package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProtobufDecoder {

    private static final int VARINT = 0;
    private static final int FIXED64 = 1;
    private static final int LENDELIM = 2;
    private static final int FIXED32 = 5;

    /**
     * 解码Proto数据结构
     *
     * @param buffer
     * @return
     */
    protected static Map<String, Object> decodeProto(byte[] buffer) {
        BufferReader reader = new BufferReader(buffer);
        List<Object> parts = new ArrayList<>();
        reader.trySkipGrpcHeader();

        try {
            while (reader.leftBytes() > 0) {
                reader.checkpoint();

                List<Integer> byteRange = new ArrayList<>();
                byteRange.add(reader.getOffset());
                int indexType = reader.readVarInt().intValue();
                int type = indexType & 0b111;
                int index = indexType >> 3;

                Object value;
                if (type == VARINT) {
                    value = reader.readVarInt();
                } else if (type == LENDELIM) {
                    int length = reader.readVarInt().intValue();
                    value = reader.readBuffer(length);
                } else if (type == FIXED32) {
                    value = reader.readBuffer(4);
                } else if (type == FIXED64) {
                    value = reader.readBuffer(8);
                } else {
                    throw new RuntimeException("Unknown type: " + type);
                }
                byteRange.add(reader.getOffset());

                Map<String, Object> part = new HashMap<>();
                part.put("byteRange", byteRange);
                part.put("index", index);
                part.put("type", type);
                part.put("value", value);
                parts.add(part);
            }
        } catch (Exception e) {
            reader.resetToCheckpoint();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("parts", parts);
        result.put("leftOver", reader.readBuffer(reader.leftBytes()));

        return result;
    }


    /**
     * 将字段类型转成字符串
     *
     * @param type
     * @param subType
     * @return
     */
    protected static String typeToString(int type, String subType) {
        switch (type) {
            case VARINT:
                return "varint";
            case LENDELIM:
                return StringUtils.isNotEmpty(subType) ? subType : "len_delim";
            case FIXED32:
                return "fixed32";
            case FIXED64:
                return "fixed64";
            default:
                return "unknown";
        }
    }


    /**
     * 解码protobuf格式的数据
     *
     * @param part
     * @return
     */
    protected static DecoderResult getProtobufPart(Map<String, Object> part) {
        DecoderResult result = new DecoderResult();
        result.setByteRange((List<Integer>) part.get("byteRange"));
        result.setFieldNumber(String.valueOf((int) part.get("index")));
        int type = (int) part.get("type");
        String subType = null;
        switch (type) {
            case VARINT:
                String value = part.get("value").toString();
                List<Map<String, Object>> varintResult = ProtobufPartDecoder.decodeVarintParts(value);
                result.setContent(JSONObject.valueToString(varintResult));
                break;
            case LENDELIM:
                byte[] bytes = (byte[]) part.get("value");
                Map<String, Object> decoded = decodeProto(bytes);
                if (bytes != null && bytes.length > 0 && decoded.get("leftOver") != null && ((byte[]) decoded.get("leftOver")).length == 0) {
                    subType = "protobuf";
                    List<Map<String, Object>> decodedParts = (List<Map<String, Object>>) decoded.get("parts");
                    List<DecoderResult> subResults = new ArrayList<>();
                    for (Map<String, Object> decodedPart : decodedParts) {
                        DecoderResult protobufPart = getProtobufPart(decodedPart);
                        subResults.add(protobufPart);
                    }
                    result.setSubResults(subResults);
                } else {
                    Map<String, Object> map = ProtobufPartDecoder.decodeStringOrBytes(bytes);
                    subType = (String) map.get("type");
                    result.setContent((String) map.get("value"));
                }
                break;
            case FIXED64:
                bytes = (byte[]) part.get("value");
                List<Map<String, Object>> fixed64Result = ProtobufPartDecoder.decodeFixed64(bytes);
                result.setContent(JSONObject.valueToString(fixed64Result));
                break;
            case FIXED32:
                bytes = (byte[]) part.get("value");
                List<Map<String, Object>> fixed32Result = ProtobufPartDecoder.decodeFixed32(bytes);
                result.setContent(JSONObject.valueToString(fixed32Result));
                break;
            default:
                break;
        }
        result.setType(typeToString(type, subType));
        return result;
    }


    /**
     * 解码字节数组
     *
     * @param bytes
     * @return
     */
    public static String bytesDecoder(byte[] bytes) {
        Map<String, Object> map = decodeProto(bytes);
        List<Map<String, Object>> parts = (List<Map<String, Object>>) map.get("parts");
        List<DecoderResult> result = new ArrayList<>();
        for (Map<String, Object> part : parts) {
            DecoderResult protobufPart = getProtobufPart(part);
            result.add(protobufPart);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", result);
        return jsonObject.toString(2);
    }
}

package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import com.google.protobuf.WireFormat;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProtobufDecoder {

    private static final String VALID_DATA = "valid_data";

    private static final String LEFT_OVER_DATA = "left_over_data";

    private static final String KEY_BYTE_RANGE = "byteRange";

    private static final String KEY_FIELD_NUMBER = "fieldNumber";

    private static final String KEY_TYPE = "type";

    private static final String KEY_VALUE = "value";

    /**
     * 解码字节数组
     *
     * @param bytes
     * @return
     */
    public static String bytesDecoder(byte[] bytes) {
        Map<String, Object> map = decodeProto(bytes);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> validItems = (List<Map<String, Object>>) map.get(VALID_DATA);
        List<JSONObject> result = new ArrayList<>();
        for (Map<String, Object> validItem : validItems) {
            DecoderResult protobufPart = getProtobufPart(validItem);
            JSONObject jsonObject = ProtobufUtils.getJsonObject(protobufPart);
            result.add(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(VALID_DATA, result);
        jsonObject.put(LEFT_OVER_DATA, map.get(LEFT_OVER_DATA));
        return jsonObject.toString(2);
    }

    /**
     * 解码Proto数据结构
     *
     * @param buffer
     * @return
     */
    private static Map<String, Object> decodeProto(byte[] buffer) {
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
                int fieldNumber = indexType >> 3;

                Object value;
                if (type == WireFormat.WIRETYPE_VARINT) {
                    value = reader.readVarInt();
                } else if (type == WireFormat.WIRETYPE_LENGTH_DELIMITED) {
                    int length = reader.readVarInt().intValue();
                    value = reader.readBuffer(length);
                } else if (type == WireFormat.WIRETYPE_FIXED32) {
                    value = reader.readBuffer(4);
                } else if (type == WireFormat.WIRETYPE_FIXED64) {
                    value = reader.readBuffer(8);
                } else {
                    throw new RuntimeException("Unknown type: " + type);
                }
                byteRange.add(reader.getOffset());

                Map<String, Object> part = new HashMap<>();
                part.put(KEY_BYTE_RANGE, byteRange);
                part.put(KEY_FIELD_NUMBER, fieldNumber);
                part.put(KEY_TYPE, type);
                part.put(KEY_VALUE, value);
                parts.add(part);
            }
        } catch (Exception e) {
            reader.resetToCheckpoint();
        }

        Map<String, Object> result = new HashMap<>();
        result.put(VALID_DATA, parts);
        result.put(LEFT_OVER_DATA, ByteUtil.bytesToHex(reader.readBuffer(reader.leftBytes())));

        return result;
    }

    /**
     * 解码protobuf格式的数据
     *
     * @param part
     * @return
     */
    private static DecoderResult getProtobufPart(Map<String, Object> part) {
        DecoderResult result = new DecoderResult();
        @SuppressWarnings("unchecked")
        List<Integer> byteRange = (List<Integer>) part.get(KEY_BYTE_RANGE);
        result.setByteRange(byteRange);
        result.setFieldNumber((int) part.get(KEY_FIELD_NUMBER));
        int type = (int) part.get(KEY_TYPE);
        Object value = part.get(KEY_VALUE);
        switch (type) {
            case WireFormat.WIRETYPE_VARINT:
                String valueStr = value.toString();
                result.setContent(decodeVarintParts(valueStr));
                break;
            case WireFormat.WIRETYPE_LENGTH_DELIMITED:
                byte[] bytes = (byte[]) value;
                Map<String, Object> decoded = decodeProto(bytes);
                String leftOverData = (String) decoded.get(LEFT_OVER_DATA);
                if (bytes != null && bytes.length > 0 && leftOverData != null && leftOverData.length() == 0) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> decodedParts = (List<Map<String, Object>>) decoded.get(VALID_DATA);
                    List<DecoderResult> subResults = new ArrayList<>();
                    for (Map<String, Object> decodedPart : decodedParts) {
                        DecoderResult protobufPart = getProtobufPart(decodedPart);
                        subResults.add(protobufPart);
                    }
                    result.setSubResults(subResults);
                } else {
                    Map<String, Object> map = decodeStringOrBytes(bytes);
                    result.setContent((String) map.get(KEY_VALUE));
                }
                break;
            case WireFormat.WIRETYPE_FIXED64:
                bytes = (byte[]) value;
                List<Map<String, Object>> fixed64Result = decodeFixed64(bytes);
                result.setContent(JSONObject.valueToString(fixed64Result));
                break;
            case WireFormat.WIRETYPE_FIXED32:
                bytes = (byte[]) value;
                List<Map<String, Object>> fixed32Result = decodeFixed32(bytes);
                result.setContent(JSONObject.valueToString(fixed32Result));
                break;
            default:
                break;
        }
        result.setType(type);
        return result;
    }


    /**
     * 解码为Fixed32格式数据
     *
     */
    private static List<Map<String, Object>> decodeFixed32(byte[] value) {
        float floatValue = ByteUtil.bytesToFloat(value);
        int intValue = ByteUtil.bytesToInt(value);
        int uintValue = ByteUtil.bytesToInt(value);

        List<Map<String, Object>> result = new ArrayList<>(3);
        Map<String, Object> map1 = new HashMap<>(2);
        map1.put(KEY_TYPE, "Int");
        map1.put(KEY_VALUE, intValue);
        result.add(map1);

        if (intValue != uintValue) {
            Map<String, Object> map2 = new HashMap<>(2);
            map2.put(KEY_TYPE, "Unsigned Int");
            map2.put(KEY_VALUE, uintValue);
            result.add(map2);
        }
        Map<String, Object> map3 = new HashMap<>(2);
        map3.put(KEY_TYPE, "Float");
        map3.put(KEY_VALUE, floatValue);
        result.add(map3);
        return result;
    }


    /**
     * 解码为Fixed64格式数据
     *
     */
    private static List<Map<String, Object>> decodeFixed64(byte[] value) {
        double floatValue = ByteUtil.bytesToDouble(value);
        BigInteger intValue = new BigInteger(ByteUtil.bytesToHex(value), 16);
        BigInteger uintValue = twoComplements(intValue);

        List<Map<String, Object>> result = new ArrayList<>(3);
        Map<String, Object> map1 = new HashMap<>(2);
        map1.put(KEY_TYPE, "Int");
        map1.put(KEY_VALUE, intValue.toString());
        result.add(map1);

        if (!intValue.equals(uintValue)) {
            Map<String, Object> map2 = new HashMap<>(2);
            map2.put(KEY_TYPE, "Unsigned Int");
            map2.put(KEY_VALUE, uintValue.toString());
            result.add(map2);
        }
        Map<String, Object> map3 = new HashMap<>(2);
        map3.put(KEY_TYPE, "Double");
        map3.put(KEY_VALUE, floatValue);
        result.add(map3);
        return result;
    }


    /**
     * 解码为字符串或16进制字符串
     *
     */
    private static Map<String, Object> decodeStringOrBytes(byte[] value) {
        Map<String, Object> result = new HashMap<>(2);
        if (ArrayUtils.isEmpty(value)) {
            result.put(KEY_TYPE, "string|bytes");
            result.put(KEY_VALUE, "");
            return result;
        }
        try {
            result.put(KEY_TYPE, "string");
            result.put(KEY_VALUE, hexStrToStr(ByteUtil.bytesToHex(value), "utf-8"));
        } catch (Exception e) {
            result.put(KEY_TYPE, "bytes");
            result.put(KEY_VALUE, ByteUtil.bytesToHex(value));
        }
        return result;
    }


    /**
     * 解码为Varint格式数据
     *
     */
    private static String decodeVarintParts(String value) {
        List<Map<String, Object>> result = new ArrayList<>(3);
        StringBuilder sb = new StringBuilder();
        BigInteger intVal = new BigInteger(value);
        Map<String, Object> map1 = new HashMap<>(2);
        sb.append("[as uint:");
        sb.append(intVal);
        sb.append("]");
        result.add(map1);

        BigInteger signedIntVal = VarintUtils.interpretAsSignedType(intVal);
        if (!signedIntVal.equals(intVal)) {
            Map<String, Object> map2 = new HashMap<>(2);
            sb.append("[as sint:");
            sb.append(signedIntVal);
            sb.append("]");
            result.add(map2);
        }
        return sb.toString();
    }


    /**
     * 补码
     *
     * @param uintValue
     * @return
     */
    private static BigInteger twoComplements(BigInteger uintValue) {
        if (uintValue.compareTo(new BigInteger("7fffffffffffffff", 16)) > 0) {
            return uintValue.subtract(new BigInteger("10000000000000000", 16));
        } else {
            return uintValue;
        }
    }

    /**
     * 将16进制字符串转字符串
     *
     * @param string
     * @param charsetName
     * @return
     */
    private static String hexStrToStr(String string, String charsetName) {
        if (string == null || string.equals("")) {
            return null;
        }
        byte[] baKeyword = new byte[string.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(string.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            string = new String(baKeyword, charsetName);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return string;
    }
}

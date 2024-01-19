package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtobufPartDecoder {

    /**
     * 解码为Fixed32格式数据
     *
     * @param value
     * @return
     */
    protected static List<Map<String, Object>> decodeFixed32(byte[] value) {
        float floatValue = ByteUtil.bytesToFloat(value);
        int intValue = ByteUtil.bytesToInt(value);
        int uintValue = ByteUtil.bytesToInt(value);

        List<Map<String, Object>> result = new ArrayList<>(3);
        Map<String, Object> map1 = new HashMap<>(2);
        map1.put("type", "Int");
        map1.put("value", intValue);
        result.add(map1);

        if (intValue != uintValue) {
            Map<String, Object> map2 = new HashMap<>(2);
            map2.put("type", "Unsigned Int");
            map2.put("value", uintValue);
            result.add(map2);
        }
        Map<String, Object> map3 = new HashMap<>(2);
        map3.put("type", "Float");
        map3.put("value", floatValue);
        result.add(map3);
        return result;
    }


    /**
     * 解码为Fixed64格式数据
     *
     * @param value
     * @return
     */
    protected static List<Map<String, Object>> decodeFixed64(byte[] value) {
        double floatValue = ByteUtil.bytesToDouble(value);
        BigInteger intValue = new BigInteger(Hex.toHexString(value), 16);
        BigInteger uintValue = twoComplements(intValue);

        List<Map<String, Object>> result = new ArrayList<>(3);
        Map<String, Object> map1 = new HashMap<>(2);
        map1.put("type", "Int");
        map1.put("value", intValue.toString());
        result.add(map1);

        if (!intValue.equals(uintValue)) {
            Map<String, Object> map2 = new HashMap<>(2);
            map2.put("type", "Unsigned Int");
            map2.put("value", uintValue.toString());
            result.add(map2);
        }
        Map<String, Object> map3 = new HashMap<>(2);
        map3.put("type", "Double");
        map3.put("value", floatValue);
        result.add(map3);
        return result;
    }


    /**
     * 解码为字符串或16进制字符串
     *
     * @param value
     * @return
     */
    protected static Map<String, Object> decodeStringOrBytes(byte[] value) {
        Map<String, Object> result = new HashMap<>(2);
        if (value.length == 0) {
            result.put("type", "string|bytes");
            result.put("value", "");
            return result;
        }
        try {
            result.put("type", "string");
            result.put("value", hexStrToStr(Hex.toHexString(value), "utf-8"));
        } catch (Exception e) {
            result.put("type", "bytes");
            result.put("value", Hex.toHexString(value));
        }
        return result;
    }


    /**
     * 解码为Varint格式数据
     *
     * @param value
     * @return
     */
    protected static List<Map<String, Object>> decodeVarintParts(String value) {
        List<Map<String, Object>> result = new ArrayList<>(3);
        BigInteger intVal = new BigInteger(value);
        Map<String, Object> map1 = new HashMap<>(2);
        map1.put("type", "Int");
        map1.put("value", String.valueOf(intVal));
        result.add(map1);

        BigInteger signedIntVal = VarintUtils.interpretAsSignedType(intVal);
        if (!signedIntVal.equals(intVal)) {
            Map<String, Object> map2 = new HashMap<>(2);
            map2.put("type", "Signed Int");
            map2.put("value", String.valueOf(signedIntVal));
            result.add(map2);
        }
        return result;
    }


    /**
     * 补码
     *
     * @param uintValue
     * @return
     */
    protected static BigInteger twoComplements(BigInteger uintValue) {
        if (uintValue.compareTo(new BigInteger("7fffffffffffffff", 16)) == 1) {
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
    protected static String hexStrToStr(String string, String charsetName) {
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
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return string;
    }


}

package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class VarintUtils {

    /**
     * 解码为有符号类型
     *
     * @param bigInteger
     * @return
     */
    protected static BigInteger interpretAsSignedType(BigInteger bigInteger){
        BigInteger i = bigInteger.and(BigInteger.ONE);
        if (i.equals(BigInteger.ZERO)) {
            return bigInteger.divide(BigInteger.TWO);
        }
        return new BigInteger("-1").multiply(bigInteger.add(BigInteger.ONE).divide(BigInteger.TWO));
    }


    /**
     * 解码可变整数
     *
     * @param buffer
     * @param offset
     * @return
     */
    protected static Map<String, Object> decodeVarint(byte[] buffer, int offset) {
        BigInteger res = BigInteger.ZERO;
        int shift = 0;
        byte bytes;

        int count = 0;
        do {
            if (offset >= buffer.length) {
                throw new RuntimeException("Index out of bound decoding varint");
            }
            count++;
            bytes = buffer[offset++];

            BigInteger multiplier =  BigInteger.TWO.pow(shift);
            BigInteger thisByteValue = new BigInteger(Long.toString(bytes & 0x7f)).multiply(multiplier);
            shift += 7;
            res = res.add(thisByteValue);
        } while ((bytes & 0xff) >= 128 && count < 8);

        Map<String, Object> result = new HashMap<>(2);
        result.put("value", res);
        result.put("length", shift / 7);
        return result;
    }

}

package edu.jiangxin.apktoolbox.convert.protobuf.unsupervised;

import com.google.protobuf.WireFormat;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONObject;

import java.util.List;

public class ProtobufUtils {
    public static JSONObject getJsonObject(DecoderResult decoderResult) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("byteRange", decoderResult.getByteRange());
        jsonObject.put("fieldNumber", decoderResult.getFieldNumber());

        List<DecoderResult> subResults = decoderResult.getSubResults();

        if (CollectionUtils.isEmpty(subResults)) {
            jsonObject.put("type", typeToString(decoderResult.getType(), false));
            jsonObject.put("content", decoderResult.getContent());
        } else {
            jsonObject.put("type", typeToString(decoderResult.getType(), true));
            for (DecoderResult subResult : decoderResult.getSubResults()) {
                jsonObject.append("content", getJsonObject(subResult));
            }
        }
        return jsonObject;
    }

    private static String typeToString(int type, boolean hasSubResults) {
        return switch (type) {
            case WireFormat.WIRETYPE_VARINT -> "varint";
            case WireFormat.WIRETYPE_LENGTH_DELIMITED -> {
                if (hasSubResults) {
                    yield "protobuf";
                }
                yield "string";
            }
            case WireFormat.WIRETYPE_FIXED32 -> "fixed32";
            case WireFormat.WIRETYPE_FIXED64 -> "fixed64";
            default -> "unknown";
        };
    }
}

package edu.jiangxin.apktoolbox.file.checksum;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.codec.digest.DigestUtils;

public class CalculationUtil {

    public static String calculate(final Hash selectedHash, final File file) {
        String result = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        try {
            switch (selectedHash) {
                case MD5: {
                    result = DigestUtils.md5Hex(fis);
                    break;
                }
                case Sha1: {
                    result = DigestUtils.sha1Hex(fis);
                    break;
                }
                case Sha256: {
                    result = DigestUtils.sha256Hex(fis);
                    break;
                }
                case Sha384: {
                    result = DigestUtils.sha384Hex(fis);
                    break;
                }
                case Sha512: {
                    result = DigestUtils.sha512Hex(fis);
                    break;
                }
                default: {
                    result = "return of the jedi";
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return result;
    }

}

package edu.jiangxin.apktoolbox.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;


/**
 * @author jiangxin
 * @author 2018-09-02
 *
 */
public class KeyVerify {
    
    public static String verify(String keyStorePath, char[] password, String alias, char[] aliasPassword) {
        KeyStore keyStore = null;
        FileInputStream fis = null;
        Key key = null;
        String message;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            fis = new FileInputStream(keyStorePath);
            keyStore.load(fis, password);
            key = keyStore.getKey(alias, aliasPassword);
            if (key == null) {
                message = "alias does not exist";
            }
            message = "OK";
        } catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException
                | UnrecoverableKeyException e) {
            message = e.getMessage();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    message = "closing fis fails";
                }
            }
        }
        return message;
    }
}

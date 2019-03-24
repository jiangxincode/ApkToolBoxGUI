package edu.jiangxin.apktoolbox.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class KeyVerify {

    public static String verify(String keyStorePath, char[] password, String alias, char[] aliasPassword) {
        KeyStore keyStore = null;
        FileInputStream fis = null;
        Key key = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            fis = new FileInputStream(keyStorePath);
            keyStore.load(fis, password);
            key = keyStore.getKey(alias, aliasPassword);
            if (key == null) {
                return "alias does not exist";
            }
            return "OK";
        } catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException
                | UnrecoverableKeyException e) {
            return e.getMessage();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    return e.getMessage();
                }
            }
        }

    }
}

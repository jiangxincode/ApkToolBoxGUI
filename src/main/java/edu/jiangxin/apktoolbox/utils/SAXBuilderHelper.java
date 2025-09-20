package edu.jiangxin.apktoolbox.utils;

import org.jdom2.input.SAXBuilder;

public class SAXBuilderHelper {
    public static void setSecurityFeatures(SAXBuilder builder) {
        builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
        builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    }
}

package edu.jiangxin.apktoolbox.file.checksum;

public enum DigestType {
    MD5(1, "MD5"), Sha1(2, "Sha1"), Sha256(3, "Sha256"), Sha384(4, "Sha384"), Sha512(5, "Sha512");

    private int id;
    private String name;

    DigestType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

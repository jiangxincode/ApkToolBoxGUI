package edu.jiangxin.apktoolbox.file.checksum;

public enum CalculateType {
    Md5(1, "Md5"),
    Sha1(2, "Sha1"),
    Sha256(3, "Sha256"),
    Sha384(4, "Sha384"),
    Sha512(5, "Sha512"),
    Crc32(6, "Crc32"),
    FileSize(7, "FileSize");

    private int id;
    private String name;

    CalculateType(int id, String name) {
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

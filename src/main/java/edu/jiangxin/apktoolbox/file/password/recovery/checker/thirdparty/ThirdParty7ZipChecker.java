package edu.jiangxin.apktoolbox.file.password.recovery.checker.thirdparty;

import edu.jiangxin.apktoolbox.utils.Constants;

public final class ThirdParty7ZipChecker extends AbstractThirdPartyChecker {
    @Override
    public String[] getFileExtensions() {
        return new String[]{"7z", "zip", "rar", "gz", "tar", "xz", "z", "001"};
    }

    @Override
    public String getFileDescription() {
        return "*.7z; *.zip; *.rar; ...";
    }

    @Override
    public String getDescription() {
        return "ThirdParty7ZipChecker(Using 7z.exe)";
    }

    @Override
    public String getToolPath() {
        return conf.getString(Constants.SEVEN_ZIP_PATH_KEY);
    }

    @Override
    public boolean isFiltered(String password) {
        if (password != null && password.contains("\"")) {
            // It is useless to escape the password
            logger.warn("checkPassword password contain double quote characters[Not Supported]");
            return true;
        }
        return false;
    }

    @Override
    public String getCmd(String password) {
        String target = file.getAbsolutePath();
        return String.format("%s t \"%s\" -p\"%s\"", toolPath, target, password);
    }
}

package edu.jiangxin.apktoolbox.file.password.recovery.checker.thirdparty;

import edu.jiangxin.apktoolbox.utils.Constants;

public class ThirdPartyWinRarChecker extends AbstractThirdPartyChecker {
    @Override
    public String[] getFileExtensions() {
        return new String[]{"rar", "zip", "7z", "arj", "bz2", "cab", "gz", "iso", "jar", "lz", "lzh", "tar", "uue", "xz", "z", "zst"};
    }

    @Override
    public String getFileDescription() {
        return "*.rar; *.zip; *.7z; ...";
    }

    @Override
    public String getDescription() {
        return "ThirdPartyWinRarChecker(Using WinRar.exe)";
    }

    @Override
    public String getToolPath() {
        return conf.getString(Constants.WIN_RAR_PATH_KEY);
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
        return String.format("%s t -inul -ibck -p\"%s\" \"%s\"", toolPath, password, target);
    }
}

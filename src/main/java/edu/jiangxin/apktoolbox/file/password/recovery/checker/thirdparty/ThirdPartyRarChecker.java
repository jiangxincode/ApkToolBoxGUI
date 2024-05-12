package edu.jiangxin.apktoolbox.file.password.recovery.checker.thirdparty;

import edu.jiangxin.apktoolbox.utils.Constants;

/**
 * The RAR command line supports a larger number of functions when compared to WinRAR, but does not support ZIP and other formats.
 * https://www.win-rar.com/cmd-shell-mode.html
 */
public final class ThirdPartyRarChecker extends AbstractThirdPartyChecker {
    @Override
    public String[] getFileExtensions() {
        return new String[]{"rar"};
    }

    @Override
    public String getFileDescription() {
        return "*.rar";
    }

    @Override
    public String getDescription() {
        return "ThirdPartyRarChecker(Using Rar.exe)";
    }

    @Override
    public String getToolPath() {
        return conf.getString(Constants.RAR_PATH_KEY);
    }

    @Override
    public boolean isFiltered(String password) {
        return false;
    }

    @Override
    public String getCmd(String password) {
        String target = file.getAbsolutePath();
        return String.format("%s t -p\"%s\" \"%s\"", toolPath, password, target);
    }
}

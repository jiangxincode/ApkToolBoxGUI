package edu.jiangxin.apktoolbox.swing.extend.plugin;

public interface IPlugin {

    void preparePlugin(IPreparePluginCallback callBack);

    String getPluginFilename();

    default boolean isPluginNeedUnzip() {
        return false;
    }

    String getPluginStartupCmd();

    default boolean isPluginNeedUnzipToSeparateDir() {
        return false;
    }
}

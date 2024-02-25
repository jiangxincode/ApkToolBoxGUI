package edu.jiangxin.apktoolbox.swing.extend.plugin;

public interface IPlugin {

    void preparePlugin(IPreparePluginCallback callBack);

    String getPluginFilename();

    boolean isPluginNeedUnzip();

    String getPluginStartupCmd();
}

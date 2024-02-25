package edu.jiangxin.apktoolbox.swing.extend.plugin;

public interface IPreparePluginCallback {
    void onPrepareStarted();

    void onCheckFinished(int result);

    void onDownloadFinished(int result);

    void onUnzipFinished(int result);

    void onPrepareFinished();
}

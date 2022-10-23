package edu.jiangxin.apktoolbox.file.password.recovery;

public interface Synchronizer {
    void setCurrentState(State currentState);

    State getCurrentState();

    void setProgressMaxValue(int maxValue);

    void increaseProgressBarValue();

    void setProgressBarValue(int value);

    void setCurrentPassword(String password);
}

package com.happynetwork.common.events;

/**
 * Created by Tom.yuan on 2016/10/09.
 */
public interface Float_UpdateApkDialogClickEvent {
    public abstract void updateApk(boolean closeWin);
    public abstract void closeWin();
    public abstract void updateState(int i);
}

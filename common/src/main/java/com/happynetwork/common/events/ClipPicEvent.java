package com.happynetwork.common.events;

/**
 * Created by Tom.yuan on 2016/9/10.
 */
public interface ClipPicEvent {
    public abstract void submitPic();
    public abstract void closeWin();
    public abstract void backClick(boolean isFormCamera);
}

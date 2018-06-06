package com.happynetwork.common.events;

import com.happynetwork.common.vo.LocalImageBean;

/**
 * Created by Tom.yuan on 2016/9/10.
 */
public interface ViewPhotoGroupClickEvent {
    public abstract void submitSelect(LocalImageBean bean);
    public abstract void closeWin();
    public abstract void backClick();
}

package com.happynetwork.common.events;
import com.happynetwork.common.vo.ImageBean;

/**
 * Created by Tom.yuan on 2016/9/10.
 */
public interface ViewPhotoClickEvent {
    public abstract void submitSelect(ImageBean bean);
    public abstract void closeWin();
    public abstract void backClick();
}

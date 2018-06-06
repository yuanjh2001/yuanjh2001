package com.happynetwork.vrestate.activitys;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.happynetwork.vrestate.R;


/**
 * 
 * @author Tom.yuan
 * 社交主界面
 * 2016-7-8
 * 下午2:51:54
 */

public class ContactListActivity extends BaseActivity implements OnClickListener{

    private static final String TAG = "SocialActivity";;
    private final int MAX_TAG = 4;
    private WeakReference<Fragment>[] mFragments = new WeakReference[MAX_TAG];
    private Fragment mCurFragment;
    private int mCurSelect = 0;
    private View mTags[] = new View[MAX_TAG];
    private View lineView[] = new View[MAX_TAG];
    private View view[] = new View[MAX_TAG];
   
    boolean isSwitch = false;
    PopupWindow window = null;
 // 账号在别处登录
    public boolean isConflict = false;
    private ImageView back,more;
    private TextView titleName;
 // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);
        switchTag(0);
    }
    
    @SuppressLint("NewApi")
    private void repalceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (manager.findFragmentById(R.id.main_fragment) == null) {
            ft.add(R.id.main_fragment, fragment);
        } else {
            ft.replace(R.id.main_fragment, fragment);
        }
        ft.commitAllowingStateLoss();
        manager.executePendingTransactions();
    }
    
    private void switchTag(int index) {//切换fragment
        if (mCurSelect < 0 || mCurSelect >= MAX_TAG)
            return;
        if (mFragments[index] == null || mFragments[index].get() == null) {
            switch(index) {
            case 0:
                mFragments[index] = new WeakReference<Fragment>(new ContactlistFragment());
                break;
            default:
                return;
            }
        }
        mCurSelect = index;
        mCurFragment = mFragments[mCurSelect].get();
        repalceFragment(mCurFragment);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
//        case R.id.group_top_layout:
//            if(mCurSelect==0){
//                return;
//            }
//            switchTag(0);
//            break;

        default:
            break;
        }
    }
    

}

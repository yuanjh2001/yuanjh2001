package com.happynetwork.vrestate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.happynetwork.vrestate.BaseApplication;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.activitys.SettingActivity;
import com.happynetwork.vrestate.activitys.UserMainActivity;
import com.happynetwork.vrestate.localdata.beans.UserInfo;
import com.happynetwork.vrestate.localdata.managers.UserServiceManager;
import com.happynetwork.vrestate.mywidgets.MyRoundImageView;
import com.happynetwork.vrestate.mywidgets.TextDrawable;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by wangfu on 2016/12/27.
 * 我的
 */
public class Me_Fragment extends BaseFragment implements View.OnClickListener {
    private TextView s_bi,liwu_tv,lv_tv,tv_uname,tv_jifen;
    private MyRoundImageView headImg;
    private LinearLayout uname_ly;
    private UserInfo userInfo;
    private ImageView sex_im;
    private ImageButton setting_bt;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_me, null);
        s_bi = (TextView) rootView.findViewById(R.id.s_bi_id);
        liwu_tv = (TextView) rootView.findViewById(R.id.liwu_id);
        lv_tv = (TextView) rootView.findViewById(R.id.lev_id);
        s_bi.setCompoundDrawables(null, getTb(getString(R.string.me_fragment_sbi_bt_tv)), null, null);
        liwu_tv.setCompoundDrawables(null, getTb(getString(R.string.me_fragment_liwu_bt_tv)), null, null);
        lv_tv.setCompoundDrawables(null, getTb(getString(R.string.me_fragment_lv_bt_tv)), null, null);
        headImg = (MyRoundImageView)rootView.findViewById(R.id.user_portrait);
        uname_ly = (LinearLayout)rootView.findViewById(R.id.uname_ly_id);
        tv_uname = (TextView)rootView.findViewById(R.id.tv_uname_id);
        headImg.setOnClickListener(this);
        uname_ly.setOnClickListener(this);
        sex_im = (ImageView)rootView.findViewById(R.id.sex_id);
        tv_jifen = (TextView)rootView.findViewById(R.id.tv_jifen_id);
        setting_bt = (ImageButton)rootView.findViewById(R.id.setting_id);
        setting_bt.setOnClickListener(this);

        return rootView;
    }

    private TextDrawable getTb(String s){
        TextDrawable d = new TextDrawable(getContext());
        d.setText(s);
        d.setTextColor(getResources().getColor(R.color.tips_txt));
        d.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.me_middle_title));
        d.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());//必须设置图片大小，否则不显示
        return d;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uname_ly_id:
            case R.id.user_portrait:
                openActivity(UserMainActivity.class);
                break;
            case R.id.setting_id:
                openActivity(SettingActivity.class);
                break;
        }
    }

    private void initDatas(){
        userInfo = UserServiceManager.getInstance().getUserInfo(BaseApplication.getInstance());
        if (!userInfo.getUserPic().equals("")) {
            BaseApplication.getInstance().getImLoader().displayImage(userInfo.getUserPic(), headImg, BaseApplication.getInstance().getImgOps());
        }
        tv_uname.setText(userInfo.getUserNick());
        if("1".equals(userInfo.getUserSex())){
            sex_im.setImageResource(R.drawable.ic_me_favicon_men);
        }else if("2".equals(userInfo.getUserSex())){
            sex_im.setImageResource(R.drawable.ic_me_favicon_women);
        }
        tv_jifen.setText(getText(R.string.me_fragment_jifen_tv).toString().replace("###",userInfo.getUserCredit()));
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatas();
        MobclickAgent.onPageStart("Me_Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Me_Fragment");
    }


}

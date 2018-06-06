package com.happynetwork.vrestate.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.listeners.PhoneCodeCallBack;
import com.happynetwork.vrestate.listeners.RegCallBack;
import com.happynetwork.vrestate.listeners.UpdateUserInfoByTokenCallBack;
import com.happynetwork.vrestate.localdata.beans.UserInfo;
import com.happynetwork.vrestate.localdata.managers.UserServiceManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 用户注册
 * Created by Tom.yuan on 2017/1/13.
 */
public class RegActivity extends BaseActivity implements View.OnClickListener {
    private TextView xieyi_tv;
    private int miao_count = 0;
    private Button getCheckCode,regBtn;
    private EditText tel_etv,checkcode_etv;
    private String tel_str,checkCode_str;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentViewBelowTitleBar(R.layout.activity_reg);
        setTitleName("注册");
        showBack();

        xieyi_tv = (TextView) findViewById(R.id.xieyi_id);
        xieyi_tv.setOnClickListener(this);
        getCheckCode = (Button)findViewById(R.id.t_getcheckcode_id);
        getCheckCode.setOnClickListener(this);
        tel_etv = (EditText)findViewById(R.id.tel_id);
        regBtn = (Button) findViewById(R.id.reg_id);
        regBtn.setOnClickListener(this);
        checkcode_etv = (EditText) findViewById(R.id.checkcode_id);

        checkBox = (CheckBox)findViewById(R.id.checkbox_id);
        checkBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.t_getcheckcode_id:
                getCheckCode.setEnabled(false);
                miao_count = 60;
                getCheckCode.setText(miao_count+"秒");
                getCheckCode.setTextColor(getResources().getColor(R.color.blue));
                handler.postDelayed(r,1000);
                tel_str = tel_etv.getText().toString();
                UserServiceManager.getInstance().setContext(RegActivity.this).setShowLoading(true).getPhoneCode(tel_str,  new PhoneCodeCallBack() {
                    @Override
                    public void phoneCodeCallBack(int error) {
                        if(error != 0){
                            if(miao_count > 0){
                                miao_count = 0;
                            }
                        }
                    }
                });
                break;
            case R.id.reg_id:
                if(checkBox.isChecked()){
                    tel_str = tel_etv.getText().toString();
                    checkCode_str = checkcode_etv.getText().toString();

                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserMobile(tel_str);
                    userInfo.setSmsCode(checkCode_str);
                    UserServiceManager.getInstance().setContext(RegActivity.this).setShowLoading(true).doReg(userInfo, new RegCallBack() {
                        @Override
                        public void regSuc(String token) {
                            getUserInfo(token);
                        }

                        @Override
                        public void regFail() {

                        }
                    });
                }else {
                    ToastUtils.createToast(RegActivity.this,"请先同意用户协议！");
                }
                break;
            case R.id.xieyi_id:
               openActivity(XieYiActivity.class);
                break;
        }
    }

    private void getUserInfo(String token){
        LogUtils.i(token);
        UserServiceManager.getInstance().setContext(RegActivity.this).setShowLoading(true).getUserInfo(token, new UpdateUserInfoByTokenCallBack() {
            @Override
            public void getUserInfoSuc(UserInfo userInfo) {
                UserServiceManager.getInstance().setuserInfo(userInfo,RegActivity.this);
                openActivity(MainActivity.class);
                finish();
            }

            @Override
            public void fail() {
                openActivity(MainActivity.class);
                finish();
            }
        });
    }

    @Override
    protected void doBack() {
        super.doBack();
        openActivity(GuideActivity.class);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            miao_count --;
            if(miao_count <= 0){
                miao_count = 0;
                getCheckCode.setText("重新发送");
                getCheckCode.setEnabled(true);
                getCheckCode.setTextColor(getResources().getColor(R.color.red));
            }else {
                getCheckCode.setText(miao_count+"秒");
                handler.postDelayed(r,1000);
            }
        }
    };

    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}

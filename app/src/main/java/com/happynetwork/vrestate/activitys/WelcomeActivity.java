package com.happynetwork.vrestate.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.happynetwork.common.activitys.Constant;
import com.happynetwork.common.nohttp.CallServer;

import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.Tools;
import com.happynetwork.vrestate.BaseApplication;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.adapters.ViewPagerAdapter;
import com.happynetwork.vrestate.beans.User;
import com.happynetwork.vrestate.localdata.UserDao;
import com.happynetwork.vrestate.utils.CommonUtils;
import com.happynetwork.vrestate.widget.DemoHXSDKHelper;
import com.happynetwork.vrestate.widget.HXSDKHelper;
import com.happynetwork.vrestate.listeners.UpdateUserInfoByTokenCallBack;
import com.happynetwork.vrestate.localdata.beans.UserInfo;
import com.happynetwork.vrestate.localdata.managers.UserServiceManager;
import com.umeng.analytics.MobclickAgent;
import com.yolanda.nohttp.download.DownloadRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomeActivity extends BaseActivity {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ViewPager viewPager;
    private ImageView imageView;
    private List<View> listViews;
    private int currPosition;
    private boolean readyToMain = false;
    private static final int sleepTime = 2000;
    private DownloadRequest mDownloadRequest;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        viewPager = (ViewPager) findViewById(R.id.yindao_viewpagers_id);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtils.i("state " + state + " currPosition " + currPosition);
                if (state == 0) {
                    if (currPosition == listViews.size() - 1) {
                        if (readyToMain) {
                            toMain();
                        }
                        readyToMain = true;
                    } else {
                        readyToMain = false;
                    }
                }
            }
        });
        boolean isLogined = DemoHXSDKHelper.getInstance().isLogined();
        LogUtils.i("isLogined===="+isLogined);
        if(!isLogined) {
            login();//模拟IM账号登录
        }else{
            //进入主页面
            Intent intent = new Intent(WelcomeActivity.this,
                    MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        LogUtils.i("Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);


    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            SharedPreferences preferences = getSharedPreferences("data", 0);
            String str = preferences.getString("yindaostr", "");
            if (str == null || str.equals("")) {
                setYinDaoAdapter();
            } else {
                toMain();
            }
        }
    };

    private void toMain() {
        SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();
        sharedata.putString("yindaostr", "1").commit();
        UserInfo userInfo = UserServiceManager.getInstance().getUserInfo(WelcomeActivity.this);
        if(userInfo.getToken().equals("")){
            openActivity(GuideActivity.class);
        }else {
            getUserInfo(userInfo.getToken());
        }
    }


    private void getUserInfo(String token){
        UserServiceManager.getInstance().setContext(WelcomeActivity.this).setShowLoading(false).getUserInfo(token, new UpdateUserInfoByTokenCallBack() {
            @Override
            public void getUserInfoSuc(UserInfo userInfo) {
                UserServiceManager.getInstance().setuserInfo(userInfo,WelcomeActivity.this);
                openActivity(MainActivity.class);
            }

            @Override
            public void fail() {
                openActivity(MainActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WelcomeActivity");
        MobclickAgent.onResume(this);
        if (BaseApplication.getInstance().isQuitApp) {
            BaseApplication.getInstance().isQuitApp = false;
            finish();
            System.exit(0);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WelcomeActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("onDestroy WelcomeActivity=====");
    }

    @Override
    protected void showStorageAlert() {
        checkCameraPermission();
    }

    @Override
    protected void showCamera() {
        mHandler.postDelayed(r, 3000);
    }

    public void setYinDaoAdapter() {
        ArrayList<Integer> list = new ArrayList();
        list.add(R.drawable.pic_guide1);
        list.add(R.drawable.pic_guide2);
        list.add(R.drawable.pic_guide3);
        list.add(R.drawable.pic_guide4);

        listViews = new ArrayList<View>();
        for (int i = 0; i < list.size(); i++) {
            imageView = new ImageView(WelcomeActivity.this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            listViews.add(imageView);
            Bitmap bitmap = Tools.readBitMap(WelcomeActivity.this, list.get(i));
            imageView.setImageBitmap(bitmap);
            //imageView.setImageResource(list.get(i));
        }
        viewPager.setAdapter(new ViewPagerAdapter(listViews));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mHandler.removeCallbacks(r);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            public void run() {
                if (DemoHXSDKHelper.getInstance().isLogined()) {
                    // ** 免登陆情况 加载所有本地群和会话
                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                    //加上的话保证进了主页面会话和群组都已经load完毕
                    long start = System.currentTimeMillis();
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    long costTime = System.currentTimeMillis() - start;
                    //等待sleeptime时长
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //进入主页面
//                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    LogUtils.i("onStart  WelcomeActivity isLogin===");
                }else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    LogUtils.i("onStart  WelcomeActivity isLogin no===");
//                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                }
            }
        }).start();
    }


    /**
     * 登录
     *
     */
    public void login() {
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }

        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
       final String currentUsername = "15811221365";
        final String currentPassword = "123456";
        EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                // 登陆成功，保存用户名密码
                BaseApplication.getInstance().setUserName(currentUsername);
                BaseApplication.getInstance().setPassword(currentPassword);

                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    // 处理好友和群组
//                    initializeContacts();

                    // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
                    List<String> usernames = EMContactManager.getInstance().getContactUserNames();
                    EMLog.d("roster", "contacts size: " + usernames.size());
                    Map<String, User> userlist = new HashMap<String, User>();
                    for (String username : usernames) {
                        User user = new User();
                        user.setUsername(username);
                        setUserHearder(username, user);
                        userlist.put(username, user);
                    }
                    // 添加user"申请与通知"
                    User newFriends = new User();
                    newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
                    newFriends.setNick("申请与通知");
                    newFriends.setHeader("");
                    userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
                    // 添加"群聊"
                    User groupUser = new User();
                    groupUser.setUsername(Constant.GROUP_USERNAME);
                    groupUser.setNick("群聊");
                    groupUser.setHeader("");
                    userlist.put(Constant.GROUP_USERNAME, groupUser);

                    // 存入内存
                    BaseApplication.getInstance().setContactList(userlist);
                    // 存入db
                    UserDao dao = new UserDao(WelcomeActivity.this);
                    List<User> users = new ArrayList<User>(userlist.values());
                    dao.saveContactList(users);

                    // 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
                    EMGroupManager.getInstance().getGroupsFromServer();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    runOnUiThread(new Runnable() {
                        public void run() {
                            DemoHXSDKHelper.getInstance().logout(true,null);
                            Toast.makeText(getApplicationContext(), R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        BaseApplication.currentUserNick.trim());
                if (!updatenick) {
                    LogUtils.e("LoginActivity ,update current user nick fail");
                }
                LogUtils.i("easemob login success=====");
                // 进入主页面
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     *
     * @param username
     * @param user
     */
    protected void setUserHearder(String username, User user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }





}

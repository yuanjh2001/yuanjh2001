package com.happynetwork.vrestate.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMGroupChangeListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.happynetwork.common.activitys.Constant;
import com.happynetwork.vrestate.utils.CommonUtils;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.beans.InviteMessage;
import com.happynetwork.vrestate.beans.User;
import com.happynetwork.vrestate.fragments.Find_Fragment;
import com.happynetwork.vrestate.fragments.Me_Fragment;
import com.happynetwork.vrestate.fragments.Message_Fragment;
import com.happynetwork.vrestate.fragments.Service_Fragment;
import com.happynetwork.vrestate.fragments.SettingsFragment;
import com.happynetwork.vrestate.fragments.Share_Fragment;
import com.happynetwork.vrestate.localdata.InviteMessgeDao;
import com.happynetwork.vrestate.localdata.UserDao;
import com.happynetwork.vrestate.widget.DemoHXSDKHelper;
import com.happynetwork.vrestate.widget.HXSDKHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;

import android.view.ContextMenu;
import android.view.ContextMenu.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends BaseActivity implements View.OnClickListener, EMEventListener {
    private static boolean readyExit = false;
    private LinearLayout ll_find, ll_share, ll_ser, ll_msg, ll_me;
    private ImageView iv_find, iv_share, iv_ser, iv_msg, iv_me,rl_back,rl_commomsearch;
    private TextView tv_find, tv_share, tv_ser, tv_msg, tv_me;
    private ArrayList<Fragment> fragmentList;
    private int currenTab;

    protected static final String TAG = "MainActivity";
    // 未读消息textview
    private TextView unreadLabel;
    // 未读通讯录textview
    private TextView unreadAddressLable;

    private Button[] mTabs;
    private ContactlistFragment contactListFragment;
    // private ChatHistoryFragment chatHistoryFragment;
    private ChatAllHistoryFragment chatHistoryFragment;
    private SettingsFragment settingFragment;
    private Fragment[] fragments;
    private int index;
    private ImageView rl_list;
    // 当前fragment的index
    private int currentTabIndex;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;

    private MyConnectionListener connectionListener = null;
    private MyGroupChangeListener groupChangeListener = null;
    private final static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100;
    private final static int CAMERA_REQUEST_CODE = 101;
    private final static int ACCESS_COARSE_LOCATION_REQUEST_CODE = 102;
    private static final int RECORD_AUDIO_REQUEST_CODE = 105;
    private static final int MICROPHONE_REQUEST_CODE = 106;
    private final static int WRITE_CALL_PHONE_REQUEST_CODE = 107;
    private PopupWindow window = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_main);

        checkStoragePermission();
        checkReadStoragePermission();
        checkRecordAudioPermission();
        checkCallPrhonePermission();
        checkCallPrivilegedPermission();
        checkAccessCoarseLocationPermission();


        ll_find = (LinearLayout) findViewById(R.id.ll_find);
        ll_share = (LinearLayout) findViewById(R.id.ll_share);
        ll_ser = (LinearLayout) findViewById(R.id.ll_ser);

        ll_msg = (LinearLayout) findViewById(R.id.ll_msg);
        ll_me = (LinearLayout) findViewById(R.id.ll_me);
        iv_find = (ImageView) findViewById(R.id.iv_find);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_ser = (ImageView) findViewById(R.id.iv_ser);
        iv_msg = (ImageView) findViewById(R.id.iv_msg);
        iv_me = (ImageView) findViewById(R.id.iv_me);
        tv_find = (TextView) findViewById(R.id.tv_find);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_ser = (TextView) findViewById(R.id.tv_ser);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_me = (TextView) findViewById(R.id.tv_me);
        rl_back = (ImageView) findViewById(R.id.rl_back);
        rl_commomsearch = (ImageView) findViewById(R.id.rl_commomsearch);
        rl_back.setImageResource(R.drawable.ic_nav_screen);
        rl_commomsearch.setImageResource(R.drawable.ic_nav_publish);
        rl_back.setVisibility(View.VISIBLE);
        rl_commomsearch.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);
        rl_commomsearch.setOnClickListener(this);
        ll_find.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_ser.setOnClickListener(this);
        ll_msg.setOnClickListener(this);
        ll_me.setOnClickListener(this);
        iv_find.setSelected(true);
        tv_find.setTextColor(getResources().getColor(R.color.bottom_tv_check));

        rl_list = (ImageView)findViewById(R.id.rl_list);
        rl_list.setImageResource(R.mipmap.ic_nav_select);
        rl_list.setVisibility(View.VISIBLE);
        rl_list.setOnClickListener(this);
        fragmentList = new ArrayList<Fragment>();
        //发现
        Find_Fragment findFragment = new Find_Fragment();
        //分享
        Share_Fragment shareFragment = new Share_Fragment();
        //服务
        Service_Fragment serviceFragment = new Service_Fragment();
        //消息
        ChatAllHistoryFragment messageFragment = new ChatAllHistoryFragment();
        ContactlistFragment contactlistFragment = new ContactlistFragment();
        //我的
        Me_Fragment meFragment = new Me_Fragment();
        fragmentList.add(findFragment);
        fragmentList.add(shareFragment);
        fragmentList.add(serviceFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(meFragment);

        // 默认显示第一页
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_content, fragmentList.get(0));
        ft.commit();
        setTitleName(getResources().getString(R.string.radar));

        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            DemoHXSDKHelper.getInstance().logout(true, null);
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            return;
        }

        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
        //异步获取当前用户的昵称和头像
        boolean isLogined = DemoHXSDKHelper.getInstance().isLogined();
        if (!isLogined) {
            init();
            //异步获取当前用户的昵称和头像
            ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().asyncGetCurrentUserInfo();
        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        LogUtils.i("MainActivity onCreate=====");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_find:
                resetView();
                setTitleName(getResources().getString(R.string.radar));
                iv_find.setSelected(true);
                tv_find.setTextColor(getResources().getColor(R.color.bottom_tv_check));
                showTab(0);
                showToolBar();
                rl_back.setVisibility(View.VISIBLE);
                rl_commomsearch.setVisibility(View.VISIBLE);
                rl_list.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_share:
                setTitleName(getResources().getString(R.string.tab_share));
                resetView();
                iv_share.setSelected(true);
                tv_share.setTextColor(getResources().getColor(R.color.bottom_tv_check));
                showTab(1);
                showToolBar();
                rl_back.setVisibility(View.INVISIBLE);
                rl_commomsearch.setVisibility(View.INVISIBLE);
                rl_list.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_ser:
                setTitleName(getResources().getString(R.string.tab_service));
                resetView();
                iv_ser.setSelected(true);
                tv_ser.setTextColor(getResources().getColor(R.color.bottom_tv_check));
                showTab(2);
                showToolBar();
                rl_back.setVisibility(View.INVISIBLE);
                rl_commomsearch.setVisibility(View.INVISIBLE);
                rl_list.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_msg:
                setTitleName(getResources().getString(R.string.tab_message));
                resetView();
                iv_msg.setSelected(true);
                tv_msg.setTextColor(getResources().getColor(R.color.bottom_tv_check));
                showTab(3);
                showToolBar();
                rl_back.setVisibility(View.INVISIBLE);
                rl_commomsearch.setVisibility(View.INVISIBLE);
                rl_list.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_me:
                setTitleName(getResources().getString(R.string.tab_me));
                resetView();
                iv_me.setSelected(true);
                tv_me.setTextColor(getResources().getColor(R.color.bottom_tv_check));
                showTab(4);
                hideToolBar();
                rl_back.setVisibility(View.INVISIBLE);
                rl_commomsearch.setVisibility(View.INVISIBLE);
                rl_list.setVisibility(View.INVISIBLE);
                break;
            case R.id.rl_back://筛选
                filterDialog(v);
                break;
            case R.id.rl_commomsearch://发起邀约的弹框
                toInvitePopWindow(v);
                break;
            case R.id.rl_list:

                break;
            case R.id.invite_layout://邀约
                Intent intentInvite = new Intent();
                intentInvite.setClass(this,InviteActivity.class);
                startActivity(intentInvite);
                break;
            case R.id.invited_layout://应邀
                Intent intentInvited = new Intent();
                intentInvited.setClass(this,InvitedActivity.class);
                startActivity(intentInvited);
                break;
            case R.id.add_friend_layout://添加好友

                break;
        }
    }

    /**
     * 发起邀约弹出框
     */
    private void toInvitePopWindow(View v){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popView = inflater.inflate(R.layout.popwindow_invite_layout, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window = new PopupWindow(popView,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        LinearLayout inviteLayout = (LinearLayout) popView.findViewById(R.id.invite_layout);
        LinearLayout invitedLayout = (LinearLayout) popView.findViewById(R.id.invited_layout);
        LinearLayout addFriendLayout = (LinearLayout) popView.findViewById(R.id.add_friend_layout);
        inviteLayout.setOnClickListener(this);
        invitedLayout.setOnClickListener(this);
        addFriendLayout.setOnClickListener(this);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        window.update();

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(dw);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp =getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int xPos = location[0]-window.getWidth();
        int yPos = location[1]-window.getHeight();
        Log.i(TAG, "xPos=="+xPos+" yPos=="+yPos);
////        window.showAtLocation(v, Gravity.TOP, xPos, yPos);
        int positonx =  -xPos+670;
        LogUtils.i("x===="+positonx);
        window.showAsDropDown(v,positonx,0);

    }
    private void resetView() {
        iv_find.setSelected(false);
        iv_share.setSelected(false);
        iv_ser.setSelected(false);
        iv_msg.setSelected(false);
        iv_me.setSelected(false);
        tv_find.setTextColor(getResources().getColor(R.color.bottom_tv));
        tv_share.setTextColor(getResources().getColor(R.color.bottom_tv));
        tv_ser.setTextColor(getResources().getColor(R.color.bottom_tv));
        tv_msg.setTextColor(getResources().getColor(R.color.bottom_tv));
        tv_me.setTextColor(getResources().getColor(R.color.bottom_tv));
    }

    private void showTab(int idx) {
        Fragment fragment = fragmentList.get(idx);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment.isAdded()) {
            fragment.onResume();
        } else {
            ft.add(R.id.frame_content, fragment);
        }
        show(idx);
        ft.commit();
    }

    private void show(int paramInt) {
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            if (paramInt == i) {
                fragmentTransaction.show(fragment);

            } else {
                fragmentTransaction.hide(fragment);
            }
            fragmentTransaction.commit();
        }
        currenTab = paramInt;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (!isConflict && !isCurrentAccountRemoved) {
            EMChatManager.getInstance().activityResumed();
        }
        LogUtils.i("onResume");
        // unregister this event listener when this activity enters the
        // background
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
        sdkHelper.pushActivity(this);

        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
        LogUtils.i("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i("onPause");
        MobclickAgent.onPause(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!readyExit) {
                readyExit = true; // 准备退出
                ToastUtils.createToast(getApplication(), "再按一次退出程序");
                readyExitandler.postDelayed(readyExitRunnable, 3000);
            } else {
                exitApp();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void exitApp() {
        super.exitApp();
        this.finish();
    }

    Runnable readyExitRunnable = new Runnable() {
        @Override
        public void run() {
            readyExit = false;
        }
    };
    Handler readyExitandler = new Handler(Looper.getMainLooper());

    private void init() {
        // setContactListener监听联系人的变化等
        EMContactManager.getInstance().setContactListener(new MyContactListener());
        // 注册一个监听连接状态的listener

        connectionListener = new MyConnectionListener();
        EMChatManager.getInstance().addConnectionListener(connectionListener);

        groupChangeListener = new MyGroupChangeListener();
        // 注册群聊相关的listener
        EMGroupManager.getInstance().addGroupChangeListener(groupChangeListener);
        //内部测试方法，请忽略
        registerInternalDebugReceiver();
    }


    static void asyncFetchGroupsFromServer() {
        HXSDKHelper.getInstance().asyncFetchGroupsFromServer(new EMCallBack() {
            @Override
            public void onSuccess() {
                HXSDKHelper.getInstance().noitifyGroupSyncListeners(true);

                if (HXSDKHelper.getInstance().isContactsSyncedWithServer()) {
                    HXSDKHelper.getInstance().notifyForRecevingEvents();
                }
            }

            @Override
            public void onError(int code, String message) {
                HXSDKHelper.getInstance().noitifyGroupSyncListeners(false);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

        });
    }

    static void asyncFetchContactsFromServer() {
        HXSDKHelper.getInstance().asyncFetchContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> usernames) {
                Context context = HXSDKHelper.getInstance().getAppContext();
                System.out.println("----------------" + usernames.toString());
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
                String strChat = context.getString(R.string.Application_and_notify);
                newFriends.setNick(strChat);
                userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
                // 添加"群聊"
                User groupUser = new User();
                String strGroup = context.getString(R.string.group_chat);
                groupUser.setUsername(Constant.GROUP_USERNAME);
                groupUser.setNick(strGroup);
                groupUser.setHeader("");
                userlist.put(Constant.GROUP_USERNAME, groupUser);

                // 添加"聊天室"
                User chatRoomItem = new User();
                String strChatRoom = context.getString(R.string.chat_room);
                chatRoomItem.setUsername(Constant.CHAT_ROOM);
                chatRoomItem.setNick(strChatRoom);
                chatRoomItem.setHeader("");
                userlist.put(Constant.CHAT_ROOM, chatRoomItem);

                // 添加"Robot"
                User robotUser = new User();
                String strRobot = context.getString(R.string.robot_chat);
                robotUser.setUsername(Constant.CHAT_ROBOT);
                robotUser.setNick(strRobot);
                robotUser.setHeader("");
                userlist.put(Constant.CHAT_ROBOT, robotUser);

                // 存入内存
                ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
                // 存入db
                UserDao dao = new UserDao(context);
                List<User> users = new ArrayList<User>(userlist.values());
                dao.saveContactList(users);

                HXSDKHelper.getInstance().notifyContactsSyncListener(true);

                if (HXSDKHelper.getInstance().isGroupsSyncedWithServer()) {
                    HXSDKHelper.getInstance().notifyForRecevingEvents();
                }

                ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().asyncFetchContactInfosFromServer(usernames, new EMValueCallBack<List<User>>() {

                    @Override
                    public void onSuccess(List<User> uList) {
                        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).updateContactList(uList);
                        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().notifyContactInfosSyncListener(true);
                    }

                    @Override
                    public void onError(int error, String errorMsg) {
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                HXSDKHelper.getInstance().notifyContactsSyncListener(false);
            }

        });
    }

    static void asyncFetchBlackListFromServer() {
        HXSDKHelper.getInstance().asyncFetchBlackListFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> value) {
                EMContactManager.getInstance().saveBlackList(value);
                HXSDKHelper.getInstance().notifyBlackListSyncListener(true);
            }

            @Override
            public void onError(int error, String errorMsg) {
                HXSDKHelper.getInstance().notifyBlackListSyncListener(false);
            }

        });
    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     *
     * @param username
     * @param user
     */
    private static void setUserHearder(String username, User user) {
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
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }


    /**
     * 监听事件
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) event.getData();

                // 提示新消息
                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

                refreshUI();
                break;
            }

            case EventOfflineMessage: {
                refreshUI();
                break;
            }

            case EventConversationListChanged: {
                refreshUI();
                break;
            }

            default:
                break;
        }
    }

    private void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                if (currentTabIndex == 0) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    if (chatHistoryFragment != null) {
                        chatHistoryFragment.refresh();
                    }
                }
            }
        });
    }


    private  void filterDialog(View v){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popView = inflater.inflate(R.layout.popwindow_filter_layout, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window = new PopupWindow(popView,
                WindowManager.LayoutParams.MATCH_PARENT,
               1400);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        window.update();

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(dw);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp =getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int xPos = location[0]-window.getWidth();
        int yPos = location[1]-window.getHeight();
        int positonx =  -xPos+670;
        LogUtils.i("x===="+positonx);
        window.showAsDropDown(v,15,0);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

        if (connectionListener != null) {
            EMChatManager.getInstance().removeConnectionListener(connectionListener);
        }

        if (groupChangeListener != null) {
            EMGroupManager.getInstance().removeGroupChangeListener(groupChangeListener);
        }

        try {
            if (internalDebugReceiver != null) {
                unregisterReceiver(internalDebugReceiver);
            }

        } catch (Exception e) {
        }
    }


    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        int count = unreadMsgCountTotal - chatroomUnreadMsgCount;
        return count;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    /***
     * 好友变化listener
     */
    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            // 保存增加的联系人
            Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
            Map<String, User> toAddUsers = new HashMap<String, User>();
            for (String username : usernameList) {
                User user = setUserHead(username);
                // 添加好友时可能会回调added方法两次
                if (!localUsers.containsKey(username)) {
                    userDao.saveContact(user);
                }
                toAddUsers.put(username, user);
            }
            localUsers.putAll(toAddUsers);
            // 刷新ui
            if (currentTabIndex == 1)
                contactListFragment.refresh();

        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
            for (String username : usernameList) {
                localUsers.remove(username);
                userDao.deleteContact(username);
                inviteMessgeDao.deleteMessage(username);
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    // 如果正在与此用户的聊天页面
                    String st10 = getResources().getString(R.string.have_you_removed);
                    if (ChatActivity.activityInstance != null
                            && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_SHORT)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                    // 刷新ui
                    contactListFragment.refresh();
                    chatHistoryFragment.refresh();
                }
            });

        }

        @Override
        public void onContactInvited(String username, String reason) {

            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            Log.d(TAG, username + "请求加你为好友,reason: " + reason);
            // 设置相应status
            msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactAgreed(String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            Log.d(TAG, username + "同意了你的好友请求");
            msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactRefused(String username) {

            // 参考同意，被邀请实现此功能,demo未实现
            Log.d(username, username + "拒绝了你的好友请求");
        }

    }

    /**
     * 连接监听listener
     */
    public class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            boolean groupSynced = HXSDKHelper.getInstance().isGroupsSyncedWithServer();
            boolean contactSynced = HXSDKHelper.getInstance().isContactsSyncedWithServer();

            // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
            if (groupSynced && contactSynced) {
                new Thread() {
                    @Override
                    public void run() {
                        HXSDKHelper.getInstance().notifyForRecevingEvents();
                    }
                }.start();
            } else {
                if (!groupSynced) {
                    asyncFetchGroupsFromServer();
                }

                if (!contactSynced) {
                    asyncFetchContactsFromServer();
                }

                if (!HXSDKHelper.getInstance().isBlackListSyncedWithServer()) {
                    asyncFetchBlackListFromServer();
                }
            }

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    chatHistoryFragment.errorItem.setVisibility(View.GONE);
                }

            });
        }

        @Override
        public void onDisconnected(final int error) {
            final String st1 = getResources().getString(R.string.can_not_connect_chat_server_connection);
            final String st2 = getResources().getString(R.string.the_current_network);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        showAccountRemovedDialog();
                    } else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
                    } else {
                        chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
                        if (NetUtils.hasNetwork(MainActivity.this))
                            chatHistoryFragment.errorText.setText(st1);
                        else
                            chatHistoryFragment.errorText.setText(st2);

                    }
                }

            });
        }
    }

    /**
     * MyGroupChangeListener
     */
    public class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

            boolean hasGroup = false;
            for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup)
                return;

            // 被邀请
            String st3 = getResources().getString(R.string.Invite_you_to_join_a_group_chat);
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(inviter + " " + st3));
            // 保存邀请消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(msg);

            runOnUiThread(new Runnable() {
                public void run() {
                    // 刷新ui
                    if (currentTabIndex == 0)
                        chatHistoryFragment.refresh();
                    if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            });

        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter, String reason) {

        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {

        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {

            // 提示用户被T了，demo省略此步骤
            // 刷新ui
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        if (currentTabIndex == 0)
                            chatHistoryFragment.refresh();
                        if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                            GroupsActivity.instance.onResume();
                        }
                    } catch (Exception e) {
                        EMLog.e(TAG, "refresh exception " + e.getMessage());
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {

            // 群被解散
            // 提示用户群被解散,demo省略
            // 刷新ui
            runOnUiThread(new Runnable() {
                public void run() {
                    if (currentTabIndex == 0)
                        chatHistoryFragment.refresh();
                    if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            });

        }

        @Override
        public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {

            // 用户申请加入群聊
            InviteMessage msg = new InviteMessage();
            msg.setFrom(applyer);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(groupName);
            msg.setReason(reason);
            Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
            msg.setStatus(InviteMessage.InviteMesageStatus.BEAPPLYED);
            notifyNewIviteMessage(msg);
        }

        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {

            String st4 = getResources().getString(R.string.Agreed_to_your_group_chat_application);
            // 加群申请被同意
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(accepter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(accepter + " " + st4));
            // 保存同意消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(msg);

            runOnUiThread(new Runnable() {
                public void run() {
                    // 刷新ui
                    if (currentTabIndex == 0)
                        chatHistoryFragment.refresh();
                    if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            });
        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
            // 加群申请被拒绝，demo未实现
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        EMChatManager.getInstance().unregisterEventListener(this);
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
        sdkHelper.popActivity(this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);

    }


    private AlertDialog.Builder conflictBuilder;
    private AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private BroadcastReceiver internalDebugReceiver;

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    /**
     * 内部测试代码，开发者请忽略
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHXSDKHelper.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // 重新显示登陆页面
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));

                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //getMenuInflater().inflate(R.menu.context_tab_contact, menu);
    }

    /*======================================*/


    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        // 未读数加1
        User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(Constant.NEW_FRIENDS_USERNAME);
        if (user.getUnreadMsgCount() == 0)
            user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
    }

    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);

        // 刷新bottom bar消息未读数
//        updateUnreadAddressLable();
        // 刷新好友页面ui
        if (currentTabIndex == 1)
            contactListFragment.refresh();
    }


    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        if (((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(Constant.NEW_FRIENDS_USERNAME) != null)
            unreadAddressCountTotal = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(Constant.NEW_FRIENDS_USERNAME)
                    .getUnreadMsgCount();
        return unreadAddressCountTotal;
    }


    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(true, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;


    /**
     * set head
     *
     * @param username
     * @return
     */
    User setUserHead(String username) {
        User user = new User();
        user.setUsername(username);
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
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
        return user;
    }


}

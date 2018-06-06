package com.happynetwork.vrestate.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.happynetwork.vrestate.R;

import java.util.ArrayList;

public class InviteScopeActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout toolbar;
    private View[] radioButtons;
    private LinearLayout selectFriendLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_invite_scope);
        setTitleName(getString(R.string.invite_scope));
        showToolBar();
        showBack();
        radioButtons = new View[3];
        initView();
    }

    public void initView(){
        selectFriendLayout = (LinearLayout) findViewById(R.id.select_friend_layout);
        ImageView open = (ImageView)findViewById(R.id.open);
        ImageView friend = (ImageView) findViewById(R.id.friend);
        ImageView selectFriend = (ImageView) findViewById(R.id.selectfriend);
        radioButtons[0] = open;
        radioButtons[1] = friend;
        radioButtons[2] = selectFriend;
        for(int i=0;i<3;i++){
            radioButtons[i].setBackgroundResource(R.mipmap.pic_invite_scope_default);
            radioButtons[i].setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.open:
                switchButton(0);
                break;
            case R.id.friend:
                switchButton(1);
                break;
            case R.id.selectfriend:
                switchButton(2);
                break;
            default:

                break;
        }
    }

    public void switchButton(int index){
        for(int i=0;i<3;i++){
            View btn = radioButtons[i];
            if(i==index){
                btn.setBackgroundResource(R.mipmap.pic_invite_scope_sel);
                if(i==2){
                    showList();
                }else{
                    hideList();
                }
            }else{
                btn.setBackgroundResource(R.mipmap.pic_invite_scope_default);
            }
        }
    }

    public void showList(){

        selectFriendLayout.setVisibility(View.VISIBLE);
//        ArrayList<Friend> friends = new ArrayList<Friend>();
//        ListView friendList = (ListView) findViewById(R.id.friend_list);
//        FriendAdapter friendAdapter = new FriendAdapter(this,friends);
//        friendList.setAdapter(friendAdapter);
    }
    public  void hideList(){
        selectFriendLayout.setVisibility(View.GONE);
    }
}

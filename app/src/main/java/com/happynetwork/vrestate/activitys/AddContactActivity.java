/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.happynetwork.vrestate.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.happynetwork.vrestate.BaseApplication;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.widget.DemoHXSDKHelper;
import com.happynetwork.vrestate.widget.HXSDKHelper;

public class AddContactActivity extends BaseActivity implements OnClickListener{
	private EditText editText;
	private LinearLayout searchedUserLayout;
	private TextView nameText,mTextView;
	private Button searchBtn;
	private ImageView avatar;
	private InputMethodManager inputMethodManager;
	private String toAddUsername;
	private ProgressDialog progressDialog;

	private RelativeLayout creategroup_layout,create_topic_layout;
	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		mTextView = (TextView) findViewById(R.id.add_list_friends);
		back = (ImageView) findViewById(R.id.back);
		back.setImageResource(R.mipmap.social_return);
		back.setOnClickListener(this);
		editText = (EditText) findViewById(R.id.edit_note);
		String strAdd = getResources().getString(R.string.add_neighbor);
		mTextView.setText(strAdd);
//		String strUserName = getResources().getString(R.string.user_name);
//		editText.setHint(strUserName);
		creategroup_layout = (RelativeLayout) findViewById(R.id.creategroup_layout);
		creategroup_layout.setOnClickListener(this);
		create_topic_layout = (RelativeLayout) findViewById(R.id.create_topic_layout);
		create_topic_layout.setOnClickListener(this);
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		searchedUserLayout.setOnClickListener(this);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
		searchBtn.setVisibility(View.GONE);
		avatar = (ImageView) findViewById(R.id.avatar);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { 
		    @Override 
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
		    { 
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) 
		        { 
//		            performSearch(); 
		            searchContact(editText);
		            return true;
		        } 
		        return false; 
		        }
		    });
	}
	
	
	/**
	 * 查找contact
	 * @param v
	 */
	public void searchContact(View v) {
		final String name = editText.getText().toString();
		String saveText = searchBtn.getText().toString();
		
		if (getString(R.string.button_search).equals(saveText)) {
			toAddUsername = name;
			if(TextUtils.isEmpty(name)) {
				String st = getResources().getString(R.string.Please_enter_a_username);
				startActivity(new Intent(this, AlertDialog.class).putExtra("msg", st));
				return;
			}
			
			// TODO 从服务器获取此contact,如果不存在提示不存在此用户
			
			//服务器存在此用户，显示此用户和添加按钮
			searchedUserLayout.setVisibility(View.VISIBLE);
			nameText.setText(toAddUsername);
			
		} 
	}	
	
	/**
	 *  添加contact
	 * @param view
	 */
	public void addContact(View view){
		if(BaseApplication.getInstance().getUserName().equals(nameText.getText().toString())){
			String str = getString(R.string.not_add_myself);
			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", str));
			return;
		}
		
		if(((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().containsKey(nameText.getText().toString())){
		    //提示已在好友列表中，无需添加
		    if(EMContactManager.getInstance().getBlackListUsernames().contains(nameText.getText().toString())){
		        startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
		        return;
		    }
			String strin = getString(R.string.This_user_is_already_your_friend);
			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", strin));
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
//		progressDialog.show();
		
		Intent intent = new Intent();
		intent.setClass(this, SendValidateInfoActivity.class);
		intent.putExtra("userId", toAddUsername);
		startActivity(intent);
//		new Thread(new Runnable() {
//			public void run() {
//				
//				try {
//					//demo写死了个reason，实际应该让用户手动填入
//					String s = getResources().getString(R.string.Add_a_friend);
//					EMContactManager.getInstance().addContact(toAddUsername, s);
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							String s1 = getResources().getString(R.string.send_successful);
//							Toast.makeText(getApplicationContext(), s1, 1).show();
//						}
//					});
//				} catch (final Exception e) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
//							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), 1).show();
//						}
//					});
//				}
//			}
//		}).start();
	}
	
	public void back(View v) {
//		finish();
	}


    @Override
    public void onClick(View view) {
        
        int id = view.getId();
        switch (id) {
        case R.id.ll_user:
//            Intent intent = new Intent();
//            intent.setClass(this, ChatActivity.class);
//            intent.putExtra("userId", nameText.getText().toString());
//            startActivity(intent);
            break;
        case R.id.creategroup_layout:
            Intent intent_addgroup = new Intent();
            intent_addgroup.setClass(this, BuildGroupActivity.class);
            startActivity(intent_addgroup);
            break;
        case R.id.back:
            this.finish();
            break;
        case R.id.create_topic_layout:
            Intent intent_create_topic = new Intent();
            intent_create_topic.setClass(this, BuildTopicActivity.class);
            startActivity(intent_create_topic);
            break;

        default:
            break;
        }
    }
}

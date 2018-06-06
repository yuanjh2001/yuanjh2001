package com.happynetwork.vrestate.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.happynetwork.vrestate.R;

/**
 * 
 * @author Tom.yuan
 * 发送验证信息
 * 2016-9-6
 * 上午11:00:23
 */
public class SendValidateInfoActivity extends Activity implements OnClickListener{
    private TextView title_name,send;
    private EditText validateInfo;
    private ImageView back,more;
    private String userName;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_validate_info);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText(R.string.friends_validate);
        more = (ImageView) findViewById(R.id.more);
        more.setVisibility(View.GONE);
        
        validateInfo = (EditText) findViewById(R.id.send_validate_info);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        userName = getIntent().getStringExtra("userId");
        progressDialog = new ProgressDialog(SendValidateInfoActivity.this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        send = (TextView) findViewById(R.id.cancel);
        send.setVisibility(View.VISIBLE);
        send.setText(R.string.button_send);
        send.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.cancel:
            
            new Thread(new Runnable() {
                public void run() {
                    Looper.prepare();
                    try {
                        
                        progressDialog.show();
                        //demo写死了个reason，实际应该让用户手动填入
                        String s = validateInfo.getText().toString();
                        EMContactManager.getInstance().addContact(userName, s);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                String s1 = getResources().getString(R.string.send_successful);
                                Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_SHORT).show();
                                
                            }
                        });
                        SendValidateInfoActivity.this.finish();
                    } catch (final Exception e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                                Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
                                
                            }
                        });
                    }
                    
                }
            }).start();
            break;
        case R.id.back:
            this.finish();
            break;
        default:
            break;
        }
    }
}

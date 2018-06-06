package com.happynetwork.vrestate.activitys;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.mywidgets.DLVideoView;
import com.umeng.analytics.MobclickAgent;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {
    private DLVideoView videoView;
    private Button toLoginBtn,toRegBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        videoView = (DLVideoView) findViewById(R.id.video_view_id);
        toLoginBtn = (Button)findViewById(R.id.tologin_bt_id);
        toRegBtn = (Button)findViewById(R.id.toreg_bt_id);
        toRegBtn.setOnClickListener(this);
        toLoginBtn.setOnClickListener(this);
        startVideo();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        startVideo();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tologin_bt_id:
                openActivity(LoginActivity.class);
                finish();
                break;
            case R.id.toreg_bt_id:
                openActivity(RegActivity.class);
                finish();
                break;
        }
    }

    private void startVideo(){
        if(videoView != null){
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.guide));
            videoView.start();
        }
    }

    @Override
    protected void onResume() {
        startVideo();
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        videoView.pause();
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}

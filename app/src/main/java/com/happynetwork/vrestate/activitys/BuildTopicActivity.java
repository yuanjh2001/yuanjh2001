package com.happynetwork.vrestate.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.happynetwork.vrestate.R;


/**
 * 
 * @author Tom.yuan
 * 新建话题
 * 2016-8-16
 * 下午1:18:48
 */
public class BuildTopicActivity extends Activity implements OnClickListener{
    private TextView titleName;
    private ImageView more,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.build_topic);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText(R.string.add_topic);
        more = (ImageView) findViewById(R.id.more);
        more.setVisibility(View.GONE);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
        case R.id.back:
            this.finish();
            break;

        default:
            break;
        }
    }

}

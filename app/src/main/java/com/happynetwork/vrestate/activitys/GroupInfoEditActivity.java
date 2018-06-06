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
 * @author Tom.yuan 编辑群信息 2016-8-18 下午5:20:11
 */
public class GroupInfoEditActivity extends Activity implements OnClickListener{
    private TextView titleName,cancel;
    private ImageView back,more;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupinfo_edit);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText(R.string.group_info);
        back = (ImageView) findViewById(R.id.back);
        more = (ImageView) findViewById(R.id.more);
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setVisibility(View.VISIBLE);
        cancel.setText(R.string.save);
        more.setVisibility(View.GONE);
        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
        case R.id.cancel:
            
            break;
        case R.id.back:
            this.finish();
            break;
        default:
            break;
        }
    }
}

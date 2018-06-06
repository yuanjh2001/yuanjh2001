package com.happynetwork.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.R;

public class UpdateAkpDialog extends Dialog {

	public UpdateAkpDialog(Context context) {
		super(context);
	}

	public UpdateAkpDialog(Context context, int theme) {
		super(context, theme);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xf_common_updateakpdialog);
        Button buttonNo = (Button) findViewById(R.id.install_id);
		buttonNo.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
                dismiss();
                dotry();
			}
		});
        buttonNo.requestFocus();
        Button buttonExit = (Button) findViewById(R.id.close_id);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                doCancel();
            }
        });
    }

    public void dotry(){
        LogUtils.i("开始更新...");
    }

    public void doCancel(){

    }

}

package com.happynetwork.vrestate.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.happynetwork.vrestate.BaseApplication;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.adapters.FocusAdapter;
import com.happynetwork.vrestate.beans.FocusModel;
import com.happynetwork.vrestate.utils.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author Tom.yuan 群成员界面 2016-8-23 上午11:40:24
 */
public class GroupMemberActivity extends Activity implements OnClickListener,OnItemClickListener{
    private static final String TAG = "GroupMemberActivity";
    private TextView titleName;
    private ImageView back,more,search;
    private PullToRefreshListView mPullRefreshListView;
    FocusAdapter la;
    private ArrayList<FocusModel> mList = new ArrayList<FocusModel>();
    private ArrayList<FocusModel> tempList = new ArrayList<FocusModel>();
    ImageLoader imageLoader = null;
    DisplayImageOptions options;  
    private EditText search_group_member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member);
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText(R.string.group_number);
        back = (ImageView) findViewById(R.id.back);
        more = (ImageView) findViewById(R.id.more);
        search = (ImageView) findViewById(R.id.search);
        search_group_member = (EditText) findViewById(R.id.search_group_member);
        search.setOnClickListener(this);
        back.setOnClickListener(this);
        more.setVisibility(View.GONE);
        imageLoader = ImageLoader.getInstance();
        options = BaseApplication.getInstance().getImgOps();
        initView();
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
        case R.id.back:
            this.finish();
            break;
        case R.id.search:
            handler.post(runnable);
            break;
        default:
            break;
        }
    }
    
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String searchCon = search_group_member.getText().toString();
            if(StringUtil.isEmpty(searchCon)){
                mList = initData();
                la.notifyDataSetChanged();
            }else{
                getDataList(searchCon);
            }
            
        }

        
    };
    
    private void getDataList(String searchCon) {
        tempList.clear();
        tempList.addAll(mList);
        int len = tempList.size();
        Log.i(TAG, "mList=="+mList);
        mList.clear();
        for(int i=0;i<len;i++){
            Log.i(TAG, "tempList=="+tempList);
            FocusModel focusModel = tempList.get(i);
            String name = focusModel.name;
            Log.i(TAG, "name==="+name);
            boolean isCon = name.contains(searchCon);
            if(isCon){
                mList.add(focusModel);
            }
        }
        Log.i(TAG, "mList.size=="+mList.size());
        la.notifyDataSetChanged();
    }
    
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
        
    };
    
    private void initView() {
        mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.common_list);
        mPullRefreshListView.setMode(Mode.BOTH);
        ListView actualListView = mPullRefreshListView.getRefreshableView();
        mList = initData();
        la = new FocusAdapter(this,mList,imageLoader,options);
        actualListView.setAdapter(la);
        actualListView.setDividerHeight(0);
        actualListView.setOnItemClickListener(this);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(GroupMemberActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                        new GetDataTask().execute();
                    }
                });
        
    }
    
    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<FocusModel>> {
        // 后台处理部分
        @Override
        protected ArrayList<FocusModel> doInBackground(Void... params) {
            // Simulates a background job.
            ArrayList<FocusModel> m = null;
            try {
                Thread.sleep(1000);
                m = initData();
            } catch (InterruptedException e) {
            }
            return m;
        }

        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
        @Override
        protected void onPostExecute(ArrayList<FocusModel> result) {
            //在头部增加新添内容
            //通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
            la.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();
            super.onPostExecute(result);//这句是必有的，AsyncTask规定的格式
        }
    }
    
    private ArrayList<FocusModel> initData() {
        mList.clear();
        for(int i=0;i<10;i++){
            FocusModel focusModel = new FocusModel();
            focusModel.headPicUrl = "http://www.onegreen.net/QQ/UploadFiles/201307/2013070307215828.jpg";
            if(i==3){
                focusModel.name = "张良";
            }else if(i==5){
                focusModel.name = "李四";
            }else if(i==6){
                focusModel.name = "李三";
            }else{
                focusModel.name = "李良";
            }
            
            focusModel.range = "1.5";
            focusModel.focusStatus = "3";
            mList.add(focusModel);
            
        }
        return mList;
    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        
    }
}

package com.happynetwork.vrestate.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.utils.BitmapUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;

/**
 * Created by Tom.yuan on 2016/12/27.
 * Edit by wangfu   on 2017/1/17
 * 发现
 */
public class Find_Fragment extends BaseFragment implements View.OnClickListener, AMap.OnMarkerClickListener, LocationSource, AMapLocationListener, AMap.OnMapClickListener, AMap.OnCameraChangeListener {
    private final static int MAX_TAG = 7;
    private int mCurSelect = 0;
    private View mTags[] = new View[MAX_TAG];
    private View lineView[] = new View[MAX_TAG];
    private View tabName[] = new View[MAX_TAG];
    private MapView mapView;
    private AMap aMap;
    private ArrayList<LatLng> latLngs;
    private MarkerOptions markerOptions;
    private View inviteViewTag, requestViewTag;

    private LayoutInflater layoutInflater;
    ArrayList<MarkerOptions> markerOptionlst = null;
    private ArrayList<Marker> mList = null;
    PopupWindow window = null;
    private Activity mActivity;
    AlertDialog.Builder builder = null;
    AlertDialog alertDialog = null;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        layoutInflater =  LayoutInflater.from(mContext);
        rootView =layoutInflater.inflate(R.layout.fragment_find, null);
        markerOptionlst = new ArrayList<MarkerOptions>();
        mList=new ArrayList<Marker>();
        initView();
        latLngs = new ArrayList<LatLng>();
        initLatLng();
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initMap();
        return rootView;
    }

    /**
     * 初始化经纬度
     */
    public void initLatLng() {
        latLngs.add(new LatLng(39.9123, 116.4588));
        latLngs.add(new LatLng(39.9121, 116.4725));
        latLngs.add(new LatLng(39.9206, 116.5044));
        latLngs.add(new LatLng(39.9062, 116.4801));
        latLngs.add(new LatLng(39.9044, 116.5095));
        latLngs.add(new LatLng(39.9026, 116.5020));
        latLngs.add(new LatLng(39.9054, 116.4520));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void initMap() {

        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.clear();
        setUpMap();
        registerLisenter();
    }

    public void setUpMap() {
        int len = latLngs.size();

//        for(int i=0;i<len;i++){
        View inviteViewTag = layoutInflater.inflate(R.layout.invite_tag, null);
        View requestViewTag = layoutInflater.inflate(R.layout.request_tag, null);
        inviteViewTag.setDrawingCacheEnabled(true);
        Bitmap bitmapInvite = inviteViewTag.getDrawingCache();
        MarkerOptions markerOptions = new MarkerOptions().anchor(0.5f, 0.5f)
                .visible(true).position(latLngs.get(0)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.pic_find_map_shadow_invite))
                .perspective(true)
                .draggable(false);
        markerOptions.setFlat(true);
        markerOptionlst.add(markerOptions);

        MarkerOptions markerOptions1 = new MarkerOptions().anchor(0.5f, 0.5f)
                .visible(true).position(latLngs.get(1)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.pic_find_map_shadow_invite))
                .perspective(true)
                .draggable(false);
        markerOptions1.setFlat(true);
        markerOptionlst.add(markerOptions1);
        MarkerOptions markerOptions2 = new MarkerOptions().anchor(0.5f, 0.5f)
                .visible(true).position(latLngs.get(2)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.pic_find_map_shadow_invite))
                .perspective(true)
                .draggable(false);

        markerOptions2.setFlat(true);
        markerOptionlst.add(markerOptions2);

        MarkerOptions markerOptions3 = new MarkerOptions().anchor(0.5f, 0.5f)
                .visible(true).position(latLngs.get(2)).icon(BitmapDescriptorFactory.fromBitmap(bitmapInvite))
                .perspective(true)
                .draggable(false);

        markerOptions3.setFlat(true);
        markerOptionlst.add(markerOptions3);

        mList = aMap.addMarkers(markerOptionlst,true);
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setMyLocationEnabled(true);
//        }

    }

    /**
     * 注册地图的监听器
     */
    public void registerLisenter() {
        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
    }

    public void initView() {

        mTags[0] = rootView.findViewById(R.id.all_layout);
        mTags[1] = rootView.findViewById(R.id.food_layout);
        mTags[2] = rootView.findViewById(R.id.sport_layout);
        mTags[3] = rootView.findViewById(R.id.film_layout);
        mTags[4] = rootView.findViewById(R.id.game_layout);
        mTags[5] = rootView.findViewById(R.id.drink_layout);
        mTags[6] = rootView.findViewById(R.id.nominate_layout);
        for (int i = 0; i < MAX_TAG; i++) {
            mTags[i].setOnClickListener(this);
        }

        lineView[0] = rootView.findViewById(R.id.line_all);
        lineView[1] = rootView.findViewById(R.id.line_food);
        lineView[2] = rootView.findViewById(R.id.line_sport);
        lineView[3] = rootView.findViewById(R.id.line_film);
        lineView[4] = rootView.findViewById(R.id.line_game);
        lineView[5] = rootView.findViewById(R.id.line_drink);
        lineView[6] = rootView.findViewById(R.id.line_groom);
        tabName[0] = rootView.findViewById(R.id.text_all);
        tabName[1] = rootView.findViewById(R.id.text_food);
        tabName[2] = rootView.findViewById(R.id.text_sport);
        tabName[3] = rootView.findViewById(R.id.text_film);
        tabName[4] = rootView.findViewById(R.id.text_game);
        tabName[5] = rootView.findViewById(R.id.text_drink);
        tabName[6] = rootView.findViewById(R.id.text_groom);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.all_layout:
                if (mCurSelect == 0) {
                    return;
                }
                switchTab(0);
                break;
            case R.id.food_layout:
                if (mCurSelect == 1) {
                    return;
                }
                switchTab(1);
                break;
            case R.id.sport_layout:
                if (mCurSelect == 2) {
                    return;
                }
                switchTab(2);
                break;
            case R.id.film_layout:
                if (mCurSelect == 3) {
                    return;
                }
                switchTab(3);
                break;
            case R.id.game_layout:
                if (mCurSelect == 4) {
                    return;
                }
                switchTab(4);
                break;
            case R.id.drink_layout:
                if (mCurSelect == 5) {
                    return;
                }
                switchTab(5);
                break;
            case R.id.nominate_layout:
                if (mCurSelect == 6) {
                    return;
                }
                switchTab(6);
                break;
            default:
                break;

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        mapView.onResume();
        MobclickAgent.onPageStart("Find_Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        MobclickAgent.onPageEnd("Find_Fragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void switchTab(int index) {
        if (mCurSelect < 0 || mCurSelect >= MAX_TAG)
            return;

        mTags[mCurSelect].setSelected(true);
        mCurSelect = index;
        for (int i = 0; i < MAX_TAG; i++) {
            if (i == mCurSelect) {
                lineView[i].setVisibility(View.VISIBLE);
                TextView tv = (TextView) tabName[i];
                tv.setTextColor(getResources().getColor(R.color.find_titlebar_font_color));
            } else {
                lineView[i].setVisibility(View.INVISIBLE);
                TextView tv = (TextView) tabName[i];
                tv.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int len = markerOptionlst.size();
        for(int i=0;i<len;i++) {
           boolean b =  marker.equals(mList.get(i));
            if(b){
                popWindowInviteResume();
            }
        }
        return false;
    }

    public void popWindowInviteResume(){

        Bitmap bt = BitmapUtil.zoomImage(BitmapFactory.decodeResource(mActivity.getResources(),R.mipmap.bg_find_popup_invite),280,247);
        builder = new AlertDialog.Builder(mActivity);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.invite_resume, null);
        RelativeLayout la = (RelativeLayout)view.findViewById(R.id.invite_resume);
        la.setBackground(new BitmapDrawable(bt));
        builder.setView(view);
        alertDialog =  builder.create();
        alertDialog.show();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}

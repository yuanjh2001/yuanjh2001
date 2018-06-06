package com.happynetwork.vrestate.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.happynetwork.common.dialog.ClipPicLayout;
import com.happynetwork.common.dialog.ViewPhotoGroupLayout;
import com.happynetwork.common.dialog.ViewPhotoLayout;
import com.happynetwork.common.events.ClipPicEvent;
import com.happynetwork.common.events.DoUploadImgListener;
import com.happynetwork.common.events.ViewPhotoClickEvent;
import com.happynetwork.common.events.ViewPhotoGroupClickEvent;
import com.happynetwork.common.utils.AndroidBug5497Workaround;
import com.happynetwork.common.utils.LogUtils;
import com.happynetwork.common.utils.ToastUtils;
import com.happynetwork.common.utils.Tools;
import com.happynetwork.common.vo.ImageBean;
import com.happynetwork.common.vo.LocalImageBean;
import com.happynetwork.vrestate.BaseApplication;
import com.happynetwork.vrestate.R;
import com.happynetwork.vrestate.adapters.ArrayWheelAdapter;
import com.happynetwork.vrestate.adapters.NumericWheelAdapter;
import com.happynetwork.vrestate.listeners.GetAreaCallBack;
import com.happynetwork.vrestate.listeners.GetIndustryCallBack;
import com.happynetwork.vrestate.listeners.OnWheelChangedListener;
import com.happynetwork.vrestate.listeners.SaveUserInfoCallBack;
import com.happynetwork.vrestate.listeners.UploadFileCallBack;
import com.happynetwork.vrestate.localdata.beans.UserInfo;
import com.happynetwork.vrestate.localdata.beans.responsevos.CityVo;
import com.happynetwork.vrestate.localdata.beans.responsevos.CountyVo;
import com.happynetwork.vrestate.localdata.beans.responsevos.Industry;
import com.happynetwork.vrestate.localdata.beans.responsevos.ProvinceVo;
import com.happynetwork.vrestate.localdata.managers.DictionaryServiceManager;
import com.happynetwork.vrestate.localdata.managers.UserServiceManager;
import com.happynetwork.vrestate.mywidgets.ImageViewMagnifyLoadingDialog;
import com.happynetwork.vrestate.mywidgets.MyRoundImageView;
import com.happynetwork.vrestate.mywidgets.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 个人主页
 */
public class UserMainActivity extends BaseActivity implements View.OnClickListener {
    String months[];
    String xingzuo[][];
    String xuelis[];
    String xinzis[];
    String sex[][];
    private ScrollView scrollView;
    private FrameLayout menu_layout;
    private LinearLayout date_ly, sex_ly, img_ly, xinzi_ly, xueli_ly, area_ly, hangye_ly;
    //弹出菜单操作按钮
    private TextView cancel_bt, submit_bt, cancel_sex_bt, submit_sex_bt, picture_tv, camera_tv, cancel_tv_img, cancel_xinzi_bt, submit_xinzi_bt,
            cancel_xueli_bt, submit_xueli_bt, cancel_area_bt, submit_area_bt, cancel_hangye_bt, submit_hangye_bt;
    //弹出滚动控件
    private WheelView month, year, day, xinzi, xueli, province, city, county, hangye;
    private TextView birthday_et, xingzuo_et, sex_et, xinzi_et, xueli_et, area_et, hangye_et;
    private RadioButton boy_rbt, gril_rbt;
    private RelativeLayout userImgLy;
    private MyRoundImageView userHeadImg;
    private UserInfo userInfo;
    private ClipPicLayout clipPicLayout;
    private ViewPhotoGroupLayout viewPhotoGroupLayout;
    private ViewPhotoLayout viewPhotoLayout;
    private String citys[];
    private String[] provinces;
    private String countys[];
    private List<ProvinceVo> provinceVos;
    private List<CityVo> cityVos;
    private List<CityVo> allcityVos;
    private List<CountyVo> allcountyVos;
    private List<Industry> industrys;
    private String[] industry_strs;
    private ClipPicEvent clipPicEvent;
    private MyMessageReceiver mMessageReceiver;
    private String brithdayTime;
    private String areaId = "0";
    private EditText nkname_et, realname_et, qianming_et, shengao_et, tizhong_et, gongsi_et, zhiwu_et, xuexiao_et, zye_et, aihao_et, jianjie_et;
    private String oldUserCode, newUserCode;
    private int yearCount = 60;
    private Map<String, Integer> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentViewBelowTitleBar(R.layout.activity_user_main);
        setTitleName("个人信息");
        showBack();
        showSave();
        userInfo = UserServiceManager.getInstance().getUserInfo(BaseApplication.getInstance());
        oldUserCode = Tools.md5(new Gson().toJson(userInfo.getValus()));
        LogUtils.i(oldUserCode);
        scrollView = (ScrollView) findViewById(R.id.scrollView_id);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        menu_layout = (FrameLayout) findViewById(R.id.menu_layout_id);
        date_ly = (LinearLayout) findViewById(R.id.date_ly_id);
        cancel_bt = (TextView) findViewById(R.id.tv_cancel);
        submit_bt = (TextView) findViewById(R.id.tv_submit);
        cancel_bt.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        birthday_et = (TextView) findViewById(R.id.birthday_et_id);
        birthday_et.setOnClickListener(this);
        xingzuo_et = (TextView) findViewById(R.id.xingzuo_et_id);
        sex_ly = (LinearLayout) findViewById(R.id.sex_ly_id);
        sex_et = (TextView) findViewById(R.id.sex_et_id);
        sex_et.setOnClickListener(this);
        cancel_sex_bt = (TextView) findViewById(R.id.tv_cancel_sex);
        submit_sex_bt = (TextView) findViewById(R.id.tv_submit_sex);
        cancel_sex_bt.setOnClickListener(this);
        submit_sex_bt.setOnClickListener(this);
        boy_rbt = (RadioButton) findViewById(R.id.boy_bt);
        gril_rbt = (RadioButton) findViewById(R.id.gril_bt);
        userHeadImg = (MyRoundImageView) findViewById(R.id.user_portrait);
        userImgLy = (RelativeLayout) findViewById(R.id.userimg_ly_id);
        userImgLy.setOnClickListener(this);
        userHeadImg.setOnClickListener(this);
        img_ly = (LinearLayout) findViewById(R.id.img_ly_id);
        menu_layout.setOnClickListener(this);
        picture_tv = (TextView) findViewById(R.id.picture_tv_id);
        camera_tv = (TextView) findViewById(R.id.camera_tv_id);
        cancel_tv_img = (TextView) findViewById(R.id.cancel_tv_img);
        picture_tv.setOnClickListener(this);
        camera_tv.setOnClickListener(this);
        cancel_tv_img.setOnClickListener(this);
        xueli_ly = (LinearLayout) findViewById(R.id.xueli_ly_id);
        xinzi_ly = (LinearLayout) findViewById(R.id.xinzi_ly_id);
        xinzi_et = (TextView) findViewById(R.id.xinzi_et_id);
        xinzi_et.setOnClickListener(this);
        xueli_et = (TextView) findViewById(R.id.xueli_et_id);
        xueli_et.setOnClickListener(this);
        cancel_xinzi_bt = (TextView) findViewById(R.id.tv_cancel_xinzi);
        submit_xinzi_bt = (TextView) findViewById(R.id.tv_submit_xinzi);
        cancel_xinzi_bt.setOnClickListener(this);
        submit_xinzi_bt.setOnClickListener(this);
        cancel_xueli_bt = (TextView) findViewById(R.id.tv_cancel_xueli);
        submit_xueli_bt = (TextView) findViewById(R.id.tv_submit_xueli);
        cancel_xueli_bt.setOnClickListener(this);
        submit_xueli_bt.setOnClickListener(this);
        area_ly = (LinearLayout) findViewById(R.id.area_ly_id);
        area_et = (TextView) findViewById(R.id.area_et_id);
        area_et.setOnClickListener(this);
        cancel_area_bt = (TextView) findViewById(R.id.tv_cancel_area);
        submit_area_bt = (TextView) findViewById(R.id.tv_submit_area);
        cancel_area_bt.setOnClickListener(this);
        submit_area_bt.setOnClickListener(this);
        hangye_ly = (LinearLayout) findViewById(R.id.hangye_ly_id);
        hangye_et = (TextView) findViewById(R.id.hangye_et_id);
        hangye_et.setOnClickListener(this);
        submit_hangye_bt = (TextView) findViewById(R.id.tv_submit_hangye);
        cancel_hangye_bt = (TextView) findViewById(R.id.tv_cancel_hangye);
        submit_hangye_bt.setOnClickListener(this);
        cancel_hangye_bt.setOnClickListener(this);
        date_ly.setOnClickListener(this);
        sex_ly.setOnClickListener(this);
        img_ly.setOnClickListener(this);
        xinzi_ly.setOnClickListener(this);
        xueli_ly.setOnClickListener(this);
        area_ly.setOnClickListener(this);
        hangye_ly.setOnClickListener(this);

        nkname_et = (EditText) findViewById(R.id.nkname_et_id);
        realname_et = (EditText) findViewById(R.id.realname_et_id);
        qianming_et = (EditText) findViewById(R.id.qianming_et_id);
        shengao_et = (EditText) findViewById(R.id.shengao_et_id);
        tizhong_et = (EditText) findViewById(R.id.tizhong_et_id);
        gongsi_et = (EditText) findViewById(R.id.gongsi_et_id);
        zhiwu_et = (EditText) findViewById(R.id.zhiwu_et_id);
        xuexiao_et = (EditText) findViewById(R.id.xuexiao_et_id);
        zye_et = (EditText) findViewById(R.id.zye_et_id);
        aihao_et = (EditText) findViewById(R.id.aihao_et_id);
        jianjie_et = (EditText) findViewById(R.id.jianjie_et_id);


        AndroidBug5497Workaround.assistActivity(this);
        initArr();
        initDateWheel(-1, -1, -1);
        initXueLiWheel();
        initXinZiWheel(3);
        initDatas();
        initAreaNet();
        regist();
    }

    private void initArr() {
        sex = new String[][]{{"1", getString(R.string.sex_boy)}, {"2", getString(R.string.sex_gril)}};
        months = new String[]{getString(R.string.user_main_month1), getString(R.string.user_main_month2), getString(R.string.user_main_month3), getString(R.string.user_main_month4),
                getString(R.string.user_main_month5), getString(R.string.user_main_month6), getString(R.string.user_main_month7), getString(R.string.user_main_month8),
                getString(R.string.user_main_month9), getString(R.string.user_main_month10), getString(R.string.user_main_month11), getString(R.string.user_main_month12)};

        xingzuo = new String[][]{{getString(R.string.user_main_start_aries), "03", "21", "04", "20"}, {getString(R.string.user_main_start_taurus), "04", "21", "05", "21"},
                {getString(R.string.user_main_start_gemini), "05", "22", "06", "21"}, {getString(R.string.user_main_start_cancer), "06", "22", "07", "22"}
                , {getString(R.string.user_main_start_lion), "07", "23", "08", "23"}, {getString(R.string.user_main_start_virgo), "08", "24", "09", "23"},
                {getString(R.string.user_main_start_libra), "09", "24", "10", "23"}, {getString(R.string.user_main_start_scorpio), "10", "24", "11", "22"}
                , {getString(R.string.user_main_start_sagittarius), "11", "23", "12", "21"}, {getString(R.string.user_main_start_capricornus), "12", "22", "01", "20"},
                {getString(R.string.user_main_start_aquarius), "01", "21", "02", "19"}, {getString(R.string.user_main_start_pisces), "02", "20", "03", "20"}};
        xuelis = new String[]{getString(R.string.user_main_followinghighschool), getString(R.string.user_main_highschool), getString(R.string.user_main_juniorcollege),
                getString(R.string.user_main_undergraduate), getString(R.string.user_main_master),
                getString(R.string.user_main_doctor), getString(R.string.user_main_postdoctoral), getString(R.string.user_main_other)};
        xinzis = new String[]{getString(R.string.user_main_5000below), getString(R.string.user_main_8000below), getString(R.string.user_main_12000below),
                getString(R.string.user_main_20000below), getString(R.string.user_main_30000below),
                getString(R.string.user_main_50000below), getString(R.string.user_main_50000above)};
        Calendar calendar = Calendar.getInstance();
        yearCount = calendar.get(Calendar.YEAR) - 1970;
    }

    private void initAreaNet() {
        DictionaryServiceManager.getInstance().setContext(UserMainActivity.this).setShowLoading(true).getArea(new GetAreaCallBack() {
            @Override
            public void suc(List<ProvinceVo> ps) {
                provinceVos = ps;
                allcityVos = new ArrayList<CityVo>();
                allcountyVos = new ArrayList<CountyVo>();
                initIndustryNet();
                initAreaWheel();
                if(!userInfo.getAreaId().equals("")){
                    getCurrArea();
                    initAreaWheel();
                }
            }

            @Override
            public void fail(String code) {
                initIndustryNet();
            }
        });
    }

    private void initIndustryNet() {
        DictionaryServiceManager.getInstance().setContext(UserMainActivity.this).setShowLoading(true).getIndustry(new GetIndustryCallBack() {
            @Override
            public void suc(List<Industry> is) {
                industrys = is;
                if (industrys == null || industrys.isEmpty()) {
                    return;
                } else {
                    initIndustryWheel();
                }
            }

            @Override
            public void fail(String code) {

            }
        });
    }

    private void initDatas() {
        if (userInfo.getUserSex().equals(sex[1][0])) {
            sex_et.setText(sex[1][1]);
        } else if (userInfo.getUserSex().equals(sex[0][0])) {
            sex_et.setText(sex[0][1]);
        }
        if (!userInfo.getUserPic().equals("")) {
            BaseApplication.getInstance().getImLoader().displayImage(userInfo.getUserPic(), userHeadImg, BaseApplication.getInstance().getImgOps());
        }
        if (!userInfo.getUserNick().equals("")) {
            nkname_et.setText(userInfo.getUserNick());
        }
        if (!userInfo.getUserName().equals("")) {
            realname_et.setText(userInfo.getUserName());
        }
        if (!userInfo.getUserSign().equals("")) {
            qianming_et.setText(userInfo.getUserSign());
        }
        if (!userInfo.getUserStar().equals("")) {
            xingzuo_et.setText(userInfo.getUserStar());
        }
        if (!userInfo.getUserHeight().equals("")) {
            shengao_et.setText(userInfo.getUserHeight());
        }
        if (!userInfo.getUserWeight().equals("")) {
            tizhong_et.setText(userInfo.getUserWeight());
        }
        if (!userInfo.getUserIndustry().equals("")) {
            hangye_et.setText(userInfo.getUserIndustry());
        }
        if (!userInfo.getUserCompany().equals("")) {
            gongsi_et.setText(userInfo.getUserCompany());
        }
        if (!userInfo.getUserJob().equals("")) {
            zhiwu_et.setText(userInfo.getUserJob());
        }
        if (!userInfo.getUserSalary().equals("")) {
            xinzi_et.setText(userInfo.getUserSalary());
            for (int i = 0; i < xinzis.length; i++) {
                if (xinzis[i].equals(userInfo.getUserSalary())) {
                    initXinZiWheel(i);
                    break;
                }
            }
        }
        if (!userInfo.getUserSchool().equals("")) {
            xuexiao_et.setText(userInfo.getUserSchool());
        }
        if (!userInfo.getUserSpecialty().equals("")) {
            zye_et.setText(userInfo.getUserSpecialty());
        }
        if (!userInfo.getUserEdu().equals("")) {
            xueli_et.setText(userInfo.getUserEdu());
        }
        if (!userInfo.getUserHobby().equals("")) {
            aihao_et.setText(userInfo.getUserHobby());
        }
        if (!userInfo.getUserIntro().equals("")) {
            jianjie_et.setText(userInfo.getUserIntro());
        }
        if (!userInfo.getUserBrithday().equals("")) {
            brithdayTime = userInfo.getUserBrithday();
            String temp[] = userInfo.getUserBrithday().split("-");
            int currYear = Integer.parseInt(temp[0]);
            int currMonth = Integer.parseInt(temp[1]) - 1;
            int currDay = Integer.parseInt(temp[2]) - 1;
            birthday_et.setText(currYear + getString(R.string.user_main_format_year) + months[currMonth] + (currDay + 1) + getString(R.string.user_main_format_day));
            Calendar calendar = Calendar.getInstance();
            currYear = yearCount - (calendar.get(Calendar.YEAR) - currYear);
            initDateWheel(currDay, currMonth, currYear);
            setXingzuo(currMonth + 1, currDay + 1);
        }
        if(!userInfo.getAreaId().equals("0")){
            areaId = userInfo.getAreaId();
        }
    }

    private void hideMenu() {
        menu_layout.setVisibility(View.GONE);
        date_ly.setVisibility(View.GONE);
        sex_ly.setVisibility(View.GONE);
        img_ly.setVisibility(View.GONE);
        xueli_ly.setVisibility(View.GONE);
        xinzi_ly.setVisibility(View.GONE);
        area_ly.setVisibility(View.GONE);
        hangye_ly.setVisibility(View.GONE);
    }

    private void showDateMenu() {
        menu_layout.setVisibility(View.VISIBLE);
        date_ly.setVisibility(View.VISIBLE);
    }

    private void showSexMenu() {
        menu_layout.setVisibility(View.VISIBLE);
        sex_ly.setVisibility(View.VISIBLE);
    }

    private void showImgMenu() {
        menu_layout.setVisibility(View.VISIBLE);
        img_ly.setVisibility(View.VISIBLE);
    }

    private void showXinZiMenu() {
        menu_layout.setVisibility(View.VISIBLE);
        xinzi_ly.setVisibility(View.VISIBLE);
    }

    private void showXueLiMenu() {
        menu_layout.setVisibility(View.VISIBLE);
        xueli_ly.setVisibility(View.VISIBLE);
    }

    private void showAreaMenu() {
        menu_layout.setVisibility(View.VISIBLE);
        area_ly.setVisibility(View.VISIBLE);
    }

    private void showHangyeMenu() {
        menu_layout.setVisibility(View.VISIBLE);
        hangye_ly.setVisibility(View.VISIBLE);
    }

    private void initDateWheel(int currDay, int currMonth, int currYear) {
        Calendar calendar = Calendar.getInstance();
        month = (WheelView) findViewById(R.id.month);
        year = (WheelView) findViewById(R.id.year);
        day = (WheelView) findViewById(R.id.day);
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day);
            }
        };

        // month
        month.setViewAdapter(new DateArrayAdapter(this, months, calendar.get(Calendar.MONTH)));
        //month.setCurrentItem(curMonth);
        currMonth = (currMonth == -1) ? 6 : currMonth;
        month.setCurrentItem(currMonth);
        month.addChangingListener(listener);

        // year
        int curYear = calendar.get(Calendar.YEAR);
        year.setViewAdapter(new DateNumericAdapter(this, curYear - yearCount, curYear, yearCount, "%s" + getString(R.string.user_main_format_year)));
        currYear = (currYear == -1) ? yearCount % 2 : currYear;
        year.setCurrentItem(currYear);
        year.addChangingListener(listener);

        //day
        updateDays(year, month, day);
        //day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        currDay = (currDay == -1) ? 10 : currDay;
        day.setCurrentItem(currDay);
    }

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1, "%s" + getString(R.string.user_main_format_day)));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current, String format) {
            super(context, minValue, maxValue, format);
            this.currentValue = current;
            setTextSize(22);
            setTextColor(getColor(R.color.user_main_item_txt));
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTextSize(22);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF3F51B5);
                view.setTextSize(22);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(22);
            setTextColor(getColor(R.color.user_main_item_txt));
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTextSize(22);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF3F51B5);
                view.setTextSize(22);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    private void initXueLiWheel() {
        xueli = (WheelView) findViewById(R.id.xueli_wheel);
        xueli.setViewAdapter(new DateArrayAdapter(this, xuelis, 2));
        xueli.setCurrentItem(3);
    }

    private void initXinZiWheel(int indx) {
        xinzi = (WheelView) findViewById(R.id.xinzi_wheel);
        xinzi.setViewAdapter(new DateArrayAdapter(this, xinzis, 4));
        xinzi.setCurrentItem(indx);
    }

    private void getCurrArea() {
        map = new HashMap<String, Integer>();
        if (provinceVos != null && !provinceVos.isEmpty()) {
            Iterator<ProvinceVo> pIt = provinceVos.iterator();
            int i = 0;
            while (pIt.hasNext()) {
                ProvinceVo provinceVo = pIt.next();
                if (!userInfo.getAreaId().equals("0")) {
                    if (userInfo.getAreaId().equals(provinceVo.getId())) {
                        map.put("province", i);
                        area_et.setText(provinceVo.getText());
                        areaId = userInfo.getAreaId();
                        return;
                    }
                    Iterator<CityVo> cIt = provinceVo.getChildren().iterator();
                    int j = 0;
                    while (cIt.hasNext()) {
                        CityVo cityVo = cIt.next();
                        if (userInfo.getAreaId().equals(cityVo.getId())) {
                            map.put("province", i);
                            map.put("city", j);
                            area_et.setText(provinceVo.getText() + cityVo.getText());
                            areaId = userInfo.getAreaId();
                            return;
                        }
                        Iterator<CountyVo> cyIt = cityVo.getChildren().iterator();
                        int m = 0;
                        while (cyIt.hasNext()) {
                            CountyVo countyVo = cyIt.next();
                            if (userInfo.getAreaId().equals(countyVo.getId())) {
                                map.put("province", i);
                                map.put("city", j);
                                map.put("county", m);
                                area_et.setText(provinceVo.getText() + cityVo.getText() + countyVo.getText());
                                areaId = userInfo.getAreaId();
                                return;
                            }
                            m++;
                        }
                        j++;
                    }
                }
                i++;
            }
        }
    }

    private void initAreaWheel() {
        province = (WheelView) findViewById(R.id.province_wheel);
        city = (WheelView) findViewById(R.id.city_wheel);
        county = (WheelView) findViewById(R.id.county_wheel);

        if (provinceVos == null || provinceVos.isEmpty()) {
            return;
        }
        provinces = new String[provinceVos.size()];
        Iterator<ProvinceVo> pIt = provinceVos.iterator();
        int i = 0;
        while (pIt.hasNext()) {
            ProvinceVo provinceVo = pIt.next();
            provinces[i] = provinceVo.getText();
            i++;
        }

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCitys();
            }
        };

        province.setViewAdapter(new DateArrayAdapter(this, provinces, 0));

        if (map != null) {
            province.setCurrentItem(map.get("province"));
            updateCitys();
        } else {
            province.setCurrentItem(provinces.length / 2);
            updateCitys();
        }
        province.addChangingListener(listener);
        map = null;
    }

    void updateCitys() {
        allcityVos.clear();
        String prs = provinces[province.getCurrentItem()];
        Iterator<ProvinceVo> pIt = provinceVos.iterator();
        while (pIt.hasNext()) {
            final ProvinceVo provinceVo = pIt.next();
            if (prs.equals(provinceVo.getText())) {
                if (provinceVo.getChildren() != null && !provinceVo.getChildren().isEmpty()) {
                    citys = new String[provinceVo.getChildren().size()];
                    Iterator<CityVo> cIt = provinceVo.getChildren().iterator();
                    int i = 0;
                    while (cIt.hasNext()) {
                        CityVo cityVo = cIt.next();
                        citys[i] = cityVo.getText();
                        allcityVos.add(cityVo);
                        i++;
                    }

                    OnWheelChangedListener listener = new OnWheelChangedListener() {
                        @Override
                        public void onChanged(WheelView wheel, int oldValue, int newValue) {
                            updateCountys();
                        }
                    };
                    cityVos = provinceVo.getChildren();
                    city.setVisibility(View.VISIBLE);
                    city.setViewAdapter(new DateArrayAdapter(this, citys, 0));
                    if (map != null) {
                        city.setCurrentItem(map.get("city"));
                    } else {
                        city.setCurrentItem(citys.length / 2);
                    }
                    city.addChangingListener(listener);
                    county.setVisibility(View.VISIBLE);
                    updateCountys();
                    break;
                }else {
                    city.setVisibility(View.INVISIBLE);
                    county.setVisibility(View.INVISIBLE);
                }
            }
        }

    }

    void updateCountys() {
        allcountyVos.clear();
        String ct = citys[city.getCurrentItem()];
        Iterator<CityVo> cIt = cityVos.iterator();
        while (cIt.hasNext()) {
            CityVo cityVo = cIt.next();
            if (ct.equals(cityVo.getText())) {
                if (cityVo.getChildren() != null && !cityVo.getChildren().isEmpty()) {
                    countys = new String[cityVo.getChildren().size()];
                    Iterator<CountyVo> cyIt = cityVo.getChildren().iterator();
                    int i = 0;
                    while (cyIt.hasNext()) {
                        CountyVo countyVo = cyIt.next();
                        countys[i] = countyVo.getText();
                        allcountyVos.add(countyVo);
                        i++;
                    }
                    county.setVisibility(View.VISIBLE);
                    county.setViewAdapter(new DateArrayAdapter(this, countys, 0));
                    if (map != null) {
                        county.setCurrentItem(map.get("county"));
                    } else {
                        county.setCurrentItem(countys.length / 2);
                    }
                    break;
                } else {
                    county.setVisibility(View.INVISIBLE);
                }
            }
        }

    }

    private void initIndustryWheel() {
        hangye = (WheelView) findViewById(R.id.hangye_wheel);
        industry_strs = new String[industrys.size()];
        Iterator<Industry> it = industrys.iterator();
        int i = 0;
        while (it.hasNext()) {
            Industry industry = it.next();
            industry_strs[i] = industry.getName();
            i++;
        }
        hangye.setViewAdapter(new DateArrayAdapter(this, industry_strs, 0));
        if (!userInfo.getUserIndustry().equals("")) {
            for (int z = 0; z < industry_strs.length; z++) {
                if (industry_strs[z].equals(userInfo.getUserIndustry())) {
                    hangye.setCurrentItem(z);
                    break;
                }
            }
        } else {
            hangye.setCurrentItem(industry_strs.length / 2);
        }
    }

    private void setXingzuo(int m, int d) {
        for (int i = 0; i < xingzuo.length; i++) {
            long st = Tools.getTimeByDateStr("1970-" + xingzuo[i][1] + "-" + xingzuo[i][2] + " 00:00:00");
            long et = Tools.getTimeByDateStr("1970-" + xingzuo[i][3] + "-" + xingzuo[i][4] + " 00:00:00");
            long t = 0l;
            if (m < 10 && d < 10) {
                t = Tools.getTimeByDateStr("1970-0" + m + "-0" + d + " 00:00:00");
            } else if (m < 10 && d >= 10) {
                t = Tools.getTimeByDateStr("1970-0" + m + "-" + d + " 00:00:00");
            } else if (m >= 10 && d < 10) {
                t = Tools.getTimeByDateStr("1970-" + m + "-0" + d + " 00:00:00");
            } else {
                t = Tools.getTimeByDateStr("1970-" + m + "-" + d + " 00:00:00");
            }
            if (t >= st && t <= et) {
                xingzuo_et.setText(xingzuo[i][0]);
                break;
            }
        }
    }

    private void createCamera() {
        Intent intent = new Intent();
        intent.setClass(this, CameraActivity.class);
        startActivity(intent);
    }

    private void createViewPhotoGroup() {
        viewPhotoGroupLayout = new ViewPhotoGroupLayout(this);
        viewPhotoGroupLayout.setwManager(null);
        viewPhotoGroupLayout.setViewPhotoGroupClickEvent(new MyViewPhotoGroupClickEvent());
    }

    class MyViewPhotoGroupClickEvent implements ViewPhotoGroupClickEvent {

        @Override
        public void submitSelect(LocalImageBean bean) {
            createViewPhoto(bean);
            closeWin();
        }

        @Override
        public void closeWin() {
            if (viewPhotoGroupLayout != null) {
                viewPhotoGroupLayout.remove();
                viewPhotoGroupLayout = null;
            }
        }

        @Override
        public void backClick() {
            closeWin();

        }
    }

    private void createViewPhoto(LocalImageBean bean) {
        viewPhotoLayout = new ViewPhotoLayout(this);
        viewPhotoLayout.setwManager(null);
        viewPhotoLayout.setImageBean(bean);
        viewPhotoLayout.setViewPhotoClickEvent(new MyViewPhotoClickEvent());
    }

    class MyViewPhotoClickEvent implements ViewPhotoClickEvent {

        @Override
        public void submitSelect(ImageBean bean) {
            createClipPic(bean.getPath(), false);
            closeWin();
        }

        @Override
        public void closeWin() {
            if (viewPhotoLayout != null) {
                viewPhotoLayout.remove();
                viewPhotoLayout = null;
            }
        }

        @Override
        public void backClick() {
            createViewPhotoGroup();
            closeWin();
        }
    }

    private void createClipPic(String picPath, boolean isFormCamera) {
        clipPicLayout = new ClipPicLayout(this);
        clipPicLayout.setwManager(null);
        clipPicEvent = new MyClipPicEvent();
        clipPicLayout.setClipPicEvent(clipPicEvent);
        clipPicLayout.setFilePath(picPath, isFormCamera);
        clipPicLayout.setDoUploadImgListener(new DoUploadImgListener() {
            @Override
            public void doUpload(String filePath) {
                UserServiceManager.getInstance().setContext(UserMainActivity.this).setShowLoading(true).uploadFile(filePath, new UploadFileCallBack() {
                    @Override
                    public void uploadSuc(String url) {
                        userInfo.setUserPic(url);
                        clipPicEvent.submitPic();
                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    class MyClipPicEvent implements ClipPicEvent {

        @Override
        public void submitPic() {
            LogUtils.i("使用照片");
            closeWin();
            initDatas();
            ToastUtils.createToast(BaseApplication.getInstance(), getString(R.string.user_main_uploadimg_suctips));
        }

        @Override
        public void closeWin() {
            if (clipPicLayout != null) {
                clipPicLayout.remove();
                clipPicLayout = null;
            }
        }

        @Override
        public void backClick(boolean isFormCamera) {
            if (isFormCamera) {
                createCamera();
            } else {
                createViewPhotoGroup();
            }
            closeWin();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                hideMenu();
                String yr = ((DateNumericAdapter) year.getViewAdapter()).getItemText(year.getCurrentItem()).toString();
                int m = month.getCurrentItem() + 1;
                int d = Tools.getNumFromStr(((DateNumericAdapter) day.getViewAdapter()).getItemText(day.getCurrentItem()).toString());
                brithdayTime = Tools.getNumFromStr(yr) + "-" + m + "-" + d;
                birthday_et.setText(yr + months[month.getCurrentItem()] + ((DateNumericAdapter) day.getViewAdapter()).getItemText(day.getCurrentItem()));
                setXingzuo(m, d);
                break;
            case R.id.birthday_et_id:
                showDateMenu();
                break;
            case R.id.sex_et_id:
                if (userInfo.getUserSex().equals("0")) {
                    showSexMenu();
                }
                break;
            case R.id.tv_submit_sex:
                hideMenu();
                if (gril_rbt.isChecked()) {
                    sex_et.setText(sex[1][1]);
                } else if (boy_rbt.isChecked()) {
                    sex_et.setText(sex[0][1]);
                }
                break;
            case R.id.userimg_ly_id:
                showImgMenu();
                break;
            case R.id.user_portrait:
                if (!userInfo.getUserPic().equals("")) {
                    ImageViewMagnifyLoadingDialog mdialog = new ImageViewMagnifyLoadingDialog(
                            UserMainActivity.this, userInfo.getUserPic());
                    mdialog.show();
                }
                break;
            case R.id.camera_tv_id:
                hideMenu();
                createCamera();
                break;
            case R.id.picture_tv_id:
                hideMenu();
                createViewPhotoGroup();
                break;
            case R.id.tv_cancel_hangye:
            case R.id.tv_cancel_area:
            case R.id.tv_cancel_xueli:
            case R.id.tv_cancel_xinzi:
            case R.id.menu_layout_id:
            case R.id.tv_cancel_sex:
            case R.id.tv_cancel:
            case R.id.cancel_tv_img:
                hideMenu();
                break;
            case R.id.xinzi_et_id:
                showXinZiMenu();
                break;
            case R.id.xueli_et_id:
                showXueLiMenu();
                break;
            case R.id.tv_submit_xinzi:
                hideMenu();
                xinzi_et.setText(xinzis[xinzi.getCurrentItem()]);
                break;
            case R.id.tv_submit_xueli:
                hideMenu();
                xueli_et.setText(xuelis[xueli.getCurrentItem()]);
                break;
            case R.id.area_et_id:
                showAreaMenu();
                break;
            case R.id.tv_submit_area:
                hideMenu();
                if (city.getVisibility() != View.VISIBLE) {
                    area_et.setText(provinces[province.getCurrentItem()]);
                    Iterator<ProvinceVo> pIt = provinceVos.iterator();
                    while (pIt.hasNext()) {
                        ProvinceVo provinceVo = pIt.next();
                        if (provinceVo.getText().equals(provinces[province.getCurrentItem()])) {
                            areaId = provinceVo.getId();
                            break;
                        }
                    }

                }else if (county.getVisibility() == View.VISIBLE) {
                    area_et.setText(provinces[province.getCurrentItem()] + citys[city.getCurrentItem()] + countys[county.getCurrentItem()]);
                    Iterator<CountyVo> it = allcountyVos.iterator();
                    while (it.hasNext()) {
                        CountyVo countyVo = it.next();
                        if (countyVo.getText().equals(countys[county.getCurrentItem()])) {
                            areaId = countyVo.getId();
                            break;
                        }
                    }

                } else {
                    area_et.setText(provinces[province.getCurrentItem()] + citys[city.getCurrentItem()]);
                    Iterator<CityVo> it = allcityVos.iterator();
                    while (it.hasNext()) {
                        CityVo cityVo = it.next();
                        if (cityVo.getText().equals(citys[city.getCurrentItem()])) {
                            areaId = cityVo.getId();
                            break;
                        }
                    }
                }
                break;
            case R.id.hangye_et_id:
                showHangyeMenu();
                break;
            case R.id.tv_submit_hangye:
                hideMenu();
                hangye_et.setText(industry_strs[hangye.getCurrentItem()]);
                break;
            case R.id.date_ly_id:
            case R.id.sex_ly_id:
            case R.id.img_ly_id:
            case R.id.xinzi_ly_id:
            case R.id.xueli_ly_id:
            case R.id.area_ly_id:
            case R.id.hangye_ly_id:
                break;
        }
    }

    private void setUserUpdate() {
        String userNick, userName, userSex, userBrithday, userStar, userHeight, userWeight, userIndustry,
                userCompany, userJob, userSalary, userSchool, userSpecialty, userEdu, userHobby, userIntro, userSign;
        userNick = nkname_et.getText().toString();
        userInfo.setUserNick(userNick);
        userSex = sex_et.getText().toString().trim();
        if (userSex.equals(sex[1][1])) {
            userInfo.setUserSex(sex[1][0]);
        } else if (userSex.equals(sex[0][1])) {
            userInfo.setUserSex(sex[0][0]);
        }
        userName = realname_et.getText().toString();
        userInfo.setUserName(userName);
        userSign = qianming_et.getText().toString();
        userInfo.setUserSign(userSign);
        userBrithday = brithdayTime;
        userInfo.setUserBrithday(userBrithday);
        userStar = xingzuo_et.getText().toString();
        if (getString(R.string.user_main_starthint_tv).equals(userStar)) {
            userInfo.setUserStar("");
        } else {
            userInfo.setUserStar(userStar);
        }
        userHeight = shengao_et.getText().toString();
        userInfo.setUserHeight(userHeight);
        userWeight = tizhong_et.getText().toString();
        userInfo.setUserWeight(userWeight);
        userInfo.setAreaId(areaId);
        userIndustry = hangye_et.getText().toString();
        if (getString(R.string.user_main_industryhint_tv).equals(userIndustry)) {
            userInfo.setUserIndustry("");
        } else {
            userInfo.setUserIndustry(userIndustry);
        }
        userCompany = gongsi_et.getText().toString();
        userInfo.setUserCompany(userCompany);
        userJob = zhiwu_et.getText().toString();
        userInfo.setUserJob(userJob);
        userSalary = xinzi_et.getText().toString();
        if (getString(R.string.user_main_salaryhint_tv).equals(userSalary)) {
            userInfo.setUserSalary("");
        } else {
            userInfo.setUserSalary(userSalary);
        }
        userSchool = xuexiao_et.getText().toString();
        userInfo.setUserSchool(userSchool);
        userSpecialty = zye_et.getText().toString();
        userInfo.setUserSpecialty(userSpecialty);
        userEdu = xueli_et.getText().toString();
        if (getString(R.string.user_main_eduhint_tv).equals(userEdu)) {
            userInfo.setUserEdu("");
        } else {
            userInfo.setUserEdu(userEdu);
        }
        userHobby = aihao_et.getText().toString();
        userInfo.setUserHobby(userHobby);
        userIntro = jianjie_et.getText().toString();
        userInfo.setUserIntro(userIntro);
        newUserCode = Tools.md5(new Gson().toJson(userInfo.getValus()));
        LogUtils.i(newUserCode);
    }

    @Override
    protected void doSave() {
        LogUtils.i("save");
        setUserUpdate();
        UserServiceManager.getInstance().setContext(UserMainActivity.this).setShowLoading(true).saveUserInfo(userInfo, new SaveUserInfoCallBack() {
            @Override
            public void saveSuc() {
                oldUserCode = newUserCode;
            }

            @Override
            public void fail(String code) {

            }
        });
    }

    private void regist() {
        mMessageReceiver = new MyMessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction("user_img_change");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    class MyMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if (b != null) {
                userInfo.setUserPic(b.getString("imgurl"));
            }
            if ("user_img_change".equals(action)) {
                LogUtils.i("====user_img_change===========");
                initDatas();
            }
        }
    }

    @Override
    protected void doBack() {
        setUserUpdate();
        if (oldUserCode.equals(newUserCode)) {
            finish();
        } else {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        industrys = null;
        citys = null;
        provinces = null;
        countys = null;
        provinceVos = null;
        cityVos = null;
        industry_strs = null;
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        } catch (Exception e) {
            LogUtils.w(e.toString());
        }
    }
}

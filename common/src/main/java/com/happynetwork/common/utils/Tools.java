package com.happynetwork.common.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公用工具类
 */
public class Tools {

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 是否在前台
     * @param context
     * @return
     */
    public static boolean isForeground(Context context) {
        boolean isLauncherForeground = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.contains(context.getPackageName())) {
                            isLauncherForeground = true;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().contains(context.getPackageName())) {
                isLauncherForeground = true;
            }
        }

        return isLauncherForeground;
    }

    /**
     * 提取字符串中的数字
     * @param str 字符串
     * @return -1，没有数字或者字符串为空
     */
    public static int getNumFromStr(String str){
        if(str == null){
            return -1;
        }
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String res = m.replaceAll("").trim();
        LogUtils.i(res);
        if(res == null || res.equals("")){
            return -1;
        }
        return Integer.parseInt(res);
    }

    /**
     * 把字符串形式的时间戳转化为时间戳
     *
     * @param timeStr 字符串形式的时间戳
     * @return 时间戳
     */
    public static long getTimeByStr(String timeStr) {
        if (timeStr == null) {
            return 0l;
        }
        timeStr = timeStr.trim();
        return Long.parseLong(timeStr);
    }

    /**
     * 把字符串形式的时间yyyy-MM-dd HH:mm:ss转化为时间戳
     *
     * @param timeStr 字符串形式的时间戳
     * @return 时间戳
     */
    public static long getTimeByDateStr(String timeStr) {
        if (timeStr == null) {
            return 0l;
        }
        long time = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStr.trim());
            time = date.getTime();
        } catch (ParseException e) {
            LogUtils.e(e.toString());
            time = 0l;
        }
        return time;
    }

    public static String getReadTimeStr(long t) {
        String result = "";
        long temp = System.currentTimeMillis();
        if (temp - t < 10000) {
            result = "刚刚";
        } else if (temp - t >=10000 && temp - t<60000) {
            result = (temp - t)/1000 +"秒以前";
        } else if (temp - t >=60000 && temp - t<3600000) {
            result = (temp - t)/60000+"分钟以前";
        } else if (temp - t >=3600000 && temp - t < 3600000*24) {
            result = (temp - t)/3600000+"小时以前";
        }else if (temp - t >=3600000*24 && temp - t < 3600000*24*7) {
            result = (temp - t)/3600000*24+"天以前";
        }else if (temp - t >=3600000*24*7 && temp - t < 3600000*24*30) {
            result = (temp - t)/3600000*24*7+"周以前";
        }else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            result = format.format(new Date(t));
        }
        return result;
    }

    /**
     * 把时间戳转化为yyyy-MM-dd HH:mm格式
     *
     * @param time 时间戳
     * @return yyyy-MM-dd HH:mm格式的字符串
     */
    public static String getDateStr(long time) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String a1 = f.format(new Date(time));
        return a1;
    }

    /**
     * 把时间戳转化为yyyy-MM-dd格式
     *
     * @param time 时间戳
     * @return yyyy-MM-dd格式的字符串
     */
    public static String getDateDayStr(long time) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String a1 = f.format(new Date(time));
        return a1;
    }

    public static String getHmDateStr(long time) {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        String a1 = f.format(new Date(time));
        return a1;
    }

    /**
     * 获取指定字符串格式的时间戳的月份
     *
     * @param timeStr 字符串格式的时间戳
     * @return 月份
     */
    public static int getMonth(String timeStr) {
        long time = getTimeByStr(timeStr);
        if (time > 0l) {
            Date date = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            return c.get(Calendar.MONTH);
        }
        return -1;
    }

    /**
     * 超出字符数的字符串以省略号结尾
     *
     * @param str 源字符串
     * @param len 最大长度
     * @return 格式化后的字符串
     */
    public static String getShortStr(String str, int len) {
        if (str != null) {
            if (str.length() <= len) {
                return str;
            } else {
                return str.substring(0, len - 1) + "……";
            }
        }
        return str;
    }

    /**
     * 获取指定字符串格式的时间戳的周数
     *
     * @param timeStr 字符串格式的时间戳
     * @return 周
     */
    public static int getWeek(String timeStr) {
        long time = getTimeByStr(timeStr);
        if (time > 0l) {
            Date date = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            return c.get(Calendar.WEEK_OF_YEAR);
        }
        return -1;
    }

    /**
     * 获取指定字符串格式的时间戳的每周的第几天
     *
     * @param timeStr 字符串格式的时间戳
     * @return 每周的第几天
     */
    public static int getDayOfWeek(String timeStr) {
        long time = getTimeByStr(timeStr);
        if (time > 0l) {
            Date date = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            return c.get(Calendar.DAY_OF_WEEK);
        }
        return -1;
    }

    /**
     * 用于请求签名，按key从a-z排序(包含秘钥),取值连接
     *
     * @param map 参数集合
     * @return 加密后的签名
     */
    public static String signValueMd5(Map<String, String> map) {
        List<String> infoIds = new ArrayList<String>(map.keySet());
        Collections.sort(infoIds, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return (o1.compareTo(o2));
            }
        });

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < infoIds.size(); i++) {
            String id = infoIds.get(i);
            sb.append(map.get(id));
        }
        LogUtils.i(sb.toString());
        return md5(sb.toString());
    }

    /**
     * 用于请求签名，按key从a-z排序,取值连接,最后加上秘钥
     *
     * @param map 参数集合
     * @param key 秘钥
     * @return 加密后的签名
     */
    public static String signValueMd5(Map<String, String> map, String key) {
        List<String> infoIds = new ArrayList<String>(map.keySet());
        Collections.sort(infoIds, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return (o1.compareTo(o2));
            }
        });

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < infoIds.size(); i++) {
            String id = infoIds.get(i);
            sb.append(map.get(id));
        }
        sb.append(key);
        LogUtils.i(sb.toString());
        return md5(sb.toString());
    }

    /**
     * 用于请求签名，按key从a-z排序,取键值对以&连接,最后加上秘钥
     *
     * @param map 参数集合
     * @param key 秘钥
     * @return 加密后的签名
     */
    public static String signMd5(Map<String, String> map, String key) {
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry entry = infoIds.get(i);
            sb.append(entry.toString());
            sb.append("&");
        }
        sb.append(key);
        LogUtils.i(sb.toString());
        return md5(sb.toString());
    }

    /**
     * 按key从a-z排序并编码
     *
     * @param map
     * @return
     */
    public static Map<String, String> urlEncode(Map<String, String> map) {
        List<String> infoIds = new ArrayList<String>(map.keySet());
        Collections.sort(infoIds, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return (o1.compareTo(o2));
            }
        });
        Map<String, String> resultMap = new HashMap<String, String>();
        for (int i = 0; i < infoIds.size(); i++) {
            String id = infoIds.get(i);
            try {
                resultMap.put(id, URLEncoder.encode(URLEncoder.encode(map.get(id), "UTF-8"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    /**
     * url解码
     *
     * @param str 需要解码的串
     * @return 解码后的串
     */
    public static String urlDecode(String str) {
        if (str == null) return null;
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return 手机Ip
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtils.w(ex.toString());
        }
        return "";
    }

    /**
     * 效验手机号是否合法
     *
     * @param telnum 要效验的手机号
     * @return false 不合法 ，反之则反之
     */
    public static boolean checkTelphone(String telnum) {
        if (telnum == null) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(telnum);
        return matcher.matches();
    }

    /**
     * 效验密码 6-16
     *
     * @param pass 需要效验的密码
     * @return false 不合法，反之则反之
     */
    public static boolean checkPass(String pass) {
        if (pass == null) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^[^\\s]{6,16}$");
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }

    /**
     * 效验用户名 6-16字母数字下划线
     *
     * @param name 需要效验的用户名
     * @return false 不合法，反之则反之
     */
    public static boolean checkUname(String name) {
        if (name == null) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^[a-zA-Z0-9][a-zA-Z_0-9]{4,14}[a-zA-Z0-9]$");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * 效验email
     *
     * @param email
     * @return false 不合法，反之则反之
     */
    public static boolean checkEmail(String email) {
        if (email == null) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 根据字符串返回整数
     *
     * @param str 字符串
     * @return 整数
     */
    public static int getNum(String str) {
        if (str == null || str.equals("")) {
            return 0;
        }
        if (!isNum(str)) {
            return -1;
        }
        return Integer.parseInt(str);
    }

    /**
     * 根据字符串返回浮点数
     *
     * @param str 字符串
     * @return 浮点数
     */
    public static double getDoubleNum(String str) {
        if (str == null || str.equals("")) {
            return 0d;
        }
        double num = 0d;
        try {
            num = Double.parseDouble(str);
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(2);
            str = nf.format(num);
            num = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            LogUtils.w(e.toString());
        }

        return num;
    }

    /**
     * 是否为数字
     *
     * @param str 字符串
     * @return false 不合法，反之则反之
     */
    public static boolean isNum(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^[0-9]+$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 是否为浮点数
     *
     * @param str 字符串
     * @return false 不合法，反之则反之
     */
    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        boolean flag = false;
        try {
            Double.parseDouble(str);
            flag = true;
        } catch (NumberFormatException e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取6位随机数
     *
     * @return 随机数
     */
    public static String productCheckCode() {
        Random random = new Random();
        StringBuffer br = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            br.append(random.nextInt(10));
        }
        return br.toString();
    }

    /**
     * 获取32位随机编码
     *
     * @return 随机编码
     */
    public static String getTokenStr() {
        Random random = new Random();
        StringBuffer br = new StringBuffer();
        for (int i = 0; i < 32; i++) {
            br.append(random.nextInt(10));
        }
        return md5(br.toString());
    }

    private static String toHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        int len = digest.length;

        String out = null;
        for (int i = 0; i < len; i++) {
            out = Integer.toHexString(0xFF & digest[i]);//原始方法
            if (out.length() == 1) {
                sb.append("0");//如果为1位 前面补个0
            }
            sb.append(out);
        }
        return sb.toString().toUpperCase();
    }

    private static String toHexLow(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        int len = digest.length;

        String out = null;
        for (int i = 0; i < len; i++) {
            out = Integer.toHexString(0xFF & digest[i]);//原始方法
            if (out.length() == 1) {
                sb.append("0");//如果为1位 前面补个0
            }
            sb.append(out);
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 获取md5加密串的大写形式
     *
     * @param val 要加密的串
     * @return 大写形式的加密串
     */
    public static String md5(String val) {
        LogUtils.i(val);
        val = val == null ? "" : val;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return toHex(m);
    }

    /**
     * 获取md5加密串的小写形式
     *
     * @param val 要加密的串
     * @return 小写形式的加密串
     */
    public static String md5Low(String val) {
        val = val == null ? "" : val;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return toHexLow(m);
    }

    /**
     * 把字节数组转化为字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    public static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }

    /**
     * 效验身份证
     *
     * @param str 需要效验的身份证
     * @return false 不合法，反之则反之
     */
    public static boolean isCard(String str) {
        Pattern pattern = Pattern.compile("(^\\d{18}$)|(^\\d{15}$)");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 效验银行卡号
     *
     * @param str 需要效验的银行卡号
     * @return false 不合法，反之则反之
     */
    public static boolean isBankCard(String str) {
        Pattern pattern = Pattern.compile("^\\d{19}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String getShowTime(long milliseconds) {
        // 获取日历函数
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat dateFormat = null;
        // 判断是否大于60分钟，如果大于就显示小时。设置日期格式
        if (milliseconds / 60000 > 60) {
            dateFormat = new SimpleDateFormat("hh:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("00:mm:ss");
        }
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 播放动画，主要用于UtoVR播放器
     *
     * @param Img
     * @param anim
     */
    public static void startImageAnim(ImageView Img, int anim) {
        Img.setVisibility(View.VISIBLE);
        try {
            Img.setImageResource(anim);
            AnimationDrawable animationDrawable = (AnimationDrawable) Img.getDrawable();
            animationDrawable.start();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止动画,主要用于UtoVR播放器
     *
     * @param Img
     */
    public static void stopImageAnim(ImageView Img) {
        try {
            AnimationDrawable animationDrawable = (AnimationDrawable) Img.getDrawable();
            animationDrawable.stop();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        Img.setVisibility(View.GONE);
    }

    /**
     * 根据时间生成年月日格式的log文件名
     *
     * @return yyyy-MM-dd格式的log文件名
     */
    public static String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date(System.currentTimeMillis()));
        return "xf-" + date + ".log";// 2012年10月03日 23:41:31
    }

    /**
     * 获取当前时间的yyyy-MM-dd HH:mm:ss格式
     *
     * @return yyyy-MM-dd HH:mm:ss的时间
     */
    public static String getDateEN() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;// 2012-10-03 23:41:31
    }

    /**
     * base64编码
     */
    public static String encodeBase64(String s) {
        String reslut = "";
        if (s == null) {
            reslut = "";
        } else {
            try {
                reslut = Base64.encodeToString(s.getBytes("UTF-8"), Base64.NO_WRAP);
            } catch (UnsupportedEncodingException e) {
                LogUtils.w(e.toString());
                reslut = "";
            }
        }
        return reslut;
    }

    /**
     * base64解码
     */
    public static String decodeBase64(String s) {
        String reslut = "";
        if (s == null) {
            reslut = "";
        } else {
            try {
                reslut = new String(Base64.decode(s, Base64.NO_WRAP), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LogUtils.w(e.toString());
                reslut = "";
            }
        }
        return reslut;
    }


    /**
     * base64编码DEFAULT
     */
    public static String encodeBase64_default(String s) {
        String reslut = "";
        if (s == null) {
            reslut = "";
        } else {
            try {
                reslut = Base64.encodeToString(s.getBytes("UTF-8"), Base64.DEFAULT);
            } catch (UnsupportedEncodingException e) {
                LogUtils.w(e.toString());
                reslut = "";
            }
        }
        return reslut;
    }

    /**
     * base64解码DEFAULT
     */
    public static String decodeBase64_default(String s) {
        String reslut = "";
        if (s == null) {
            reslut = "";
        } else {
            try {
                reslut = new String(Base64.decode(s, Base64.DEFAULT), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LogUtils.w(e.toString());
                reslut = "";
            }
        }
        return reslut;
    }

}

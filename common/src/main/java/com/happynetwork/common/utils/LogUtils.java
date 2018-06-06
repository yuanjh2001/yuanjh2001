package com.happynetwork.common.utils;

import android.util.Log;

/**
 * 日志输出控制类
 * @author Tom.yuan
 */
public final class LogUtils {
	private static String tag = " ==>> ";
	private static boolean logIsOpen = true;
	private LogUtils() {

	}

	public static void setLogIsOpen(boolean isOpen){
		logIsOpen = isOpen;
	}

	/**
	 * @param msg The message you would like logged.
	 */
	public static int v( String msg) {
        if(logIsOpen){
            return Log.v(tag,msg+getStackTrace());
        }
        return 0;
	}

	/**
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static int v(String msg, Throwable tr) {
        if(logIsOpen){
            return Log.v(tag,msg,tr);
        }
        return 0;
	}

	/**
	 * @param msg The message you would like logged.
	 */
	public static int d( String msg) {
		if(logIsOpen){
			return Log.d(tag,msg+getStackTrace());
		}
		return 0;
	}

	/**
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static int d(String msg, Throwable tr) {
        if(logIsOpen){
            return Log.d(tag,msg,tr);
        }
        return 0;
	}

	/**
	 * @param msg The message you would like logged.
	 */
	public static int i(String msg) {
        if(logIsOpen){
            return Log.i(tag,msg+getStackTrace());
        }
        return 0;
	}

    private static String getStackTrace(){
        if(Thread.currentThread().getStackTrace().length>4){
            return "\n"+Thread.currentThread().getStackTrace()[4].toString();
        }
        return "";
    }

	/**
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static int i(String msg, Throwable tr) {
        if(logIsOpen){
            return Log.i(tag,msg,tr);
        }
        return 0;
	}

	/**
	 * @param msg The message you would like logged.
	 */
	public static int w(String msg) {
        if(logIsOpen){
            return Log.w(tag,msg+getStackTrace());
        }
        return 0;
	}

	/**
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static int w (String msg, Throwable tr) {
        if(logIsOpen){
            return Log.w(tag,msg,tr);
        }
        return 0;
	}

	/**
	 * @param msg The message you would like logged.
	 */
	public static int e(String msg) {
        if(logIsOpen){
            return Log.e(tag,msg+getStackTrace());
        }
        return 0;
	}

	/**
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static int e(String msg, Throwable tr) {
        if(logIsOpen){
            return Log.e(tag,msg,tr);
        }
        return 0;
	}
}
package com.happynetwork.common.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Tom.yuan on 2016/12/27.
 * 3des
 *
 *
 * 用法示例
 * byte[] key=Base64.decode("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4".getBytes(),Base64.DEFAULT);
 * byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
 * byte[] data= new byte[0];
 * try {
 * data = "中国ABCabc123".getBytes("UTF-8");
 * } catch (UnsupportedEncodingException e) {
 * e.printStackTrace();
 * }

 * LogUtils.i("ECB加密解密");
 * byte[] str3 = EncryptUtils.des3EncodeECB(key,data );
 * byte[] str4 = EncryptUtils.ees3DecodeECB(key, str3);
 * try {
 * LogUtils.i(new String(Base64.encode(str3,  Base64.NO_WRAP),"UTF-8"));
 * LogUtils.i(new String(str4, "UTF-8"));
 * } catch (UnsupportedEncodingException e) {
 * e.printStackTrace();
 * }


 * LogUtils.i("CBC加密解密");
 * byte[] str5 = EncryptUtils.des3EncodeCBC(key, keyiv, data);
 * byte[] str6 = EncryptUtils.des3DecodeCBC(key, keyiv, str5);
 * try {
 * LogUtils.i(new String(Base64.encode(str5,  Base64.NO_WRAP),"UTF-8"));
 * LogUtils.i(new String(str6, "UTF-8"));
 * } catch (UnsupportedEncodingException e) {
 * e.printStackTrace();
 * }
 */

public class EncryptUtils {
    /**
     * ECB加密,不要IV
     *
     * @param key  密钥
     * @param data 明文
     * @return Base64编码的密文
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data) {

        Key deskey = null;
        byte[] bOut = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            bOut = cipher.doFinal(data);
        } catch (Exception e) {
            LogUtils.w(e.toString());
        }

        return bOut;
    }

    /**
     * ECB解密,不要IV
     *
     * @param key  密钥
     * @param data Base64编码的密文
     * @return 明文
     */
    public static byte[] ees3DecodeECB(byte[] key, byte[] data) {

        Key deskey = null;
        byte[] bOut = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, deskey);
            bOut = cipher.doFinal(data);
        } catch (Exception e) {
            LogUtils.w(e.toString());
        }
        return bOut;

    }

    /**
     * CBC加密
     *
     * @param key   密钥
     * @param keyiv IV
     * @param data  明文
     * @return Base64编码的密文
     */
    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data) {

        Key deskey = null;
        byte[] bOut = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(keyiv);
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            bOut = cipher.doFinal(data);
        } catch (Exception e) {
            LogUtils.w(e.toString());
        }
        return bOut;
    }

    /**
     * CBC解密
     *
     * @param key   密钥
     * @param keyiv IV
     * @param data  Base64编码的密文
     * @return 明文
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data) {

        Key deskey = null;
        byte[] bOut = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(keyiv);

            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

            bOut = cipher.doFinal(data);
        } catch (Exception e) {
            LogUtils.w(e.toString());
        }

        return bOut;

    }
}

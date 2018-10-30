package com.daka.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @ClassName: DecodeUtils
 * 
 * @Description: 编码解码
 * 
 * @author
 * 
 */

public class DecodeUtils
{

    /**
     * @Title: decryptBASE64
     * @Description: BASE64解密
     * @param key
     * @return String
     * @throws
     */
    public static String decryptBASE64(String key)
        throws Exception
    {
        return new String(Base64.decodeBase64(key.getBytes()));
    }

    /**
     * @Title: encryptBASE64
     * @Description: BASE64加密
     * @param key
     * @return String
     * @throws
     */
    public static String encryptBASE64(byte[] key)
        throws Exception
    {
        return new String(Base64.encodeBase64(key));
    }

    /**
     * @Title: encryptMD5
     * @Description: MD5加密 32位小写
     * @param key
     * @return String
     * @throws
     */
    public static String encryptMD5(String key)
        throws Exception
    {
        return DigestUtils.md5Hex(key);
    }

    public static void main(String[] args)
        throws Exception
    {
        // String encryptMD5 = DecodeUtils.encryptMD5("INSURE_BUSINESS_CORE_VC00001");
        String encryptMD5 = DecodeUtils.encryptMD5("admin");
        String encryptBASE64 = DecodeUtils.encryptBASE64(encryptMD5.getBytes());
        String decryptBASE64 = DecodeUtils.decryptBASE64(encryptBASE64);
        System.out.println(encryptMD5);
        System.out.println(encryptBASE64);
        System.out.println(decryptBASE64);
    }
}

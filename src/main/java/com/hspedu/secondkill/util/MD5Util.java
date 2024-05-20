package com.hspedu.secondkill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 9:04
 */
public class MD5Util {

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    //准备 salt
    private static final String SALT = "t2J4jFyA";

    //第一次加密加盐  (md5(password + salt1))
    public static String inputPassToMidPass(String inputPass){
        String str =  SALT.charAt(0) + inputPass + SALT.charAt(6);
        return md5(str);
    }

    // 第二次加密加盐
    public static String MidPassToDBPass(String midPass, String salt){
        String str =  salt.charAt(0) + midPass + salt.charAt(6);
        return md5(str);
    }


    //编写个方法，可以将邻assword明文，直接转成DB中的密码
    public static String inputPassToDBPass(String inputPass, String saltDB){
        //inputPass 123456, saltDB t2J4jFyA
        String midPass = inputPassToMidPass(inputPass);
        String dbPass =  MidPassToDBPass(midPass, saltDB);
        return dbPass;
    }


}

package com.hspedu.secondkill.util;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 9:38
 */

import org.junit.jupiter.api.Test;

/**
 * 测试MD5Ut1L方法的使用
 */
public class MD5UtilTest {
    @Test
    public void f1(){
        String midPass = MD5Util.inputPassToMidPass("123456");
        System.out.println(midPass);

        String dbPass = MD5Util.MidPassToDBPass(midPass, "abcd1234");
        System.out.println(dbPass);

        String s = MD5Util.inputPassToDBPass("123456", "abcd1234");
        System.out.println(s);
    }


}

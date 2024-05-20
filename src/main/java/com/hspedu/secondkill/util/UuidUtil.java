package com.hspedu.secondkill.util;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 16:23
 */
public class UuidUtil {

    public static String getUuid(){
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}

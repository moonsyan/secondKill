package com.hspedu.secondkill.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 10:12
 */
//  完成一些校验工作，比如手机号码格式是否正确.·
public class ValidatorUtil {
    private static final Pattern mobile_pattern = Pattern.compile("^[1][3-9][0-9]{9}$");
    public static boolean isMobile(String mobile){
       if(!StringUtils.hasText(mobile)){
           return false;
       }
        return mobile_pattern.matcher(mobile).matches();
    }
}

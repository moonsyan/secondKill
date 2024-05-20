package com.hspedu.secondkill.vo;

import com.hspedu.secondkill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 10:11
 */
//LoginVo:接收用户登录时，发的信息(mobile,password).
@Data
public class LoginVo {

    @NotNull
    @IsMobile(required = true)
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

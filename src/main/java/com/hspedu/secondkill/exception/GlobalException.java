package com.hspedu.secondkill.exception;

import com.hspedu.secondkill.vo.RespBean;
import com.hspedu.secondkill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 15:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalException extends RuntimeException{

    private RespBeanEnum respBeanEnum;



}

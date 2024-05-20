package com.hspedu.secondkill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-19 8:42
 */
//秒杀消息对象
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecondKillMessage {

    private User user;
    private Long goodsId;
}

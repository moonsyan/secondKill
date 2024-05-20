package com.hspedu.secondkill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-16 17:14
 */
@Data
@TableName("t_seckill_order")
public class SeckillOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
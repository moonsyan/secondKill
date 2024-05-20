package com.hspedu.secondkill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-16 10:44
 */
@Data
@TableName("t_goods")
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商品 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String goodsName;
    /**
     * 商品标题
     */
    private String goodsTitle;
    /**
     * 商品图片
     */
    private String goodsImg;
    /**
     * 商品详情
     */
    private String goodsDetail;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;
    /**
     * 商品库存
     */
    private Integer goodsStock;
}

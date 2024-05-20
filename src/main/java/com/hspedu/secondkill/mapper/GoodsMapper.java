package com.hspedu.secondkill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hspedu.secondkill.pojo.Goods;
import com.hspedu.secondkill.vo.GoodsVo;

import java.util.List;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-16 10:51
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    //获取商品列表
    List<GoodsVo> findGoodsVo();

    //获取商品详细
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}

package com.hspedu.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.secondkill.mapper.GoodsMapper;
import com.hspedu.secondkill.pojo.Goods;
import com.hspedu.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-16 11:02
 */
public interface GoodsService extends IService<Goods> {
    //秒杀商品列表
    List<GoodsVo> findGoodsVo();

    //获取商品详情
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}

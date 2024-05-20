package com.hspedu.secondkill.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.secondkill.mapper.GoodsMapper;
import com.hspedu.secondkill.pojo.Goods;
import com.hspedu.secondkill.service.GoodsService;
import com.hspedu.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-16 11:03
 */
@Service
public class  GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    //获取商品列表
    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    //获取商品详情
    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}

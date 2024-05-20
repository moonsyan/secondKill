package com.hspedu.secondkill.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.secondkill.mapper.SeckillGoodsMapper;
import com.hspedu.secondkill.pojo.SeckillGoods;
import com.hspedu.secondkill.service.SeckillGoodsService;
import org.springframework.stereotype.Service;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-16 11:05
 */
@Service
public class SeckillGoodsServiceImpl extends
        ServiceImpl<SeckillGoodsMapper, SeckillGoods>
        implements SeckillGoodsService {
}

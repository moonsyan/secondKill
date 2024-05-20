package com.hspedu.secondkill.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.secondkill.mapper.SeckillOrderMapper;
import com.hspedu.secondkill.pojo.SeckillOrder;
import com.hspedu.secondkill.service.SeckillOrderService;
import org.springframework.stereotype.Service;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-16 17:24
 */
@Service
public class SeckillOrderServiceImpl
        extends ServiceImpl<SeckillOrderMapper, SeckillOrder>
        implements SeckillOrderService {
}
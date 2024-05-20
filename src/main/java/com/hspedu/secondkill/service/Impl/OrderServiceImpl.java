package com.hspedu.secondkill.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.secondkill.mapper.OrderMapper;
import com.hspedu.secondkill.pojo.Order;
import com.hspedu.secondkill.pojo.SeckillGoods;
import com.hspedu.secondkill.pojo.SeckillOrder;
import com.hspedu.secondkill.pojo.User;
import com.hspedu.secondkill.service.OrderService;
import com.hspedu.secondkill.service.SeckillGoodsService;
import com.hspedu.secondkill.service.SeckillOrderService;
import com.hspedu.secondkill.util.MD5Util;
import com.hspedu.secondkill.util.UuidUtil;
import com.hspedu.secondkill.vo.GoodsVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-16 17:23
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    private SeckillGoodsService seckillGoodsService;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SeckillOrderService seckillOrderService;


    @Resource
    private RedisTemplate redisTemplate;
    //秒杀商品，减少库存
    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goodsVo) {

        System.out.println("=============================开始生成订单==========================");
        //查询后端的库存量进行减一
        SeckillGoods seckillGoods =
                seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
                        .eq("goods_id", goodsVo.getId()));



        //下面这句和判断库存不是原子性操作，会出现问题,后面再优化
//        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
//        seckillGoodsService.updateById(seckillGoods);


        //1.Mysql在默认的事务隔离级[REPEATABLE-READ]别下I
        //2.执行update语句时，会在事务中锁定要更新的行
        //3.这样可以防止其它会话在同一行执行update,delete

        //只要在更新成功时，返回trUe,否刚返回false,即更新后，受影响的行数1为T
        boolean update = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count - 1")
                .eq("goods_id", goodsVo.getId()).gt("stock_count", 0));

        if(!update){//如果更新失败，说明己经没有库存了
            //把这个秒杀失败的信息-记录到Redis
            redisTemplate.opsForValue().set("secondFail:"+user.getId()+":"+goodsVo.getId(),"fail");
            return null;
        }


        //生成普通订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);   //设置一个初始值-未支付
        order.setCreateDate(new Date());
        orderMapper.insert(order);


        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsVo.getId());
        //这里秒杀商品订单对应的order._id是从上面你添加order,后获取到的
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrderService.save(seckillOrder);

        //将生成的秒杀订单，存入到Rdis,这样在查询某个用户是否已经秒杀了这个商品时，
        //直接到Redis中查询，起到优化效果

        //设计秒杀订单  key=>  order:用户id:商品id
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goodsVo.getId(), seckillOrder);

        return order;
    }

    @Override
    public String createPath(User user, Long goodsId) {
        //生成秒杀路径值
        String path = MD5Util.md5(UuidUtil.getUuid());
        //将随机生成的路径保存到Rdis,设置一个超时时间60s
        //key =>  seckillPath:用户id:商品id
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+":"+goodsId,path,60, TimeUnit.SECONDS );

        return path;
    }

    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if(user != null && goodsId < 0 && !StringUtils.hasText(path)){
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);

        return redisPath.equals(path);

    }

    @Override
    public boolean checkCapthcha(User user, Long goodsId, String captcha) {
        if(user == null || goodsId < 0 || !StringUtils.hasText(captcha)){
            return false;
        }

        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        if(redisCaptcha != null){
            return redisCaptcha.equals(captcha);
        }
        return false;

    }


}
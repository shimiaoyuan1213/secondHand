package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.common.enums.OrderStatusEnum;
import com.example.entity.Account;
import com.example.entity.Address;
import com.example.entity.Goods;
import com.example.entity.Orders;
import com.example.mapper.OrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 订单信息业务处理
 **/
@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private GoodsService goodsService;

    @Resource
    private  AddressService addressService;
    /**
     * 新增
     */
    public void add(Orders orders) {

            Integer goodsId = orders.getGoodsId();
        System.out.println("订单id-------"+orders.getId()+orders.getGoodsId());
            Goods goods = goodsService.selectById(goodsId);
            System.out.println("id-------"+goodsId);
            orders.setGoodsName(goods.getName());
            orders.setGoodsImg(goods.getImg());
            orders.setSaleId(goods.getUserId());  //卖家用户ID
            orders.setTotal(goods.getPrice());

            Integer addressId = orders.getAddressId();
            Address address = addressService.selectById(addressId);
            orders.setUserName(address.getUserName());
            orders.setAddress(address.getAddress());
            orders.setPhone(address.getPhone());

            Account currentUser = TokenUtils.getCurrentUser();
            orders.setUserId(currentUser.getId());  //下单人的ID
            orders.setStatus(OrderStatusEnum.NOTPAY.value); // 订单默认是待支付
            orders.setOrderNo(System.currentTimeMillis() + RandomUtil.randomNumbers(7)); // 20位的订单号
            orders.setTime(DateUtil.now());

            ordersMapper.insert(orders);
        }


    /**
     * 删除
     */
    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            ordersMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    /**
     * 根据ID查询
     */
    public Orders selectById(Integer id) {

        return ordersMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    /**
     * 分页查询
     */
    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

}
package com.lxf.eas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxf.eas.entity.TOrder;
import com.lxf.eas.model.response.OrderUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Classname TOrderMapper
 * @Description
 * @Date 2022/7/1 11:44
 * @Author lxf
 */
public interface TOrderMapper extends BaseMapper<TOrder> {

    @Select("select SUM(order_id) as sums from t_order where user_id =#{userId}")
    public Integer getSum(@Param("userId") Integer userId);
    @Select("select tr.*,su.name,su.username from t_order tr  left join sys_user su on tr.user_id = su.user_id where su.user_id = #{userId} ")
    public List<OrderUser> getOrderUser(@Param("userId") Integer userId);
}

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxf.eas.EASApplication;
import com.lxf.eas.entity.TOrder;
import com.lxf.eas.mapper.TOrderMapper;
import com.lxf.eas.model.response.OrderUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Classname ApplicationTest
 * @Description
 * @Date 2022/6/30 17:32
 * @Author lxf
 */
@SpringBootTest(classes = EASApplication.class)
@Slf4j
public class ApplicationTest {


    @Autowired
    private TOrderMapper mapper;

    @Test
    public void testCreateOder(){
        for (int i = 0; i < 5; i++) {
             TOrder tOrder = new TOrder();
            tOrder.setOrderId(i)
                    .setCloumn("Cloumn"+i)
                    .setUserId(i)
                    .setCreateTime(1656648352691L);
            mapper.insert(tOrder);
        }

        for (int i = 0; i < 5; i++) {
            TOrder tOrder = new TOrder();
            tOrder.setOrderId(i)
                    .setCloumn("Cloumn"+i)
                    .setUserId(i)
                    .setCreateTime(1655740800000L);
            mapper.insert(tOrder);
        }

        for (int i = 0; i < 5; i++) {
            TOrder tOrder = new TOrder();
            tOrder.setOrderId(i)
                    .setCloumn("Cloumn"+i)
                    .setUserId(i)
                    .setCreateTime(1653062400000L);
            mapper.insert(tOrder);
        }
    }

    @Test
    public void testSearch01(){
         QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("create_time",1653148800000L);
         List<TOrder> tOrders = mapper.selectList(wrapper);
        tOrders.forEach(System.out::println);
    }

    @Test
    public void testSearch011(){
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
/*        wrapper.ge("create_time",1643148800000L);
        wrapper.lt("create_time",1658148800000L);*/
        wrapper.between("create_time",1651334400000L,1651680000000L);
        List<TOrder> tOrders = mapper.selectList(wrapper);
        tOrders.forEach(System.out::println);
    }

    @Test
    public void testSearch02(){
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",0);
        List<TOrder> tOrders = mapper.selectList(wrapper);
        tOrders.forEach(System.out::println);
    }

    @Test
    public void testSearch03(){
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",0);
        final Integer count = mapper.selectCount(wrapper);
        System.out.println(count);
    }

    @Test
    public void testSearch04(){
        System.out.println(mapper.getSum(3));
    }

    @Test
    public void testSearch05(){
         List<OrderUser> orderUser = mapper.getOrderUser(1);
         orderUser.forEach(System.out::println);
    }


}

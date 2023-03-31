package com.lxf.eas.config;

import com.alibaba.nacos.common.utils.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * @Classname OrderPreciseAlgorithm
 * @Description 自定义sharding分表策略
 * @Date 2022/7/1 11:14
 * @Author lxf
 */
//@Component
public class OrderPreciseAlgorithm implements PreciseShardingAlgorithm<Long> {


    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> preciseShardingValue) {

        if(Objects.nonNull(preciseShardingValue)){
            Long value = preciseShardingValue.getValue();
             String ym = getYM(value);
            for (String tableName : tableNames) {
                if(tableName.endsWith(ym)){
                    return tableName;
                }
            }
        }
        return null;
    }

    private String getYM(Long time){
         Date date = new Date(time);
         SimpleDateFormat monthOfYear = new SimpleDateFormat("yyyyMM");
         return monthOfYear.format(date);
    }

    public static void main(String[] args) {
        Date date = new Date();
        SimpleDateFormat monthOfYear = new SimpleDateFormat("yyyyMM");
        System.out.println(monthOfYear.format(date));
    }
}

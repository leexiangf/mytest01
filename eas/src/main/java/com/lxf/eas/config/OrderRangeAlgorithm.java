package com.lxf.eas.config;

import com.google.common.collect.Range;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname OrderRangeAlgorithm
 * @Description 自定义sharding分表策略
 * @Date 2022/7/1 11:14
 * @Author lxf
 *
 */
//@Component
public class OrderRangeAlgorithm implements RangeShardingAlgorithm<Long>{


    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        HashSet<String> tables = new HashSet<>();
        final String logicTableName = shardingValue.getLogicTableName();
        final Range<Long> range = shardingValue.getValueRange();
        final Long upperPoint = range.upperEndpoint();
        final Long endPoint = range.lowerEndpoint();
        if(Objects.nonNull(upperPoint)&&Objects.nonNull(endPoint)&&upperPoint<endPoint){
            throw new IllegalArgumentException("时间参数错误");
        }
        if(Objects.isNull(upperPoint)&&Objects.isNull(endPoint)){
            return availableTargetNames;
        }
        //起始年月
        int startYM;
        //结束年月
        int endYM;
        //如果结束年月为空，取availableTargetNames最大值
        if(Objects.isNull(upperPoint)){
            Optional<Integer> optional = availableTargetNames.stream().map(t -> Integer.valueOf(t.replaceAll(shardingValue.getLogicTableName(), "")))
                    .sorted(Comparator.reverseOrder()).findFirst();
            endYM=optional.orElse(202401);
        }else {
            endYM = Integer.valueOf(getYM(upperPoint));
        }
        //如果起始年月为空，取availableTargetNames最小值
        if(Objects.isNull(endPoint)){
            Optional<Integer> optional = availableTargetNames.stream().map(t -> Integer.valueOf(t.replaceAll(shardingValue.getLogicTableName(), "")))
                    .sorted().findFirst();
            startYM= optional.orElse(202205);
        }else {
            startYM = Integer.valueOf(getYM(endPoint));
        }

        //起始年
        Integer startY = Integer.valueOf(getY(endPoint));
        //起始月
        Integer startM = Integer.valueOf(getM(endPoint));
        //如果起始年月和结束年月为一个月，则判断是否有这个月，直接返回
        if(startYM==endYM){
            if(availableTargetNames.contains(logicTableName+startYM)){
                tables.add(logicTableName+startYM);
                return tables;
            }else {
                return availableTargetNames;
            }
        }
        Integer tempYM =startYM;
        while (tempYM<=endYM){
            if(availableTargetNames.contains(logicTableName+tempYM)){
                tables.add(logicTableName+tempYM);
            }
            //跨年+1，重置年月日
            if(startM==12){
                startY+=1;
                startM=1;
                tempYM=Integer.valueOf(String.valueOf(startY)+"0"+String.valueOf(startM));
            }else{
                tempYM+=1;
                startM+=1;
            }
        }
        return tables;
    }


/*    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        if(Objects.nonNull(shardingValue)){
            Long value = shardingValue.getValue();
            String ym = getYM(value);
            for (String tableName : availableTargetNames) {
                if(tableName.endsWith(ym)){
                    return tableName;
                }
            }
        }
        return null;
    }*/

    private String getYM(Long time){
         Date date = new Date(time);
         SimpleDateFormat monthOfYear = new SimpleDateFormat("yyyyMM");
         return monthOfYear.format(date);
    }
    private String getY(Long time){
        Date date = new Date(time);
        SimpleDateFormat monthOfYear = new SimpleDateFormat("yyyy");
        return monthOfYear.format(date);
    }
    private String getM(Long time){
        Date date = new Date(time);
        SimpleDateFormat monthOfYear = new SimpleDateFormat("MM");
        return monthOfYear.format(date);
    }

    public static void main(String[] args) {
         List<String> tables = Arrays.asList("t_order202208","t_order202205", "t_order202206", "t_order202207");
         List<Integer> integerList = tables.stream().map(t -> Integer.valueOf(t.replaceAll("t_order", "")))
                 .sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        System.out.println(integerList.stream().findFirst().orElse(202201));
        //integerList.forEach(System.out::println);
    }


    public OrderRangeAlgorithm() {
    }
}

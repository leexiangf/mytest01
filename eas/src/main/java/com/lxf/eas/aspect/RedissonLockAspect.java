//package com.lxf.eas.aspect;
//
//import com.lxf.common.annotation.RedissonLock;
//import com.lxf.common.exception.BadException;
//import com.lxf.eas.service.LockService;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
//import org.springframework.core.annotation.Order;
//import org.springframework.expression.EvaluationContext;
//import org.springframework.expression.ExpressionParser;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.expression.spel.support.StandardEvaluationContext;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.lang.reflect.Method;
//
///**
// * redisson 上锁切面
// * @Order 设置执行范围比@Transactional 大
// */
//@Aspect
//@Component
//@Order(0)
//@Slf4j
//public class RedissonLockAspect {
//
//    @Resource
//    private LockService lockService;
//    private ExpressionParser spelExpressionParser = new SpelExpressionParser();
//    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
//
//    /**
//     * 解析el表达式
//     *
//     * @param point
//     * @param elStr
//     * @return
//     */
//    private String elParser(ProceedingJoinPoint point, String elStr) {
//        // TODO 判定el表达式不完善
//        if(elStr == null || !(elStr.startsWith("#") || elStr.startsWith("$"))){
//            return elStr;
//        }
//        Object[] args = point.getArgs();
//        String[] params = discoverer.getParameterNames(((MethodSignature) point.getSignature()).getMethod());
//        EvaluationContext context = new StandardEvaluationContext();
//        for (int len = 0; len < params.length; len++) {
//            context.setVariable(params[len], args[len]);
//        }
//        return spelExpressionParser.parseExpression(elStr).getValue(context, String.class);
//    }
//    /**
//     * 定义切点
//     * execution([权限修饰符] [返回值类型] [简单类名/全类名] [方法名]([参数列表]))
//     */
//    //@Pointcut(value = "@annotation(com.promotion.common.annotation.RedissonLock)")
//    public void lock() {
//    }
//
//    /**
//     * 环绕
//     * @param joinPoint
//     */
//    @Around(value = "@annotation(com.promotion.common.annotation.RedissonLock)")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.warn("lock ......");
//        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
//        //log.warn("redissonLock : {}",redissonLock.key());
//        String elParser = elParser(joinPoint, redissonLock.key());
//        log.warn("redissonLock elParser key : {}",elParser);
//        String key = redissonLock.preKey() + elParser;
//        return lockService.executeTryLock(key,redissonLock.tryTime(),
//                redissonLock.lockTime(),redissonLock.timeUnit(),
//                joinPoint::proceed,
//                ()-> new BadException(redissonLock.code(),redissonLock.msg()));
//    }
//
//
//}

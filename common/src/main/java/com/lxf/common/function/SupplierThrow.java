package com.lxf.common.function;


/**
 * @Classname SupplierThrow
 * @Description
 * @Date 2023/7/24 16:39
 * @Author lxf
 */
@FunctionalInterface
public interface SupplierThrow<T> {
    /**
     * Supplier support Throwable Function
     * @return
     * @throws Throwable
     */
    T get() throws Throwable;
}

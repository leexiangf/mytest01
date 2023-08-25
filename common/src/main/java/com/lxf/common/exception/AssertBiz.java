package com.lxf.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.function.Supplier;

/**
 *
 * @author hh
 */
@Slf4j
public class AssertBiz {

    private AssertBiz() {
    }

    public static void isTrue(boolean expression, String message, Object...args) {
        if (!expression) {
            if (args != null && args.length > 0) {
                message = String.format(message.replace("{}","%s"),args);
            }
            log.warn(message);
            throw new BadException(ResponseCodes.FAIL.getCode(),message);
        }
    }

    public static void notTrue(boolean expression, String message, Object...args) {
        if (expression) {
            if (args != null && args.length > 0) {
                message = String.format(message.replace("{}","%s"),args);
            }
            log.warn(message);
            throw new BadException(ResponseCodes.FAIL.getCode(),message);
        }
    }

    public static void notNull(@Nullable Object object, String message, Object...args) {
        if (object == null) {
            if (args != null && args.length > 0) {
                message = String.format(message.replace("{}","%s"),args);
            }
            log.warn(message);
            throw new BadException(ResponseCodes.FAIL.getCode(),message);
        }
    }

    public static void hasText(@Nullable String text, String message, Object...args) {
        if (StringUtils.isBlank(text)) {
            if (args != null && args.length > 0) {
                message = String.format(message.replace("{}","%s"),args);
            }
            log.warn(message);
            throw new BadException(ResponseCodes.FAIL.getCode(),message);
        }
    }

    public static void notEmpty(@Nullable Object[] array, String message, Object...args) {
        if (ObjectUtils.isEmpty(array)) {
            if (args != null && args.length > 0) {
                message = String.format(message.replace("{}","%s"),args);
            }
            log.warn(message);
            throw new BadException(ResponseCodes.FAIL.getCode(),message);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String message, Object...args) {
        if (CollectionUtils.isEmpty(collection)) {
            if (args != null && args.length > 0) {
                message = String.format(message.replace("{}","%s"),args);
            }
            log.warn(message);
            throw new BadException(ResponseCodes.FAIL.getCode(),message);
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, String message, Object...args) {
        if (!CollectionUtils.isEmpty(collection)) {
            if (args != null && args.length > 0) {
                message = String.format(message.replace("{}","%s"),args);
            }
            log.warn(message);
            throw new BadException(ResponseCodes.FAIL.getCode(),message);
        }
    }

    public static void isNull(@Nullable Object object, String message, Object...args) {
        if (object != null) {
            if (args != null && args.length > 0) {
                message = String.format(message.replace("{}","%s"),args);
            }
            log.warn(message);
            throw new BadException(ResponseCodes.FAIL.getCode(),message);
        }
    }

    @Nullable
    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return messageSupplier != null ? (String)messageSupplier.get() : null;
    }
}

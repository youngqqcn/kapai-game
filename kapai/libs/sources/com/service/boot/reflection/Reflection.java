package com.service.boot.reflection;

import java.lang.reflect.Field;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* compiled from: Reflection.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��$\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\bÆ\u0002\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001a\u0010\u0006\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0001H\u0007J\"\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00012\b\u0010\f\u001a\u0004\u0018\u00010\u0001H\u0007R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n��¨\u0006\r"}, d2 = {"Lcom/service/boot/reflection/Reflection;", "", "()V", "LOGGER", "Lorg/slf4j/Logger;", "kotlin.jvm.PlatformType", "getFieldValue", "field", "Ljava/lang/reflect/Field;", "target", "setFieldValue", "", "value", "common"})
/* loaded from: kapai-common.jar:com/service/boot/reflection/Reflection.class */
public final class Reflection {
    @NotNull
    public static final Reflection INSTANCE = new Reflection();
    private static final Logger LOGGER = LoggerFactory.getLogger(Reflection.class);

    private Reflection() {
    }

    @JvmStatic
    public static final boolean setFieldValue(@NotNull Field field, @NotNull Object target, @Nullable Object value) {
        String str;
        try {
            field.set(target, value);
            return true;
        } catch (IllegalAccessException e) {
            Logger logger = LOGGER;
            Object[] objArr = new Object[5];
            objArr[0] = field.getName();
            objArr[1] = field.getType().getName();
            objArr[2] = target.getClass().getName();
            objArr[3] = value;
            if (value != null) {
                Class<?> cls = value.getClass();
                if (cls != null) {
                    str = cls.getName();
                    objArr[4] = str;
                    logger.error("反射设置值错误！ field: {}, type: {}, object: {}, value: {}, valueType: {}", objArr);
                    return false;
                }
            }
            str = null;
            objArr[4] = str;
            logger.error("反射设置值错误！ field: {}, type: {}, object: {}, value: {}, valueType: {}", objArr);
            return false;
        }
    }

    @JvmStatic
    @Nullable
    public static final Object getFieldValue(@NotNull Field field, @NotNull Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            LOGGER.error("反射获取值错误！field: {}, type: {}, object: {}", new Object[]{field.getName(), field.getType().getName(), target.getClass().getName()});
            return null;
        }
    }
}

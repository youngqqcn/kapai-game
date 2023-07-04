package com.service.boot.converter.enums;

import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/* loaded from: kapai-core.jar:com/service/boot/converter/enums/EnumIntegerConverterFactory.class */
public class EnumIntegerConverterFactory implements ConverterFactory<Integer, EnumCode> {
    private static final Map<Class<?>, Converter<Integer, EnumCode>> CONVERTERS = new HashMap();

    public <T extends EnumCode> Converter<Integer, T> getConverter(Class<T> targetType) {
        Converter converter = CONVERTERS.get(targetType);
        if (converter == null) {
            converter = new EnumIntegerConverter(targetType);
            CONVERTERS.put(targetType, converter);
        }
        return (Converter<Integer, T>) converter;
    }

    /* loaded from: kapai-core.jar:com/service/boot/converter/enums/EnumIntegerConverterFactory$EnumIntegerConverter.class */
    public static class EnumIntegerConverter<T extends EnumCode<?>> implements Converter<Integer, T> {
        private final Class<T> clazz;
        private final Map<Integer, T> values;

        public EnumIntegerConverter(Class<T> targetType) {
            this.clazz = targetType;
            T[] enums = this.clazz.getEnumConstants();
            this.values = new HashMap(enums.length);
            for (T e : enums) {
                Object value = e.getValue();
                if (value instanceof Integer) {
                    this.values.put((Integer) value, e);
                } else if (value instanceof String) {
                    try {
                        this.values.put(Integer.valueOf(Integer.parseInt((String) value)), e);
                    } catch (Exception e2) {
                    }
                }
            }
        }

        public T convert(Integer source) {
            T t = this.values.get(source);
            if (t != null) {
                return t;
            }
            System.err.println("参数枚举转化错误！枚举类【" + this.clazz.getSimpleName() + "】, 转换的值为：【" + source + "】, 默认值【" + t + "】");
            return t;
        }
    }
}

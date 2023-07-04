package com.service.boot.converter.enums;

import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/* loaded from: kapai-core.jar:com/service/boot/converter/enums/EnumStringConverterFactory.class */
public class EnumStringConverterFactory implements ConverterFactory<String, EnumCode> {
    private static final Map<Class<?>, Converter<String, EnumCode>> CONVERTERS = new HashMap();

    public <T extends EnumCode> Converter<String, T> getConverter(Class<T> targetType) {
        Converter converter = CONVERTERS.get(targetType);
        if (converter == null) {
            converter = new EnumStringConverter(targetType);
            CONVERTERS.put(targetType, converter);
        }
        return (Converter<String, T>) converter;
    }

    /* loaded from: kapai-core.jar:com/service/boot/converter/enums/EnumStringConverterFactory$EnumStringConverter.class */
    public static class EnumStringConverter<T extends EnumCode<?>> implements Converter<String, T> {
        private final Class<T> clazz;
        private final Map<String, T> values;

        public EnumStringConverter(Class<T> targetType) {
            this.clazz = targetType;
            T[] enums = this.clazz.getEnumConstants();
            this.values = new HashMap(enums.length);
            for (T e : enums) {
                Object value = e.getValue();
                if (value instanceof String) {
                    this.values.put((String) value, e);
                } else {
                    this.values.put(value.toString(), e);
                }
            }
        }

        public T convert(String source) {
            T t = this.values.get(source);
            if (t != null) {
                return t;
            }
            System.err.println("参数枚举转化错误！枚举类【" + this.clazz.getSimpleName() + "】, 转换的值为：【" + source + "】, 默认值【" + t + "】");
            return null;
        }
    }
}

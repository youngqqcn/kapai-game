package com.service.boot.converter.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/* loaded from: kapai-core.jar:com/service/boot/converter/enums/EnumCodeConverterFactory.class */
public class EnumCodeConverterFactory implements ConverterFactory<EnumCode, Object> {
    private static final EnumCodeConverter CONVERTER = new EnumCodeConverter();

    public <T> Converter<EnumCode, T> getConverter(Class<T> targetType) {
        return CONVERTER;
    }

    /* loaded from: kapai-core.jar:com/service/boot/converter/enums/EnumCodeConverterFactory$EnumCodeConverter.class */
    public static class EnumCodeConverter<T extends EnumCode<?>> implements Converter<T, Object> {
        /* JADX WARN: Multi-variable type inference failed */
        public /* bridge */ /* synthetic */ Object convert(Object source) {
            return convert((EnumCodeConverter<T>) ((EnumCode) source));
        }

        public Object convert(T source) {
            return source.getValue();
        }
    }
}

package com.service.boot.converter.enums;

import com.service.boot.converter.enums.EnumCode;
import jakarta.persistence.AttributeConverter;
import java.lang.Enum;
import java.util.HashMap;
import java.util.Map;

/* loaded from: kapai-core.jar:com/service/boot/converter/enums/EnumCodeConverter.class */
public abstract class EnumCodeConverter<E extends Enum<E> & EnumCode<V>, V> implements AttributeConverter<E, V> {
    private final Class<E> clazz;
    private final Map<V, E> values;

    /* JADX WARN: Multi-variable type inference failed */
    public EnumCodeConverter(Class<E> clazz) {
        this.clazz = clazz;
        Enum[] enumArr = (Enum[]) clazz.getEnumConstants();
        this.values = new HashMap(enumArr.length);
        for (Enum r0 : enumArr) {
            this.values.put(((EnumCode) r0).getValue(), r0);
        }
    }

    /* JADX WARN: Incorrect types in method signature: (TE;)TV; */
    public Object convertToDatabaseColumn(Enum attribute) {
        if (attribute != null) {
            return ((EnumCode) attribute).getValue();
        }
        return null;
    }

    /* JADX WARN: Incorrect return type in method signature: (TV;)TE; */
    /* renamed from: convertToEntityAttribute */
    public Enum m43convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        }
        Enum r0 = (Enum) this.values.get(dbData);
        if (r0 != null) {
            return r0;
        }
        throw new UnsupportedOperationException("JPA枚举转化错误！枚举类【" + this.clazz.getSimpleName() + "】, 转换的值为：【" + dbData + "】");
    }
}

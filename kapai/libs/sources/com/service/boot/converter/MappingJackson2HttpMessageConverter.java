package com.service.boot.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.boot.json.JSON;
import com.service.boot.model.ApiResponse;
import java.lang.reflect.Type;
import kotlin.Metadata;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;

/* compiled from: MappingJackson2HttpMessageConverter.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��8\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010��\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\u0018��2\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J&\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0002\b\u0003\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\"\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n��¨\u0006\u0012"}, d2 = {"Lcom/service/boot/converter/MappingJackson2HttpMessageConverter;", "Lorg/springframework/http/converter/json/MappingJackson2HttpMessageConverter;", "()V", "mDefaultObjectMapper", "Lcom/fasterxml/jackson/databind/ObjectMapper;", "read", "", "type", "Ljava/lang/reflect/Type;", "contextClass", "Ljava/lang/Class;", "inputMessage", "Lorg/springframework/http/HttpInputMessage;", "writeInternal", "", "t", "outputMessage", "Lorg/springframework/http/HttpOutputMessage;", "core"})
/* loaded from: kapai-core.jar:com/service/boot/converter/MappingJackson2HttpMessageConverter.class */
public final class MappingJackson2HttpMessageConverter extends org.springframework.http.converter.json.MappingJackson2HttpMessageConverter {
    @NotNull
    private final ObjectMapper mDefaultObjectMapper = this.defaultObjectMapper;

    protected void writeInternal(@NotNull Object t, @Nullable Type type, @NotNull HttpOutputMessage outputMessage) {
        this.defaultObjectMapper = t instanceof ApiResponse ? JSON.INSTANCE.getObjectMapper() : this.mDefaultObjectMapper;
        super.writeInternal(t, type, outputMessage);
    }

    @NotNull
    public Object read(@NotNull Type type, @Nullable Class<?> contextClass, @NotNull HttpInputMessage inputMessage) {
        this.defaultObjectMapper = StringsKt.startsWith$default(type.getTypeName(), "com.service", false, 2, (Object) null) ? JSON.INSTANCE.getObjectMapper() : this.mDefaultObjectMapper;
        return super.read(type, contextClass, inputMessage);
    }
}

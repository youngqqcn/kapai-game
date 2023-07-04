package com.service.boot.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializerBase;
import com.service.boot.ExtCoreKt;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* compiled from: JSON.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��P\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n��\n\u0002\u0010$\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\tH\u0007J*\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0010\"\u0004\b��\u0010\u00112\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0015H\u0007J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0007J,\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0010\"\u0004\b��\u0010\u00112\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0015H\u0007J\u001c\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00010\u001a2\u0006\u0010\u0017\u001a\u00020\u0018H\u0007JD\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u0002H\u001b\u0012\u0004\u0012\u0002H\u001c0\u001a\"\u0004\b��\u0010\u001b\"\u0004\b\u0001\u0010\u001c2\u0006\u0010\u0017\u001a\u00020\u00182\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u001b0\u00152\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u001c0\u0015H\u0007J\u0012\u0010\u001f\u001a\u0004\u0018\u00010 2\u0006\u0010\u0012\u001a\u00020\u0013H\u0007J+\u0010\u001f\u001a\u0004\u0018\u0001H\u0011\"\u0004\b��\u0010\u00112\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0015H\u0007¢\u0006\u0002\u0010!J\u0012\u0010\u001f\u001a\u0004\u0018\u00010 2\u0006\u0010\u0017\u001a\u00020\u0018H\u0007J-\u0010\u001f\u001a\u0004\u0018\u0001H\u0011\"\u0004\b��\u0010\u00112\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0015H\u0007¢\u0006\u0002\u0010\"J\u0010\u0010#\u001a\u00020\u00182\u0006\u0010$\u001a\u00020\u0001H\u0007J\u0010\u0010%\u001a\u00020\u00182\u0006\u0010$\u001a\u00020\u0001H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n��R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004¢\u0006\u0002\n��R\u001b\u0010\b\u001a\u00020\t8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000b¨\u0006&"}, d2 = {"Lcom/service/boot/json/JSON;", "", "()V", "DATE_FORMAT", "Ljava/text/SimpleDateFormat;", "LOGGER", "Lorg/slf4j/Logger;", "kotlin.jvm.PlatformType", "objectMapper", "Lcom/fasterxml/jackson/databind/ObjectMapper;", "getObjectMapper", "()Lcom/fasterxml/jackson/databind/ObjectMapper;", "objectMapper$delegate", "Lkotlin/Lazy;", "createDefaultObjectMapper", "parseArray", "", "T", "stream", "Ljava/io/InputStream;", "clazz", "Ljava/lang/Class;", "Lcom/service/boot/json/JSONArray;", "json", "", "parseMap", "", "K", "V", "keyClazz", "valueClazz", "parseObject", "Lcom/service/boot/json/JSONObject;", "(Ljava/io/InputStream;Ljava/lang/Class;)Ljava/lang/Object;", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", "toFormatJSONString", "any", "toJSONString", "common"})
/* loaded from: kapai-common.jar:com/service/boot/json/JSON.class */
public final class JSON {
    @NotNull
    public static final JSON INSTANCE = new JSON();
    private static final Logger LOGGER = LoggerFactory.getLogger(JSON.class);
    @NotNull
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @NotNull
    private static final Lazy objectMapper$delegate = LazyKt.lazy(new Function0<ObjectMapper>() { // from class: com.service.boot.json.JSON$objectMapper$2
        @NotNull
        /* renamed from: invoke */
        public final ObjectMapper m49invoke() {
            return JSON.createDefaultObjectMapper();
        }
    });

    private JSON() {
    }

    @NotNull
    public final ObjectMapper getObjectMapper() {
        return (ObjectMapper) objectMapper$delegate.getValue();
    }

    @JvmStatic
    @NotNull
    public static final ObjectMapper createDefaultObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(DATE_FORMAT);
        om.setTimeZone(TimeZone.getDefault());
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Module simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(BigDecimal.class, new ToStringSerializerBase(BigDecimal.class) { // from class: com.service.boot.json.JSON$createDefaultObjectMapper$1$1
            @NotNull
            public String valueToString(@NotNull Object value) {
                Intrinsics.checkNotNull(value, "null cannot be cast to non-null type java.math.BigDecimal");
                return ExtCoreKt.number$default((BigDecimal) value, null, null, 3, null);
            }
        });
        om.registerModule(simpleModule);
        return om;
    }

    @JvmStatic
    @NotNull
    public static final <K, V> Map<K, V> parseMap(@NotNull String json, @NotNull Class<K> keyClazz, @NotNull Class<V> valueClazz) {
        Map<K, V> emptyMap;
        try {
            emptyMap = (Map) INSTANCE.getObjectMapper().readValue(json, INSTANCE.getObjectMapper().getTypeFactory().constructMapType(HashMap.class, keyClazz, valueClazz));
        } catch (Exception e) {
            LOGGER.error("JSON解析异常！", e);
            emptyMap = MapsKt.emptyMap();
        }
        return emptyMap;
    }

    @JvmStatic
    @NotNull
    public static final Map<String, Object> parseMap(@NotNull String json) {
        JSON json2 = INSTANCE;
        return parseMap(json, String.class, Object.class);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @JvmStatic
    @Nullable
    public static final <T> T parseObject(@Nullable String json, @NotNull Class<T> clazz) {
        T t;
        try {
            t = INSTANCE.getObjectMapper().readValue(json, clazz);
        } catch (Exception e) {
            LOGGER.error("JSON解析异常！", e);
            t = null;
        }
        return t;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @JvmStatic
    @Nullable
    public static final <T> T parseObject(@NotNull InputStream stream, @NotNull Class<T> clazz) {
        T t;
        try {
            t = INSTANCE.getObjectMapper().readValue(stream, clazz);
        } catch (Exception e) {
            LOGGER.error("JSON解析异常！", e);
            t = null;
        }
        return t;
    }

    @JvmStatic
    @Nullable
    public static final JSONObject parseObject(@NotNull String json) {
        JSON json2 = INSTANCE;
        return (JSONObject) parseObject(json, JSONObject.class);
    }

    @JvmStatic
    @Nullable
    public static final JSONObject parseObject(@NotNull InputStream stream) {
        JSONObject jSONObject;
        try {
            jSONObject = (JSONObject) INSTANCE.getObjectMapper().readValue(stream, JSONObject.class);
        } catch (Exception e) {
            LOGGER.error("JSON解析异常！", e);
            jSONObject = null;
        }
        return jSONObject;
    }

    @JvmStatic
    @NotNull
    public static final <T> List<T> parseArray(@Nullable String json, @NotNull Class<T> clazz) {
        List<T> emptyList;
        try {
            emptyList = (List) INSTANCE.getObjectMapper().readValue(json, INSTANCE.getObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, clazz));
        } catch (Exception e) {
            LOGGER.error("JSON解析异常！", e);
            emptyList = CollectionsKt.emptyList();
        }
        return emptyList;
    }

    @JvmStatic
    @Nullable
    public static final JSONArray parseArray(@NotNull String json) {
        JSON json2 = INSTANCE;
        return (JSONArray) parseObject(json, JSONArray.class);
    }

    @JvmStatic
    @NotNull
    public static final <T> List<T> parseArray(@NotNull InputStream stream, @NotNull Class<T> clazz) {
        List<T> emptyList;
        try {
            emptyList = (List) INSTANCE.getObjectMapper().readValue(stream, INSTANCE.getObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, clazz));
        } catch (Exception e) {
            LOGGER.error("JSON解析异常！", e);
            emptyList = CollectionsKt.emptyList();
        }
        return emptyList;
    }

    @JvmStatic
    @NotNull
    public static final String toJSONString(@NotNull Object any) {
        String str;
        try {
            str = INSTANCE.getObjectMapper().writeValueAsString(any);
        } catch (Exception e) {
            LOGGER.error("JSON序列化异常！", e);
            str = "";
        }
        return str;
    }

    @JvmStatic
    @NotNull
    public static final String toFormatJSONString(@NotNull Object any) {
        String str;
        try {
            str = INSTANCE.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(any);
        } catch (Exception e) {
            LOGGER.error("JSON序列化异常！", e);
            str = "";
        }
        return str;
    }
}

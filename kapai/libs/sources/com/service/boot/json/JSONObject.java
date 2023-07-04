package com.service.boot.json;

import com.service.boot.common.NumberUtils;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JSONObject.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n��\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u0004\n��\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0004\u0018��2\"\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0001j\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u0003`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\b\u001a\u00020\u0002J\u0015\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\b\u001a\u00020\u0002¢\u0006\u0002\u0010\rJ\u000e\u0010\u000e\u001a\u00020\f2\u0006\u0010\b\u001a\u00020\u0002J\u0015\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\b\u001a\u00020\u0002¢\u0006\u0002\u0010\u0011J\u000e\u0010\u0012\u001a\u00020\u00102\u0006\u0010\b\u001a\u00020\u0002J\u0015\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\b\u001a\u00020\u0002¢\u0006\u0002\u0010\u0015J\u000e\u0010\u0016\u001a\u00020\u00142\u0006\u0010\b\u001a\u00020\u0002J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\b\u001a\u00020\u0002J\u0010\u0010\u0019\u001a\u0004\u0018\u00010��2\u0006\u0010\b\u001a\u00020\u0002J\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u00022\u0006\u0010\b\u001a\u00020\u0002J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u00022\u0006\u0010\b\u001a\u00020\u0002J\u0015\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\b\u001a\u00020\u0002¢\u0006\u0002\u0010\u001eJ\u000e\u0010\u001f\u001a\u00020\u001d2\u0006\u0010\b\u001a\u00020\u0002J/\u0010 \u001a\u0004\u0018\u0001H!\"\b\b��\u0010!*\u00020\"2\u0006\u0010\b\u001a\u00020\u00022\f\u0010#\u001a\b\u0012\u0004\u0012\u0002H!0$H\u0002¢\u0006\u0002\u0010%J\u0010\u0010&\u001a\u0004\u0018\u00010\u00022\u0006\u0010\b\u001a\u00020\u0002J\u000e\u0010'\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002J(\u0010(\u001a\b\u0012\u0004\u0012\u0002H!0)\"\u0004\b��\u0010!2\u0006\u0010\b\u001a\u00020\u00022\f\u0010#\u001a\b\u0012\u0004\u0012\u0002H!0$J)\u0010*\u001a\u0004\u0018\u0001H!\"\u0004\b��\u0010!2\u0006\u0010\b\u001a\u00020\u00022\f\u0010#\u001a\b\u0012\u0004\u0012\u0002H!0$¢\u0006\u0002\u0010+J\u0006\u0010,\u001a\u00020\u0002¨\u0006-"}, d2 = {"Lcom/service/boot/json/JSONObject;", "Ljava/util/HashMap;", "", "", "Lkotlin/collections/HashMap;", "()V", "getBigDecimalValue", "Ljava/math/BigDecimal;", "key", "getBooleanValue", "", "getDouble", "", "(Ljava/lang/String;)Ljava/lang/Double;", "getDoubleValue", "getFloat", "", "(Ljava/lang/String;)Ljava/lang/Float;", "getFloatValue", "getInteger", "", "(Ljava/lang/String;)Ljava/lang/Integer;", "getIntegerValue", "getJSONArray", "Lcom/service/boot/json/JSONArray;", "getJSONObject", "getJSONString", "getJSONStringValue", "getLong", "", "(Ljava/lang/String;)Ljava/lang/Long;", "getLongValue", "getNumberValue", "T", "", "clazz", "Ljava/lang/Class;", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Number;", "getString", "getStringValue", "parseArray", "", "parseObject", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", "toJSONString", "common"})
/* loaded from: kapai-common.jar:com/service/boot/json/JSONObject.class */
public final class JSONObject extends HashMap<String, Object> {
    public /* bridge */ Object remove(String key) {
        return super.remove((Object) key);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ Object remove(Object key) {
        if (key instanceof String) {
            return remove((String) key);
        }
        return null;
    }

    public /* bridge */ boolean remove(String key, Object value) {
        return super.remove((Object) key, value);
    }

    @Override // java.util.HashMap, java.util.Map
    public final /* bridge */ boolean remove(Object key, Object value) {
        if (key instanceof String) {
            if (value == null) {
            }
            return remove((String) key, value);
        }
        return false;
    }

    public /* bridge */ boolean containsKey(String key) {
        return super.containsKey((Object) key);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ boolean containsKey(Object key) {
        if (key instanceof String) {
            return containsKey((String) key);
        }
        return false;
    }

    public /* bridge */ Object get(String key) {
        return super.get((Object) key);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ Object get(Object key) {
        if (key instanceof String) {
            return get((String) key);
        }
        return null;
    }

    public /* bridge */ Object getOrDefault(String key, Object defaultValue) {
        return super.getOrDefault((Object) key, (String) defaultValue);
    }

    @Override // java.util.HashMap, java.util.Map
    public final /* bridge */ Object getOrDefault(Object key, Object defaultValue) {
        return !(key instanceof String) ? defaultValue : getOrDefault((String) key, defaultValue);
    }

    public /* bridge */ int getSize() {
        return super.size();
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ int size() {
        return getSize();
    }

    public /* bridge */ Collection<Object> getValues() {
        return super.values();
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ Collection<Object> values() {
        return getValues();
    }

    public /* bridge */ Set<Map.Entry<String, Object>> getEntries() {
        return super.entrySet();
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ Set<Map.Entry<String, Object>> entrySet() {
        return getEntries();
    }

    public /* bridge */ Set<String> getKeys() {
        return super.keySet();
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ Set<String> keySet() {
        return getKeys();
    }

    @Nullable
    public final JSONObject getJSONObject(@NotNull String key) {
        Object value = get((Object) key);
        if (value != null && (value instanceof Map)) {
            JSONObject obj = new JSONObject();
            obj.putAll((Map) value);
            return obj;
        }
        return null;
    }

    @Nullable
    public final JSONArray getJSONArray(@NotNull String key) {
        Object value = get((Object) key);
        if (value != null && (value instanceof List)) {
            JSONArray array = new JSONArray();
            array.addAll((List) value);
            return array;
        }
        return null;
    }

    private final <T extends Number> T getNumberValue(String key, Class<T> clazz) {
        Object value = get((Object) key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            if (Intrinsics.areEqual(value.getClass(), clazz)) {
                if (value instanceof Number) {
                    return (T) value;
                }
                return null;
            } else if (Intrinsics.areEqual(clazz, Integer.TYPE)) {
                Integer valueOf = Integer.valueOf(((Number) value).intValue());
                if (valueOf instanceof Number) {
                    return valueOf;
                }
                return null;
            } else if (Intrinsics.areEqual(clazz, Long.TYPE)) {
                Long valueOf2 = Long.valueOf(((Number) value).longValue());
                if (valueOf2 instanceof Number) {
                    return valueOf2;
                }
                return null;
            } else if (Intrinsics.areEqual(clazz, Double.TYPE)) {
                Double valueOf3 = Double.valueOf(((Number) value).doubleValue());
                if (valueOf3 instanceof Number) {
                    return valueOf3;
                }
                return null;
            } else if (Intrinsics.areEqual(clazz, Float.TYPE)) {
                Float valueOf4 = Float.valueOf(((Number) value).floatValue());
                if (valueOf4 instanceof Number) {
                    return valueOf4;
                }
                return null;
            } else if (Intrinsics.areEqual(clazz, BigDecimal.class)) {
                BigDecimal bigDecimal = new BigDecimal(String.valueOf(value));
                if (bigDecimal instanceof Number) {
                    return bigDecimal;
                }
                return null;
            } else {
                return null;
            }
        } else if (value instanceof String) {
            return (T) NumberUtils.INSTANCE.parseNumber((String) value, clazz);
        } else {
            if (value instanceof Number) {
                return (T) value;
            }
            return null;
        }
    }

    @Nullable
    public final String getJSONString(@NotNull String key) {
        Object value = get((Object) key);
        if (value != null) {
            if (Intrinsics.areEqual(value.getClass(), String.class)) {
                return (String) value;
            }
            return JSON.toJSONString(value);
        }
        return null;
    }

    @Nullable
    public final String getJSONStringValue(@NotNull String key) {
        Object value = get((Object) key);
        if (value != null) {
            if (Intrinsics.areEqual(value.getClass(), String.class)) {
                return (String) value;
            }
            return JSON.toJSONString(value);
        }
        return "";
    }

    @Nullable
    public final <T> T parseObject(@NotNull String key, @NotNull Class<T> clazz) {
        return (T) JSON.parseObject(getJSONStringValue(key), clazz);
    }

    @NotNull
    public final <T> List<T> parseArray(@NotNull String key, @NotNull Class<T> clazz) {
        return JSON.parseArray(getJSONStringValue(key), clazz);
    }

    @Nullable
    public final String getString(@NotNull String key) {
        Object value = get((Object) key);
        if (value == null) {
            return null;
        }
        return value instanceof String ? (String) value : value.toString();
    }

    @NotNull
    public final String getStringValue(@NotNull String key) {
        Object value = get((Object) key);
        return value == null ? "" : value instanceof String ? (String) value : value.toString();
    }

    @Nullable
    public final Integer getInteger(@NotNull String key) {
        return (Integer) getNumberValue(key, Integer.TYPE);
    }

    public final int getIntegerValue(@NotNull String key) {
        Integer num = (Integer) getNumberValue(key, Integer.TYPE);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    @Nullable
    public final Long getLong(@NotNull String key) {
        return (Long) getNumberValue(key, Long.TYPE);
    }

    public final long getLongValue(@NotNull String key) {
        Long l = (Long) getNumberValue(key, Long.TYPE);
        if (l != null) {
            return l.longValue();
        }
        return 0L;
    }

    @Nullable
    public final Float getFloat(@NotNull String key) {
        return (Float) getNumberValue(key, Float.TYPE);
    }

    public final float getFloatValue(@NotNull String key) {
        Float f = (Float) getNumberValue(key, Float.TYPE);
        if (f != null) {
            return f.floatValue();
        }
        return 0.0f;
    }

    @Nullable
    public final Double getDouble(@NotNull String key) {
        return (Double) getNumberValue(key, Double.TYPE);
    }

    public final double getDoubleValue(@NotNull String key) {
        Double d = (Double) getNumberValue(key, Double.TYPE);
        if (d != null) {
            return d.doubleValue();
        }
        return 0.0d;
    }

    public final boolean getBooleanValue(@NotNull String key) {
        Object value = get((Object) key);
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        if (Intrinsics.areEqual(value, "true") || Intrinsics.areEqual(value, 1) || Intrinsics.areEqual(value, "1")) {
            return true;
        }
        return (Intrinsics.areEqual(value, "false") || Intrinsics.areEqual(value, 0) || Intrinsics.areEqual(value, "0")) ? false : false;
    }

    @NotNull
    public final BigDecimal getBigDecimalValue(@NotNull String key) {
        BigDecimal bigDecimal = (BigDecimal) getNumberValue(key, BigDecimal.class);
        return bigDecimal == null ? BigDecimal.ZERO : bigDecimal;
    }

    @NotNull
    public final String toJSONString() {
        return JSON.toJSONString(this);
    }
}

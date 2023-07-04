package com.service.boot.json;

import java.util.ArrayList;
import java.util.Map;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JSONArray.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\b\n��\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018��2\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u0010\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0006\u001a\u00020\u0007J\u0006\u0010\n\u001a\u00020\t¨\u0006\u000b"}, d2 = {"Lcom/service/boot/json/JSONArray;", "Ljava/util/ArrayList;", "", "()V", "getJSONObject", "Lcom/service/boot/json/JSONObject;", "index", "", "getString", "", "toJSONString", "common"})
/* loaded from: kapai-common.jar:com/service/boot/json/JSONArray.class */
public final class JSONArray extends ArrayList<Object> {
    public /* bridge */ Object removeAt(int p0) {
        return super.remove(p0);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public final /* bridge */ Object remove(int index) {
        return removeAt(index);
    }

    public /* bridge */ int getSize() {
        return super.size();
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ int size() {
        return getSize();
    }

    @Nullable
    public final JSONObject getJSONObject(int index) {
        Object value = get(index);
        if (value != null && (value instanceof Map)) {
            JSONObject obj = new JSONObject();
            obj.putAll((Map) value);
            return obj;
        }
        return null;
    }

    @NotNull
    public final String toJSONString() {
        return JSON.toJSONString(this);
    }

    @Nullable
    public final String getString(int index) {
        Object obj = get(index);
        if (obj instanceof String) {
            return (String) obj;
        }
        return null;
    }
}

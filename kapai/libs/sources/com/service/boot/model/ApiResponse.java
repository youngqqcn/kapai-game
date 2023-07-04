package com.service.boot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ApiResponse.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��\"\n\u0002\u0018\u0002\n\u0002\u0010��\n��\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0086\b\u0018�� \u001e2\u00020\u0001:\u0001\u001eB!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0001\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0006HÆ\u0003J+\u0010\u0017\u001a\u00020��2\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006HÆ\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÖ\u0001J\b\u0010\u001c\u001a\u00020\u0019H\u0007J\t\u0010\u001d\u001a\u00020\u0006HÖ\u0001R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n��\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0001X\u0086\u000e¢\u0006\u000e\n��\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0086\u000e¢\u0006\u000e\n��\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013¨\u0006\u001f"}, d2 = {"Lcom/service/boot/model/ApiResponse;", "", "code", "", "data", "message", "", "(ILjava/lang/Object;Ljava/lang/String;)V", "getCode", "()I", "setCode", "(I)V", "getData", "()Ljava/lang/Object;", "setData", "(Ljava/lang/Object;)V", "getMessage", "()Ljava/lang/String;", "setMessage", "(Ljava/lang/String;)V", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "isSuccessful", "toString", "Companion", "core"})
/* loaded from: kapai-core.jar:com/service/boot/model/ApiResponse.class */
public final class ApiResponse {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private int code;
    @Nullable
    private Object data;
    @Nullable
    private String message;

    public final int component1() {
        return this.code;
    }

    @Nullable
    public final Object component2() {
        return this.data;
    }

    @Nullable
    public final String component3() {
        return this.message;
    }

    @NotNull
    public final ApiResponse copy(int code, @Nullable Object data, @Nullable String message) {
        return new ApiResponse(code, data, message);
    }

    public static /* synthetic */ ApiResponse copy$default(ApiResponse apiResponse, int i, Object obj, String str, int i2, Object obj2) {
        if ((i2 & 1) != 0) {
            i = apiResponse.code;
        }
        if ((i2 & 2) != 0) {
            obj = apiResponse.data;
        }
        if ((i2 & 4) != 0) {
            str = apiResponse.message;
        }
        return apiResponse.copy(i, obj, str);
    }

    @NotNull
    public String toString() {
        return "ApiResponse(code=" + this.code + ", data=" + this.data + ", message=" + this.message + ")";
    }

    public int hashCode() {
        int result = Integer.hashCode(this.code);
        return (((result * 31) + (this.data == null ? 0 : this.data.hashCode())) * 31) + (this.message == null ? 0 : this.message.hashCode());
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof ApiResponse) {
            ApiResponse apiResponse = (ApiResponse) other;
            return this.code == apiResponse.code && Intrinsics.areEqual(this.data, apiResponse.data) && Intrinsics.areEqual(this.message, apiResponse.message);
        }
        return false;
    }

    @JvmStatic
    @NotNull
    public static final ApiResponse success(@Nullable Object data, @Nullable String message) {
        return Companion.success(data, message);
    }

    @JvmStatic
    @NotNull
    public static final ApiResponse error(int code, @Nullable String message) {
        return Companion.error(code, message);
    }

    @JvmStatic
    @NotNull
    public static final ApiResponse error(@Nullable String message) {
        return Companion.error(message);
    }

    public ApiResponse(int code, @Nullable Object data, @Nullable String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public final int getCode() {
        return this.code;
    }

    public final void setCode(int i) {
        this.code = i;
    }

    @Nullable
    public final Object getData() {
        return this.data;
    }

    public final void setData(@Nullable Object obj) {
        this.data = obj;
    }

    @Nullable
    public final String getMessage() {
        return this.message;
    }

    public final void setMessage(@Nullable String str) {
        this.message = str;
    }

    @JsonIgnore
    public final boolean isSuccessful() {
        return this.code == 200;
    }

    /* compiled from: ApiResponse.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"�� \n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\b\n��\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0007J\u0014\u0010\u0003\u001a\u00020\u00042\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0007J \u0010\t\u001a\u00020\u00042\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0007¨\u0006\u000b"}, d2 = {"Lcom/service/boot/model/ApiResponse$Companion;", "", "()V", "error", "Lcom/service/boot/model/ApiResponse;", "code", "", "message", "", "success", "data", "core"})
    /* loaded from: kapai-core.jar:com/service/boot/model/ApiResponse$Companion.class */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        public static /* synthetic */ ApiResponse success$default(Companion companion, Object obj, String str, int i, Object obj2) {
            if ((i & 1) != 0) {
                obj = null;
            }
            if ((i & 2) != 0) {
                str = null;
            }
            return companion.success(obj, str);
        }

        @JvmStatic
        @NotNull
        public final ApiResponse success(@Nullable Object data, @Nullable String message) {
            return new ApiResponse(200, data, message);
        }

        public static /* synthetic */ ApiResponse error$default(Companion companion, int i, String str, int i2, Object obj) {
            if ((i2 & 2) != 0) {
                str = null;
            }
            return companion.error(i, str);
        }

        @JvmStatic
        @NotNull
        public final ApiResponse error(int code, @Nullable String message) {
            return new ApiResponse(code, null, message);
        }

        public static /* synthetic */ ApiResponse error$default(Companion companion, String str, int i, Object obj) {
            if ((i & 1) != 0) {
                str = null;
            }
            return companion.error(str);
        }

        @JvmStatic
        @NotNull
        public final ApiResponse error(@Nullable String message) {
            return new ApiResponse(500, null, message);
        }
    }
}

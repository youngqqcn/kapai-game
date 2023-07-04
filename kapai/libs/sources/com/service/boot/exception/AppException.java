package com.service.boot.exception;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

/* compiled from: AppException.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\b\n��\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0016\u0018��2\u00060\u0001j\u0002`\u0002B\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\bR\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n��\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f¨\u0006\r"}, d2 = {"Lcom/service/boot/exception/AppException;", "Ljava/lang/RuntimeException;", "Lkotlin/RuntimeException;", "code", "", "message", "", "(ILjava/lang/String;)V", "(Ljava/lang/String;)V", "getCode", "()I", "setCode", "(I)V", "core"})
/* loaded from: kapai-core.jar:com/service/boot/exception/AppException.class */
public class AppException extends RuntimeException {
    private int code;

    public final int getCode() {
        return this.code;
    }

    public final void setCode(int i) {
        this.code = i;
    }

    public AppException(int code, @NotNull String message) {
        super(message);
        this.code = 500;
        this.code = code;
    }

    public AppException(@NotNull String message) {
        this(500, message);
    }
}

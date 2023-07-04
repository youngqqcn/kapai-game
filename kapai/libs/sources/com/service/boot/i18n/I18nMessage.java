package com.service.boot.i18n;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/* compiled from: I18nMessage.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��\"\n\u0002\u0018\u0002\n\u0002\u0010��\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\u0018��2\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006J'\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0012\u0010\b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010\t\"\u00020\u0001¢\u0006\u0002\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n��¨\u0006\u000b"}, d2 = {"Lcom/service/boot/i18n/I18nMessage;", "", "source", "Lorg/springframework/context/MessageSource;", "(Lorg/springframework/context/MessageSource;)V", "getMessage", "", "code", "args", "", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "core"})
/* loaded from: kapai-core.jar:com/service/boot/i18n/I18nMessage.class */
public final class I18nMessage {
    @NotNull
    private final MessageSource source;

    public I18nMessage(@NotNull MessageSource source) {
        this.source = source;
    }

    @NotNull
    public final String getMessage(@NotNull String code) {
        return this.source.getMessage(code, (Object[]) null, LocaleContextHolder.getLocale());
    }

    @NotNull
    public final String getMessage(@NotNull String code, @NotNull Object... args) {
        return this.source.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}

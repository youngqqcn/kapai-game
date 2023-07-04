package com.service.boot.common;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: Secret.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��\u001a\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0010\u000e\n��\n\u0002\u0010\u0012\n\u0002\b\u0005\bÆ\u0002\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004H\u0007J\u0018\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004H\u0007J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u0006H\u0007¨\u0006\u000b"}, d2 = {"Lcom/service/boot/common/Secret;", "", "()V", "decrypt", "", "key", "", "text", "encrypt", "to16ByteArray", "array", "common"})
/* loaded from: kapai-common.jar:com/service/boot/common/Secret.class */
public final class Secret {
    @NotNull
    public static final Secret INSTANCE = new Secret();

    private Secret() {
    }

    @JvmStatic
    @NotNull
    public static final String encrypt(@NotNull byte[] key, @NotNull String text) {
        Secret secret = INSTANCE;
        SecretKeySpec secretKey = new SecretKeySpec(to16ByteArray(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, secretKey);
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
        return new String(encoder.encode(cipher.doFinal(bytes)), StandardCharsets.UTF_8);
    }

    @JvmStatic
    @NotNull
    public static final String decrypt(@NotNull byte[] key, @NotNull String text) {
        Secret secret = INSTANCE;
        SecretKeySpec secretKey = new SecretKeySpec(to16ByteArray(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, secretKey);
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
        return new String(cipher.doFinal(decoder.decode(bytes)), StandardCharsets.UTF_8);
    }

    @JvmStatic
    @NotNull
    public static final byte[] to16ByteArray(@NotNull byte[] array) {
        int len = array.length;
        if (len == 16) {
            return array;
        }
        if (len > 16) {
            len = 16;
        }
        return ArraysKt.copyInto(array, new byte[16], 0, 0, len);
    }
}

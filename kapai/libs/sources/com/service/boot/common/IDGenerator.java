package com.service.boot.common;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import java.util.Random;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

/* compiled from: IDGenerator.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��$\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\bÆ\u0002\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u00020\u0004H\u0007J\b\u0010\u0007\u001a\u00020\bH\u0007J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0012\u0010\r\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0007J\u0018\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n��R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n��¨\u0006\u0010"}, d2 = {"Lcom/service/boot/common/IDGenerator;", "", "()V", "CHARS", "", "HI", "generator", "generatorId", "", "getRandomString", "len", "", "getRandomStringHi", "getSMSCode", "warpLen", "str", "common"})
@SourceDebugExtension({"SMAP\nIDGenerator.kt\nKotlin\n*S Kotlin\n*F\n+ 1 IDGenerator.kt\ncom/service/boot/common/IDGenerator\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,74:1\n1#2:75\n*E\n"})
/* loaded from: kapai-common.jar:com/service/boot/common/IDGenerator.class */
public final class IDGenerator {
    @NotNull
    public static final IDGenerator INSTANCE = new IDGenerator();
    @NotNull
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    @NotNull
    private static final String HI = "~!@^&*,.?";

    private IDGenerator() {
    }

    static {
        YitIdHelper.setIdGenerator(new IdGeneratorOptions((short) 1));
    }

    @JvmStatic
    public static final long generatorId() {
        return YitIdHelper.nextId();
    }

    @JvmStatic
    @NotNull
    public static final String generator() {
        IDGenerator iDGenerator = INSTANCE;
        return String.valueOf(generatorId());
    }

    public static /* synthetic */ String getSMSCode$default(int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 6;
        }
        return getSMSCode(i);
    }

    @JvmStatic
    @NotNull
    public static final String getSMSCode(int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int i = 1;
        if (1 <= len) {
            while (true) {
                sb.append(random.nextInt(10));
                if (i == len) {
                    break;
                }
                i++;
            }
        }
        return sb.toString();
    }

    @JvmStatic
    @NotNull
    public static final String getRandomString(int len) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(62);
            sb.append(CHARS.charAt(index));
        }
        return sb.toString();
    }

    @JvmStatic
    @NotNull
    public static final String getRandomStringHi(int len) {
        int size = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@^&*,.?".length();
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(size);
            sb.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@^&*,.?".charAt(index));
        }
        return sb.toString();
    }

    @JvmStatic
    @NotNull
    public static final String warpLen(@NotNull String str, int len) {
        byte b;
        byte[] array = str.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(array, "this as java.lang.String).getBytes(charset)");
        byte[] buffer = new byte[len];
        for (int i = 0; i < len; i++) {
            byte[] bArr = buffer;
            int i2 = i;
            if (i >= 0 && i <= ArraysKt.getLastIndex(array)) {
                b = array[i];
            } else {
                bArr = bArr;
                i2 = i2;
                b = 0;
            }
            bArr[i2] = b;
        }
        return new String(buffer, Charsets.UTF_8);
    }
}

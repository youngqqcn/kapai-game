package com.service.boot.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

/* compiled from: FileCodec.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��J\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0010\u0002\n��\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n��\bÆ\u0002\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J(\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0007J \u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0007J(\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0007J \u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0013H\u0007J(\u0010\u0014\u001a\n \u0016*\u0004\u0018\u00010\u00150\u00152\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0003¨\u0006\u0019"}, d2 = {"Lcom/service/boot/common/FileCodec;", "", "()V", "decodeFile", "", "key", "", "iv", "source", "Ljava/io/File;", "out", "decryptInputStream", "Ljavax/crypto/CipherInputStream;", "inputStream", "Ljava/io/InputStream;", "encodeFile", "encryptOutputStream", "Ljavax/crypto/CipherOutputStream;", "outputStream", "Ljava/io/OutputStream;", "getCipher", "Ljavax/crypto/Cipher;", "kotlin.jvm.PlatformType", "mode", "", "common"})
@SourceDebugExtension({"SMAP\nFileCodec.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileCodec.kt\ncom/service/boot/common/FileCodec\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,37:1\n1#2:38\n*E\n"})
/* loaded from: kapai-common.jar:com/service/boot/common/FileCodec.class */
public final class FileCodec {
    @NotNull
    public static final FileCodec INSTANCE = new FileCodec();

    private FileCodec() {
    }

    @JvmStatic
    public static final void encodeFile(@NotNull byte[] key, @NotNull byte[] iv, @NotNull File source, @NotNull File out) {
        FileCodec fileCodec = INSTANCE;
        CipherOutputStream encryptOutputStream = encryptOutputStream(key, iv, new FileOutputStream(out));
        try {
            CipherOutputStream cos = encryptOutputStream;
            FileInputStream fileInputStream = new FileInputStream(source);
            FileInputStream input = fileInputStream;
            ByteStreamsKt.copyTo$default(input, cos, 0, 2, (Object) null);
            CloseableKt.closeFinally(fileInputStream, (Throwable) null);
            CloseableKt.closeFinally(encryptOutputStream, (Throwable) null);
        } finally {
        }
    }

    @JvmStatic
    public static final void decodeFile(@NotNull byte[] key, @NotNull byte[] iv, @NotNull File source, @NotNull File out) {
        FileOutputStream fileOutputStream = new FileOutputStream(out);
        try {
            FileOutputStream fos = fileOutputStream;
            CipherInputStream decryptInputStream = decryptInputStream(key, iv, new FileInputStream(source));
            CipherInputStream input = decryptInputStream;
            ByteStreamsKt.copyTo$default(input, fos, 0, 2, (Object) null);
            CloseableKt.closeFinally(decryptInputStream, (Throwable) null);
            CloseableKt.closeFinally(fileOutputStream, (Throwable) null);
        } finally {
        }
    }

    @JvmStatic
    @NotNull
    public static final CipherInputStream decryptInputStream(@NotNull byte[] key, @NotNull byte[] iv, @NotNull InputStream inputStream) {
        FileCodec fileCodec = INSTANCE;
        return new CipherInputStream(inputStream, getCipher(2, key, iv));
    }

    @JvmStatic
    @NotNull
    public static final CipherOutputStream encryptOutputStream(@NotNull byte[] key, @NotNull byte[] iv, @NotNull OutputStream outputStream) {
        FileCodec fileCodec = INSTANCE;
        return new CipherOutputStream(outputStream, getCipher(1, key, iv));
    }

    @JvmStatic
    private static final Cipher getCipher(int mode, byte[] key, byte[] iv) {
        Cipher $this$getCipher_u24lambda_u244 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        $this$getCipher_u24lambda_u244.init(mode, new SecretKeySpec(Secret.to16ByteArray(key), "AES"), new IvParameterSpec(Secret.to16ByteArray(iv)));
        return $this$getCipher_u24lambda_u244;
    }
}

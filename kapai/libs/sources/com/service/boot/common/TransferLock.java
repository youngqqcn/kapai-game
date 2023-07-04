package com.service.boot.common;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* compiled from: TransferLock.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��L\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\bÆ\u0002\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J!\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\r\"\u00020\u0005H\u0003¢\u0006\u0002\u0010\u000eJ9\u0010\u000f\u001a\u0004\u0018\u0001H\u0010\"\u0004\b��\u0010\u00102\u0012\u0010\f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\r\"\u00020\u00052\u000e\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0012H\u0007¢\u0006\u0002\u0010\u0013JN\u0010\u0014\u001a\u0004\u0018\u0001H\u0010\"\u0004\b��\u0010\u00102\u0012\u0010\f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\r\"\u00020\u00052#\u0010\u0011\u001a\u001f\u0012\u0013\u0012\u00110\u0016¢\u0006\f\b\u0017\u0012\b\b\u0018\u0012\u0004\b\b(\u0019\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0015H\u0007¢\u0006\u0002\u0010\u001aJ)\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u00062\u0012\u0010\f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\r\"\u00020\u0005H\u0003¢\u0006\u0002\u0010\u001eR\"\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00048\u0002X\u0083\u0004¢\u0006\b\n��\u0012\u0004\b\u0007\u0010\u0002R\u0016\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\tX\u0082\u0004¢\u0006\u0002\n��¨\u0006\u001f"}, d2 = {"Lcom/service/boot/common/TransferLock;", "", "()V", "lockMap", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lcom/service/boot/common/InnerTransferLock;", "getLockMap$annotations", "logger", "Lorg/slf4j/Logger;", "kotlin.jvm.PlatformType", "getLock", "accounts", "", "([Ljava/lang/String;)Lcom/service/boot/common/InnerTransferLock;", "transfer", "R", "onTransfer", "Lkotlin/Function0;", "([Ljava/lang/String;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "tryTransfer", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "locked", "([Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "unlock", "", "lock", "(Lcom/service/boot/common/InnerTransferLock;[Ljava/lang/String;)V", "common"})
/* loaded from: kapai-common.jar:com/service/boot/common/TransferLock.class */
public final class TransferLock {
    @NotNull
    public static final TransferLock INSTANCE = new TransferLock();
    private static final Logger logger = LoggerFactory.getLogger(TransferLock.class);
    @NotNull
    private static final ConcurrentHashMap<String, InnerTransferLock> lockMap = new ConcurrentHashMap<>();

    @JvmStatic
    private static /* synthetic */ void getLockMap$annotations() {
    }

    private TransferLock() {
    }

    @JvmStatic
    private static final synchronized InnerTransferLock getLock(String... accounts) {
        InnerTransferLock lock = null;
        for (String account : accounts) {
            TransferLock transferLock = INSTANCE;
            InnerTransferLock l = lockMap.get(account);
            if (lock != null && l != null && !Intrinsics.areEqual(lock, l)) {
                throw new RuntimeException("输入账号持有多个锁");
            }
            if (l != null) {
                lock = l;
            }
        }
        if (lock == null) {
            lock = new InnerTransferLock();
        }
        for (String account2 : accounts) {
            TransferLock transferLock2 = INSTANCE;
            lockMap.put(account2, lock);
        }
        lock.incrementAndGet();
        return lock;
    }

    @JvmStatic
    @Nullable
    public static final <R> R transfer(@NotNull String[] accounts, @NotNull Function0<? extends R> onTransfer) {
        TransferLock transferLock = INSTANCE;
        InnerTransferLock lock = getLock((String[]) Arrays.copyOf(accounts, accounts.length));
        try {
            logger.debug("加锁 [" + ArraysKt.joinToString$default(accounts, "->", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null) + "] [" + lock + "]");
            lock.lock();
            logger.debug("锁执行 [" + ArraysKt.joinToString$default(accounts, "->", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null) + "] [" + lock + "]");
            R r = (R) onTransfer.invoke();
            TransferLock transferLock2 = INSTANCE;
            unlock(lock, (String[]) Arrays.copyOf(accounts, accounts.length));
            return r;
        } catch (Throwable th) {
            TransferLock transferLock3 = INSTANCE;
            unlock(lock, (String[]) Arrays.copyOf(accounts, accounts.length));
            throw th;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Unexpected missing predecessor for block: B:4:0x0018
        	at jadx.core.dex.visitors.blocks.BlockSplitter.addTempConnectionsForExcHandlers(BlockSplitter.java:241)
        	at jadx.core.dex.visitors.blocks.BlockSplitter.visit(BlockSplitter.java:54)
        */
    @kotlin.jvm.JvmStatic
    @org.jetbrains.annotations.Nullable
    public static final <R> R tryTransfer(@org.jetbrains.annotations.NotNull java.lang.String[] r11, @org.jetbrains.annotations.NotNull kotlin.jvm.functions.Function1<? super java.lang.Boolean, ? extends R> r12) {
        /*
            com.service.boot.common.TransferLock r0 = com.service.boot.common.TransferLock.INSTANCE
            r0 = r11
            r1 = r11
            int r1 = r1.length
            java.lang.Object[] r0 = java.util.Arrays.copyOf(r0, r1)
            java.lang.String[] r0 = (java.lang.String[]) r0
            com.service.boot.common.InnerTransferLock r0 = getLock(r0)
            r13 = r0
            r0 = r13
            boolean r0 = r0.tryLock()
            if (r0 == 0) goto L6a
        L19:
            org.slf4j.Logger r0 = com.service.boot.common.TransferLock.logger     // Catch: java.lang.Throwable -> L56
            r1 = r11
            java.lang.String r2 = "->"
            java.lang.CharSequence r2 = (java.lang.CharSequence) r2     // Catch: java.lang.Throwable -> L56
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 62
            r9 = 0
            java.lang.String r1 = kotlin.collections.ArraysKt.joinToString$default(r1, r2, r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L56
            r2 = r13
            java.lang.String r1 = "加锁成功 [" + r1 + "] [" + r2 + "]"     // Catch: java.lang.Throwable -> L56
            r0.debug(r1)     // Catch: java.lang.Throwable -> L56
            r0 = r12
            r1 = 1
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch: java.lang.Throwable -> L56
            java.lang.Object r0 = r0.invoke(r1)     // Catch: java.lang.Throwable -> L56
            r14 = r0
            com.service.boot.common.TransferLock r0 = com.service.boot.common.TransferLock.INSTANCE     // Catch: java.lang.Throwable -> L56
            r0 = r13
            r1 = r11
            r2 = r11
            int r2 = r2.length
            java.lang.Object[] r1 = java.util.Arrays.copyOf(r1, r2)
            java.lang.String[] r1 = (java.lang.String[]) r1
            unlock(r0, r1)
            r0 = r14
            return r0
        L56:
            r14 = move-exception
            com.service.boot.common.TransferLock r0 = com.service.boot.common.TransferLock.INSTANCE     // Catch: java.lang.Throwable -> L56
            r0 = r13
            r1 = r11
            r2 = r11
            int r2 = r2.length
            java.lang.Object[] r1 = java.util.Arrays.copyOf(r1, r2)
            java.lang.String[] r1 = (java.lang.String[]) r1
            unlock(r0, r1)
            r0 = r14
            throw r0
        L6a:
            org.slf4j.Logger r0 = com.service.boot.common.TransferLock.logger
            r1 = r11
            java.lang.String r2 = "->"
            java.lang.CharSequence r2 = (java.lang.CharSequence) r2
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 62
            r9 = 0
            java.lang.String r1 = kotlin.collections.ArraysKt.joinToString$default(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            r2 = r13
            java.lang.String r1 = "加锁失败 [" + r1 + "] [" + r2 + "]"
            r0.warn(r1)
            r0 = r12
            r1 = 0
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)
            java.lang.Object r0 = r0.invoke(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.service.boot.common.TransferLock.tryTransfer(java.lang.String[], kotlin.jvm.functions.Function1):java.lang.Object");
    }

    @JvmStatic
    private static final void unlock(InnerTransferLock lock, String... accounts) {
        logger.debug("锁释放 [" + ArraysKt.joinToString$default(accounts, "->", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null) + "] [" + lock + "]");
        lock.unlock();
        if (lock.decrementAndGet() == 0) {
            for (String account : accounts) {
                TransferLock transferLock = INSTANCE;
                lockMap.remove(account);
            }
            logger.debug("锁移除 [" + lock + "]");
        }
        Logger logger2 = logger;
        TransferLock transferLock2 = INSTANCE;
        logger2.info("锁信息: " + lockMap);
    }
}

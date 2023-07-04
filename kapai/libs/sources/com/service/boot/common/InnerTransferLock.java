package com.service.boot.common;

import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

/* compiled from: TransferLock.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��.\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0010\b\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n��\n\u0002\u0010\u000e\n��\n\u0002\u0010\u000b\n\u0002\b\u0002\b��\u0018��2\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\u0004J\u0006\u0010\b\u001a\u00020\u0004J\u0006\u0010\u0005\u001a\u00020\tJ\b\u0010\n\u001a\u00020\u000bH\u0016J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\tR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n��R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n��¨\u0006\u000f"}, d2 = {"Lcom/service/boot/common/InnerTransferLock;", "", "()V", "count", "", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "decrementAndGet", "incrementAndGet", "", "toString", "", "tryLock", "", "unlock", "common"})
/* loaded from: kapai-common.jar:com/service/boot/common/InnerTransferLock.class */
public final class InnerTransferLock {
    private int count;
    @NotNull
    private final ReentrantLock lock = new ReentrantLock(false);

    public final void lock() {
        this.lock.lock();
    }

    public final boolean tryLock() {
        return this.lock.tryLock();
    }

    public final void unlock() {
        this.lock.unlock();
    }

    public final int incrementAndGet() {
        this.count++;
        return this.count;
    }

    public final int decrementAndGet() {
        this.count--;
        return this.count;
    }

    @NotNull
    public String toString() {
        return "Lock@" + hashCode() + ", count=" + this.count;
    }
}

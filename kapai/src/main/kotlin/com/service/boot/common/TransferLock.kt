package com.service.boot.common

import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

internal class InnerTransferLock {
    private var count = 0
    private val lock = ReentrantLock(false)

    fun tryLock() = lock.tryLock()

    fun unlock() = lock.unlock()

    fun incrementAndGet() = ++count

    fun decrementAndGet() = --count
}

object TransferLock {

    @JvmStatic
    private val lockMap = ConcurrentHashMap<String, InnerTransferLock>()

    @Synchronized
    @JvmStatic
    private fun getLock(vararg accounts: String): InnerTransferLock {
        var lock: InnerTransferLock? = null
        for (account in accounts) {
            val l = lockMap[account]
            if (lock != null && l != null && lock != l) {
                throw java.lang.RuntimeException("输入账号持有多个锁")
            }
            if (l != null) {
                lock = l
            }
        }
        if (lock == null) lock = InnerTransferLock()
        for (account in accounts) {
            lockMap[account] = lock
        }
        lock.incrementAndGet()
        return lock
    }

    @JvmStatic
    fun <R> tryTransfer(vararg accounts: String, onTransfer: (locked: Boolean) -> R?): R? {
        val lock = getLock(*accounts)
        if (lock.tryLock()) {
            try {
                return onTransfer(true)
            } finally {
                unlock(lock, *accounts)
            }
        }
        return onTransfer(false)
    }

    @JvmStatic
    private fun unlock(lock: InnerTransferLock, vararg accounts: String) {
        lock.unlock()
        if (lock.decrementAndGet() == 0) {
            for (account in accounts) {
                lockMap.remove(account)
            }
        }
    }

}
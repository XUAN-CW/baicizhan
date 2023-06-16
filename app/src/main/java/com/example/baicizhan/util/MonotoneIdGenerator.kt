package com.example.baicizhan.util

import java.util.concurrent.atomic.AtomicLong

object IdUtil {
    private val counter = AtomicLong(System.currentTimeMillis())

    fun generateLongId(): Long {
        return counter.getAndIncrement()
    }
}

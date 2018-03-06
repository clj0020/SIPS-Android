package com.madmensoftware.sips.util

/**
 * Created by clj00 on 3/2/2018.
 */
import io.reactivex.Scheduler

interface SchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}
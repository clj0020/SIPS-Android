package com.madmensoftware.sips.util

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers



/**
 * Created by clj00 on 3/2/2018.
 */
class AppSchedulerProvider : SchedulerProvider {

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
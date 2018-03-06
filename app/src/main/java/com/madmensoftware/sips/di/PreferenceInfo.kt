package com.madmensoftware.sips.di

/**
 * Created by clj00 on 3/2/2018.
 */
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Qualifier

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class PreferenceInfo
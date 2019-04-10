package com.example.awesomefamilyshoppinglist.util

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

/** A tree which logs important information for crash reporting.  */
class CrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        Crashlytics.log(priority, tag, message)

        if (t != null) {
            if (priority == Log.ERROR) {
                Crashlytics.logException(t)
            }
        }
    }
}
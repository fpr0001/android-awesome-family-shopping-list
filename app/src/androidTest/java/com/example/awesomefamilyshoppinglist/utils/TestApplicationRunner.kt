package com.example.awesomefamilyshoppinglist.utils

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TestApplicationRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application =
        Instrumentation.newApplication(AppForTests::class.java, context)

}
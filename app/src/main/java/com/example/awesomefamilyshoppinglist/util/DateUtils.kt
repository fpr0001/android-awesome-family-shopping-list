package com.example.awesomefamilyshoppinglist.util

import android.icu.text.DateFormat
import com.google.firebase.Timestamp
import java.util.*

object DateUtils {

    private val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)

    @JvmStatic
    fun toText(timestamp: Timestamp): String = dateFormat.format(timestamp.toDate())

    @JvmStatic
    fun toText(date: Date): String = dateFormat.format(date)
}
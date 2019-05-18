package com.example.awesomefamilyshoppinglist.util

import android.icu.text.DateFormat
import java.util.*

object DateUtils {

    private val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)

    @JvmStatic
    fun toText(milliseconds: Long): String = dateFormat.format(Date(milliseconds))


}
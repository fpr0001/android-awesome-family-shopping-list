package com.example.awesomefamilyshoppinglist.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.awesomefamilyshoppinglist.R

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.showToast(@StringRes stringRes: Int = R.string.general_error_message) {
    Toast.makeText(this, getString(stringRes), Toast.LENGTH_LONG).show()
}




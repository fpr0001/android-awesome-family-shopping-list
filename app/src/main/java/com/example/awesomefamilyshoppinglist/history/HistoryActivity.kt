package com.example.awesomefamilyshoppinglist.history

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, HistoryActivity::class.java))
        }
    }
}
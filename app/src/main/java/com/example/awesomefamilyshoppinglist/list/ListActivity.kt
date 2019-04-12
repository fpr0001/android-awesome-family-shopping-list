package com.example.awesomefamilyshoppinglist.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class ListActivity : FragmentActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ListActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

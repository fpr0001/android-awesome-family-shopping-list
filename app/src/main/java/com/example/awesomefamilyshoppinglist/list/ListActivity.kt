package com.example.awesomefamilyshoppinglist.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class ListActivity : FragmentActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.let {
                it.startActivity(Intent(it, ListActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

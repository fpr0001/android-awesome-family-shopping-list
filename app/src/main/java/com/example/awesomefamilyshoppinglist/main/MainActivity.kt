package com.example.awesomefamilyshoppinglist.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.databinding.ActivityMainBinding
import com.example.awesomefamilyshoppinglist.databinding.NavHeaderMainBinding
import com.example.awesomefamilyshoppinglist.list.ListActivity
import com.example.awesomefamilyshoppinglist.splash.SplashActivity
import com.example.awesomefamilyshoppinglist.splash.SplashContract
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    internal lateinit var vmFactory: MainContract.ViewModel.Companion.Factory

    @Inject
    internal lateinit var router: MainContract.Router

    lateinit var viewModel: MainContract.ViewModel

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = vmFactory.getViewModel(this)

        val mainBinding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.lifecycleOwner = this
        mainBinding.viewModel = viewModel

        val navigationHeaderBinding: NavHeaderMainBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.nav_header_main, mainBinding.navView, false)
        navigationHeaderBinding.lifecycleOwner = this
        navigationHeaderBinding.viewModel = viewModel
        mainBinding.navView.addHeaderView(navigationHeaderBinding.root)

        setSupportActionBar(toolbar)

        setListeners()
        if (savedInstanceState == null) {
            viewModel.loadUser()
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        nav_view.imageView
    }

    private fun setListeners() {
        viewModel.user.observe(this, Observer { user -> if (user == null) router.goToSplash(this) })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.action_history -> {
                router.goToHistory(this)
            }
            R.id.action_logout -> {
                viewModel.logout()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}

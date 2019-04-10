package com.example.awesomefamilyshoppinglist.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.list.ListActivity
import com.example.awesomefamilyshoppinglist.util.showToast
import com.firebase.ui.auth.IdpResponse
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : FragmentActivity() {

    companion object {
        const val CODE_SIGN_IN = 1
    }

    @Inject
    internal lateinit var vmFactory: SplashContract.ViewModel.Companion.Factory

    lateinit var viewModel: SplashContract.ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = vmFactory.getViewModel(this)
        setListeners(viewModel)
        viewModel.autoLogin()
    }

    private fun setListeners(vm: SplashContract.ViewModel) {

        vm.loginIntent.observe(
            this,
            Observer { loginIntent -> startActivityForResult(loginIntent, CODE_SIGN_IN) })

        vm.user.observe(this,
            Observer { goToListActivity() })
    }

    private fun goToListActivity() {
        ListActivity.startActivity(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                goToListActivity()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                if (response != null) {
                    showToast()
                }
                viewModel.enableTryAgain()
            }
        }
    }

}
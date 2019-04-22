package com.example.awesomefamilyshoppinglist.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.databinding.ActivitySplashBinding
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.splash.SplashContract.Router.Companion.CODE_SIGN_IN
import com.example.awesomefamilyshoppinglist.util.showToast
import com.firebase.ui.auth.IdpResponse
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : FragmentActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SplashActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    internal lateinit var vmFactory: SplashContract.ViewModelFactory

    @Inject
    internal lateinit var router: SplashContract.Router

    lateinit var viewModel: SplashContract.ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        binding.lifecycleOwner = this
        viewModel = vmFactory.getViewModel(this)
        binding.viewModel = viewModel
        setListeners()
        if (savedInstanceState == null) {
            viewModel.autoLogin()
        }
    }

    private fun setListeners() {
        viewModel.user.observe(this,
            Observer { user ->
                if (user != null) {
                    router.goToMain()
                } else {
                    router.goToLogin()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                router.goToMain()
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
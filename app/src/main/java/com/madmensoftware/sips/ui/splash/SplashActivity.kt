package com.madmensoftware.sips.ui.splash

import android.os.Bundle
import com.madmensoftware.sips.ui.main.MainActivity
import android.content.Context
import android.content.Intent
import com.madmensoftware.sips.ui.login.LoginActivity
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.R
import com.madmensoftware.sips.databinding.ActivitySplashBinding
import javax.inject.Inject
import com.madmensoftware.sips.ui.base.BaseActivity



/**
 * Created by clj00 on 3/2/2018.
 */
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashNavigator {

    @Inject
    override lateinit var viewModel: SplashViewModel
        internal set

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_splash

    override fun openLoginActivity() {
        val intent = LoginActivity.newIntent(this@SplashActivity)
        startActivity(intent)
        finish()
    }

    override fun openMainActivity() {
        val intent = MainActivity.newIntent(this@SplashActivity)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewModel.decideNextActivity()
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, SplashActivity::class.java)
        }
    }
}
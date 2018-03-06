package com.madmensoftware.sips.ui.login

import android.os.Bundle
import com.madmensoftware.sips.ui.main.MainActivity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.R
import com.madmensoftware.sips.databinding.ActivityLoginBinding
import javax.inject.Inject
import com.madmensoftware.sips.ui.base.BaseActivity



/**
 * Created by clj00 on 3/2/2018.
 */
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), LoginNavigator {

    @Inject
    override lateinit var viewModel: LoginViewModel
        internal set

    private var mActivityLoginBinding: ActivityLoginBinding? = null

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun handleError(throwable: Throwable) {
        // handle error
    }

    override fun login() {
        val email = mActivityLoginBinding!!.etEmail.getText().toString()
        val password = mActivityLoginBinding!!.etPassword.getText().toString()
        if (viewModel.isEmailAndPasswordValid(email, password)) {
            hideKeyboard()
            viewModel.login(email, password)
        } else {
            Toast.makeText(this, getString(R.string.invalid_email_password), Toast.LENGTH_SHORT).show()
        }
    }

    override fun openMainActivity() {
        val intent = MainActivity.newIntent(this@LoginActivity)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityLoginBinding = viewDataBinding
        viewModel.navigator = this
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
package com.madmensoftware.sips.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.madmensoftware.sips.R
import dagger.android.AndroidInjector
import android.content.Intent
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.BuildConfig
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.databinding.ActivityMainBinding
import com.madmensoftware.sips.ui.athlete.AthleteFragment
import com.madmensoftware.sips.ui.athlete_add.AddAthleteFragment
import com.madmensoftware.sips.ui.athlete_edit.EditAthleteFragment
import com.madmensoftware.sips.ui.athlete_list.AthleteListFragment
import com.madmensoftware.sips.ui.athlete_test.TestAthleteFragment
import javax.inject.Inject
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import com.madmensoftware.sips.ui.base.BaseActivity
import com.madmensoftware.sips.ui.login.LoginActivity
import java.util.*



class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator, HasSupportFragmentInjector {

    @Inject
    internal lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    override lateinit var viewModel: MainViewModel
        internal set

    private var mActivityMainBinding: ActivityMainBinding? = null
    private var mToolbar: Toolbar? = null

    var mFragmentStack: Stack<String> = Stack<String>()

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = viewDataBinding
        viewModel.navigator = this
        setUp()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentDispatchingAndroidInjector
    }

    override fun handleError(throwable: Throwable) {
        // handle error
    }

    override fun openLoginActivity() {
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    private fun setUp() {
        mToolbar = mActivityMainBinding!!.toolbar

        setSupportActionBar(mToolbar)

        val version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME
        viewModel.updateAppVersion(version)


        viewModel.backStackLiveData.observe(this, Observer { data ->
            handleViewData(data!!)
        })
    }

    private fun setupViewFragment(fragment: Fragment) {
        if (supportFragmentManager.findFragmentByTag(fragment.javaClass.name) == null) {
            // Create the fragment
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
            if (!mFragmentStack.isEmpty()) {
                val currentFragment = supportFragmentManager.findFragmentByTag(mFragmentStack.peek())
                transaction.hide(currentFragment)
            }

            transaction.add(R.id.fragment_container, fragment, fragment.javaClass.name)
            transaction.addToBackStack(fragment.tag)
            mFragmentStack.add(fragment.tag)
            transaction.commit()
        }
    }

    private fun handleViewData(data: MainViewModel.Data) {
        when (data) {
            MainViewModel.Data.AthletesData -> {
                hideBackButton()
                supportActionBar!!.title = "Auburn University" // TODO: Hardcoded organization
                setupViewFragment(AthleteListFragment.newInstance())
            }
            is MainViewModel.Data.AthleteData -> {
                showBackButton()
                supportActionBar!!.title = "Athlete Profile"
                setupViewFragment(AthleteFragment.newInstance(data.athleteId))
            }
            MainViewModel.Data.AddAthleteData -> {
                showBackButton()
                supportActionBar!!.title = "Add Athlete"
                setupViewFragment(AddAthleteFragment.newInstance())
            }
            is MainViewModel.Data.TestAthleteData -> {
                showBackButton()
                supportActionBar!!.title = "Test Athlete"
                setupViewFragment(TestAthleteFragment.newInstance(data.athleteId))
            }
            is MainViewModel.Data.EditAthleteData -> {
                showBackButton()
                supportActionBar!!.title = "Edit Athlete"
                setupViewFragment(EditAthleteFragment.newInstance(data.athleteId))
            }

        }
    }

    override fun onBackPressed() {
        if (viewModel.onBackPress()) {
            // remove the current fragment from the stack.
            mFragmentStack.pop()
            val transaction = supportFragmentManager.beginTransaction()
            // get fragment that is to be shown (in our case fragment1).
            val fragment = supportFragmentManager.findFragmentByTag(mFragmentStack.peek())
            // This time I set an animation with no fade in, so the user doesn't wait for the animation in back press
            transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right)

            // Refresh Athlete Profile Page if we're going back to it.
            if (fragment is AthleteFragment) {
                fragment.viewModel.fetchAthlete()
            }

            // Refresh Athlete List if we're going back to it.
            if (fragment is AthleteListFragment) {
                fragment.viewModel.fetchAthletes()
            }

            // We must use the show() method.
            transaction.show(fragment)
            transaction.commit()



            super.onBackPressed()
        }
    }

    /** Shows the back button on the toolbar_main */
    fun showBackButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /** Shows the back button on the toolbar_main */
    fun hideBackButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

}
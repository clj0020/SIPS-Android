package com.madmensoftware.sips.ui.main

import android.arch.lifecycle.Observer
import android.content.*
import com.madmensoftware.sips.R
import dagger.android.AndroidInjector
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.BuildConfig
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
import android.util.Log
import com.madmensoftware.sips.data.services.MainActivityHandler
import android.content.Intent
import android.os.*
import com.madmensoftware.sips.data.services.AthleteActivityRecognitionService


/** This is the Main Activity that holds all of the different Fragments.
 *  Handles Fragment Navigation mostly.
 */
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator, HasSupportFragmentInjector, ServiceConnection {

    @Inject
    internal lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    override lateinit var viewModel: MainViewModel
        internal set

    private var mActivityMainBinding: ActivityMainBinding? = null
    private var mToolbar: Toolbar? = null

    var mFragmentStack: Stack<String> = Stack<String>()

    // Two messengers, one for service and one for activity.
    lateinit var mAthleteActivityRecognitionServiceMessenger: Messenger
    private val mActivityMessenger = Messenger(MainActivityHandler(this@MainActivity))
    var mAthleteActivityRecognitionServiceBound: Boolean = false


    // We need to create a ServiceConnection in order to bind the player service to the activity.
    // In the onServiceConnected method that we override, we can get the binder object that the service
    // returns from its onBind method and use our custom Binder's getService method to get the instance of
    // the service.


    override val bindingVariable: Int
        get() = BR.viewModel

    /** Sets Layout File **/
    override val layoutId: Int
        get() = R.layout.activity_main

    /** Sets up navigator, viewBinding, and other UI elements. **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = viewDataBinding
        viewModel.navigator = this
        setUp()
    }

    /** Sets up Fragment Injector for Dependency Injection. **/
    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentDispatchingAndroidInjector
    }

    /** Handles errors. **/
    override fun handleError(throwable: Throwable) {
        // handle error
    }

    /** Opens Login Activity **/
    override fun openLoginActivity() {
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    /** Sets up UI and Fragment Navigation **/
    private fun setUp() {
        mToolbar = mActivityMainBinding!!.toolbar

        setSupportActionBar(mToolbar)

        val version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME
        viewModel.updateAppVersion(version)


        viewModel.backStackLiveData.observe(this, Observer { data ->
            handleViewData(data!!)
        })
    }

    /** Shows a fragment and adds last fragment to back stack. **/
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

    /** Handles Fragment Navigation. **/
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

    /** Handles Fragments when Back Button is pressed. **/
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

    override fun onAthleteActivityRecognitionServiceRequested() {
        // Bind the service
        val intent = Intent(this@MainActivity, AthleteActivityRecognitionService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()

        if (mAthleteActivityRecognitionServiceBound) {
            // Unbind from the service.
            unbindService(this)
            mAthleteActivityRecognitionServiceBound = false
        }
    }

    override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
        // We have to manually declare a boolean bound field and keep track of it ourselves,
        // so that we don't try to do things with an unbound service. Because onServiceConnected
        // is called, we know that the service is bound.
        Log.i(TAG,"Service connected. Retrieving service messenger.")

        mAthleteActivityRecognitionServiceBound = true

        // Get the service's messenger using the binder
        mAthleteActivityRecognitionServiceMessenger = Messenger(binder)

        // Start the athlete activity recognition service
        startAthleteActivityRecognitionService(mAthleteActivityRecognitionServiceMessenger, mActivityMessenger)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        Log.i(TAG,"Service disconnected.")
        mAthleteActivityRecognitionServiceBound = false
    }

    private fun startAthleteActivityRecognitionService(athleteActivityRecognitionServiceMessenger: Messenger, mainActivityMessenger: Messenger) {
        val intent = Intent(this@MainActivity, AthleteActivityRecognitionService::class.java)
        intent.putExtra(EXTRA_ACTIVITY_MESSENGER, mActivityMessenger)
        startService(intent)

        val message: Message = Message.obtain()
        message.arg1 = ACTIVITY_CONNECTING_TO_SERVICE_MESSAGE

        // set this message's messenger to the activity messenger of this class
        message.replyTo = mainActivityMessenger

        // use the service's messenger to send the message to our service
        try {
            athleteActivityRecognitionServiceMessenger.send(message)

            Log.i(TAG,"Sending message to the Service using the service's messenger and it's replyto as the activity's messenger.")
        }
        catch (e: RemoteException) {
            Log.e(TAG,"RemoteException! " + e.message)
        }
    }

    private fun stopAthleteActivityRecognitionService(athleteActivityRecognitionServiceMessenger: Messenger, mainActivityMessenger: Messenger) {

        val message: Message = Message.obtain()
        message.arg1 = TURN_OFF_SERVICE_MESSAGE

        // set this message's messenger to the activity messenger of this class
        message.replyTo = mainActivityMessenger

        // use the service's messenger to send the message to our service
        try {
            athleteActivityRecognitionServiceMessenger.send(message)
            Log.i(TAG,"Sending message to turn off the AthleteActivityRecognitionService using the its messenger.")
        }
        catch (e: RemoteException) {
            Log.e(TAG,"RemoteException! " + e.message)
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

    /** Initializes Activity. **/
    companion object {
        val TAG = MainActivity::class.java.simpleName
        val EXTRA_ACTIVITY_MESSENGER = "extra_activity_messenger"
        val ACTIVITY_CONNECTING_TO_SERVICE_MESSAGE = 0
        val TURN_OFF_SERVICE_MESSAGE = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

}
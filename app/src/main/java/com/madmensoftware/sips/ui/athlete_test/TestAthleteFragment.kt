package com.madmensoftware.sips.ui.athlete_test

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.*
import com.github.pwittchen.reactivesensors.library.ReactiveSensors
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.R
import com.madmensoftware.sips.databinding.FragmentTestAthleteBinding
import com.madmensoftware.sips.ui.base.BaseFragment
import com.madmensoftware.sips.ui.main.MainActivity
import com.madmensoftware.sips.util.SensorHelper
import kotlinx.android.synthetic.main.fragment_test_athlete.*
import javax.inject.Inject

/**
 * Created by clj00 on 3/5/2018.
 */
class TestAthleteFragment : BaseFragment<FragmentTestAthleteBinding, TestAthleteViewModel>(), TestAthleteNavigator {

    @Inject
    override lateinit var viewModel: TestAthleteViewModel
        internal set

    @Inject
    lateinit var mReactiveSensors: ReactiveSensors

    @Inject
    lateinit var mSensorHelper: SensorHelper


    var mFragmentTestAthleteBinding: FragmentTestAthleteBinding? = null


    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_test_athlete


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewModel.athleteId = getArguments()!!.getLong(TestAthleteFragment.KEY_ATHLETE_ID)
        viewModel.mReactiveSensors = mReactiveSensors
        viewModel.mSensorHelper = mSensorHelper
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentTestAthleteBinding = viewDataBinding
        mFragmentTestAthleteBinding!!.isTestSelected = false
        mFragmentTestAthleteBinding!!.isClockStarted = false
    }

    override fun testSaved() {
        showSuccess("Test Saved", "Test was successfully added to database.")
        goBack()
    }

    override fun testStarted() {
        val timeObserver = Observer<String> {formattedTime ->
            if (formattedTime != null) {
                time_text.setText(formattedTime)
            }
        }

        viewModel.formattedTime.observe(this, timeObserver)

        mFragmentTestAthleteBinding!!.isClockStarted = true
    }

    override fun testStopped() {
        mFragmentTestAthleteBinding!!.isClockStarted = false
    }

    override fun testSelected(testId: Int) {
        mFragmentTestAthleteBinding!!.isTestSelected = true
    }

    override fun goBack() {
        (baseActivity as MainActivity).onBackPressed()
    }

    override fun handleError(throwable: Throwable) {
        showError("Error", throwable.message!!)
    }

    /** For handling toolbar_main actions  */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mActivity = activity as MainActivity
        when (item.itemId) {
            android.R.id.home -> {
                mActivity.onBackPressed()
            }
            R.id.log_out_button -> {
                mActivity.viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** Simply inflates menu resource file to the toolbar_main  */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_detail, menu)
    }

    companion object {

        val TAG = TestAthleteFragment::class.java.simpleName
        val KEY_ATHLETE_ID = "athleteId"


        fun newInstance(athleteId: Long): TestAthleteFragment {
            val args = Bundle()
            args.putLong(KEY_ATHLETE_ID, athleteId)
            val fragment = TestAthleteFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
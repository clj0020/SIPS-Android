package com.madmensoftware.sips.ui.athlete_test

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.github.pwittchen.reactivesensors.library.ReactiveSensors
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.R
import com.madmensoftware.sips.data.models.room.TestType
import com.madmensoftware.sips.databinding.FragmentTestAthleteBinding
import com.madmensoftware.sips.ui.athlete_list.AthleteListAdapter
import com.madmensoftware.sips.ui.base.BaseFragment
import com.madmensoftware.sips.ui.main.MainActivity
import com.madmensoftware.sips.util.SensorHelper
import com.wardellbagby.rxcountdowntimer.RxCountDownTimer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_test_athlete.*
import javax.inject.Inject
import cn.pedant.SweetAlert.SweetAlertDialog
import com.madmensoftware.sips.data.models.api.TestDataRequest


/**
 * Created by clj00 on 3/5/2018.
 */
class TestAthleteFragment : BaseFragment<FragmentTestAthleteBinding, TestAthleteViewModel>(), TestAthleteNavigator, TestTypeListAdapter.TestTypeAdapterListener {

    @Inject
    lateinit var mTestTypeAdapter: TestTypeListAdapter

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager

    @Inject
    override lateinit var viewModel: TestAthleteViewModel
        internal set

    private var mTestTypes: List<TestType> = arrayListOf()


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
        mTestTypeAdapter.setListener(this)
        viewModel.athleteId = getArguments()!!.getString(TestAthleteFragment.KEY_ATHLETE_ID)
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
        setUp()
        subscribeToLiveData()
    }

    fun setUp() {
        mFragmentTestAthleteBinding!!.isTestSelected = false
        mFragmentTestAthleteBinding!!.isClockStarted = false
        mFragmentTestAthleteBinding!!.context = context

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mFragmentTestAthleteBinding!!.testTypeListRecyclerView.setLayoutManager(layoutManager)
        mFragmentTestAthleteBinding!!.testTypeListRecyclerView.setItemAnimator(DefaultItemAnimator())
        mFragmentTestAthleteBinding!!.testTypeListRecyclerView.setAdapter(mTestTypeAdapter)

    }

    override fun updateTestTypeList(testTypeList: List<TestType>) {
        mTestTypeAdapter.addItems(testTypeList)
    }

    private fun subscribeToLiveData() {
        viewModel.testTypeListLiveData.observe(this, Observer<List<TestType>> {
            testTypes ->
            viewModel.addTestTypeItemsToList(testTypes!!)
            mTestTypes = testTypes
        }
        )
    }

    override fun showConfirmDialog(testDataRequest: TestDataRequest.UploadTestDataRequest) {
        val confirmDialog = SweetAlertDialog(baseActivity, SweetAlertDialog.WARNING_TYPE)
        confirmDialog.titleText = "Do you want to upload testing data?"
        confirmDialog.contentText = "You can always redo if you don't think this was a good test"

        confirmDialog.confirmText = "Upload"
        confirmDialog.setConfirmClickListener(SweetAlertDialog.OnSweetClickListener {dialog ->
            Log.i("Confirm Dialog","clicked confirm")
            dialog.dismissWithAnimation()
            viewModel.uploadTestData(testDataRequest)
        })


        confirmDialog.cancelText = "Redo Test"
        confirmDialog.showCancelButton(true)
        confirmDialog.setCancelable(true)
        confirmDialog.setCancelClickListener(SweetAlertDialog.OnSweetClickListener {dialog ->
            Log.i("Confirm Dialog","clicked cancel")
            dialog.cancel()
            viewModel.resetTime()
        })

        confirmDialog.show()
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
        viewModel.stopCountDownTimer()
    }

    override fun testSelected(testType: TestType) {
        mFragmentTestAthleteBinding!!.isTestSelected = true
        time_text.text = viewModel.formatTime(testType.duration!!.toLong())
        test_title_text.text = testType.title
    }

    override fun goBack() {
        (baseActivity as MainActivity).onBackPressed()
    }

    override fun onRetryClick() {
        viewModel.fetchTestTypes()
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


        fun newInstance(athleteId: String): TestAthleteFragment {
            val args = Bundle()
            args.putString(KEY_ATHLETE_ID, athleteId)
            val fragment = TestAthleteFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
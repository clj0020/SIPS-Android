package com.madmensoftware.sips.ui.athlete

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.R
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.databinding.FragmentAthleteBinding

import com.madmensoftware.sips.ui.base.BaseFragment
import com.madmensoftware.sips.ui.main.MainActivity
import javax.inject.Inject

/**
 * Created by clj00 on 3/4/2018.
 */
class AthleteFragment : BaseFragment<FragmentAthleteBinding, AthleteViewModel>(), AthleteNavigator, TestDataListAdapter.TestDataAdapterListener {

    @Inject
    lateinit var mTestDataAdapter: TestDataListAdapter

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager

    @Inject
    override lateinit var viewModel: AthleteViewModel
        internal set

    var mFragmentAthleteBinding: FragmentAthleteBinding? = null

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_athlete

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        mTestDataAdapter.setListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentAthleteBinding = viewDataBinding

        setUp()

        val athleteId: String = arguments!!.getString(KEY_ATHLETE_ID)

        subscribeToAthleteLiveData(athleteId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUp() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mFragmentAthleteBinding!!.testDataListRecyclerView.setLayoutManager(layoutManager)
        mFragmentAthleteBinding!!.testDataListRecyclerView.setItemAnimator(DefaultItemAnimator())
        mFragmentAthleteBinding!!.testDataListRecyclerView.setAdapter(mTestDataAdapter)

        mFragmentAthleteBinding!!.testDataListSwipeRefresh.setOnRefreshListener(
                SwipeRefreshLayout.OnRefreshListener {
                    mFragmentAthleteBinding!!.viewModel!!.fetchTestData()
                }
        )
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        mFragmentAthleteBinding!!.testDataListSwipeRefresh.isRefreshing = isRefreshing
    }

    private fun subscribeToAthleteLiveData(athleteId: String) {
        viewModel.athleteId = athleteId
        viewModel.fetchAthlete()
        viewModel.athleteLiveData.observe(this, Observer<Athlete> {
            athlete: Athlete? ->
            if (athlete != null) {
                mFragmentAthleteBinding!!.setAthlete(athlete)
                subscribeToTestDataLiveData()
            }
        })
    }

    private fun subscribeToTestDataLiveData() {
        viewModel.fetchTestData()
        viewModel.testDataListLiveData.observe(this, Observer<List<TestData>> {
            testDataList -> viewModel.addTestDataItemsToList(testDataList!!)
        })
    }

    override fun updateTestDataList(testDataList: List<TestData>) {
        mTestDataAdapter.addItems(testDataList)
    }

    override fun onRetryClick() {
        viewModel.fetchTestData()
    }

    override fun showTestAthleteFragment(athleteId: String) {
        (activity as MainActivity).viewModel.onTestAthlete(athleteId)
    }

    override fun showEditAthleteFragment(athleteId: String) {
        (activity as MainActivity).viewModel.onEditAthlete(athleteId)
    }

    override fun onStart() {
        super.onStart()
        Log.i("AthleteFrag", "onStart")
    }

    override fun onPause() {
        super.onPause()
        mLayoutManager.detachAndScrapAttachedViews(mFragmentAthleteBinding!!.testDataListRecyclerView.Recycler())
        Log.i("AthleteFrag", "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.i("AthleteFrag", "onResume")
    }

    override fun handleError(throwable: Throwable) {
        // TODO: handle error
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
        val TAG = AthleteFragment::class.java.simpleName
        val KEY_ATHLETE_ID = "athleteId"

        fun newInstance(athleteId: String): AthleteFragment {
            val args = Bundle()
            args.putString(KEY_ATHLETE_ID, athleteId)
            val fragment = AthleteFragment()
            fragment.setArguments(args)
            return fragment
        }
    }
}
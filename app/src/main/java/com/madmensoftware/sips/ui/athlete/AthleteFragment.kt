package com.madmensoftware.sips.ui.athlete

import android.animation.ValueAnimator
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
import kotlinx.android.synthetic.main.fragment_athlete.*
import javax.inject.Inject
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.view.animation.LinearInterpolator
import android.animation.ObjectAnimator
import android.os.Build
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.madmensoftware.sips.R.id.scroll
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.scrollY
import android.support.v4.view.ViewCompat.setTranslationY
import com.github.ksoichiro.android.observablescrollview.ScrollUtils
import android.R.id.primary
import android.view.animation.AlphaAnimation
import android.support.design.widget.AppBarLayout






/**
 * Created by clj00 on 3/4/2018.
 */
class AthleteFragment : BaseFragment<FragmentAthleteBinding, AthleteViewModel>(), AthleteNavigator, TestDataListAdapter.TestDataAdapterListener, AppBarLayout.OnOffsetChangedListener {

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
        mFragmentAthleteBinding!!.profileSwipeRefresh.setOnRefreshListener({
                    mFragmentAthleteBinding!!.viewModel!!.fetchAthlete()
                    mFragmentAthleteBinding!!.viewModel!!.fetchTestData()
        })

        mFragmentAthleteBinding!!.profileAppBarLayout.addOnOffsetChangedListener(this)
        startAlphaAnimation(mFragmentAthleteBinding!!.athleteName, 0, View.INVISIBLE)
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        mFragmentAthleteBinding!!.profileSwipeRefresh.isRefreshing = isRefreshing
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

    override fun onPause() {
        super.onPause()
        mLayoutManager.detachAndScrapAttachedViews(mFragmentAthleteBinding!!.testDataListRecyclerView.Recycler())
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
            R.id.edit_athlete_button -> {
                viewModel.onEditAthleteButtonClick(arguments!!.getString(KEY_ATHLETE_ID))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** Simply inflates menu resource file to the toolbar_main  */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_profile, menu)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)

        mFragmentAthleteBinding!!.profileSwipeRefresh.setEnabled(offset == 0)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= viewModel.PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!viewModel.mIsTheTitleVisible) {
                startAlphaAnimation(mFragmentAthleteBinding!!.athleteName, viewModel.ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                viewModel.mIsTheTitleVisible = true
            }

        } else {

            if (viewModel.mIsTheTitleVisible) {
                startAlphaAnimation(mFragmentAthleteBinding!!.athleteName, viewModel.ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                viewModel.mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= viewModel.PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (viewModel.mIsTheTitleContainerVisible) {
                startAlphaAnimation(mFragmentAthleteBinding!!.athleteNameContainer, viewModel.ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                viewModel.mIsTheTitleContainerVisible = false
            }

        } else {

            if (!viewModel.mIsTheTitleContainerVisible) {
                startAlphaAnimation(mFragmentAthleteBinding!!.athleteNameContainer, viewModel.ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                viewModel.mIsTheTitleContainerVisible = true
            }
        }
    }

    fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE)
            AlphaAnimation(0f, 1f)
        else
            AlphaAnimation(1f, 0f)

        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
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
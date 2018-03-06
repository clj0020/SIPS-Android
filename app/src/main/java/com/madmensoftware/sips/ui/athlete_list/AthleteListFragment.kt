package com.madmensoftware.sips.ui.athlete_list

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.R
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.databinding.FragmentAthleteListBinding
import com.madmensoftware.sips.ui.base.BaseFragment
import com.madmensoftware.sips.ui.main.MainActivity
import javax.inject.Inject

/**
 * Created by clj00 on 3/2/2018.
 */
class AthleteListFragment : BaseFragment<FragmentAthleteListBinding, AthleteListViewModel>(), AthleteListNavigator, AthleteListAdapter.AthleteAdapterListener, SearchView.OnQueryTextListener {

    @Inject
    lateinit var mAthleteAdapter: AthleteListAdapter

    var mFragmentAthleteListBinding: FragmentAthleteListBinding? = null

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager


    @Inject
    override lateinit var viewModel: AthleteListViewModel
        internal set

    private var mAthletes: List<Athlete> = arrayListOf()

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_athlete_list


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        mAthleteAdapter.setListener(this)
    }

    override fun onRetryClick() {
        viewModel.fetchAthletes()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentAthleteListBinding = viewDataBinding
        setUp()
        subscribeToLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun updateAthleteList(athleteList: List<Athlete>) {
        mAthleteAdapter.addItems(athleteList)
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setUp() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mFragmentAthleteListBinding!!.athleteListRecyclerView.setLayoutManager(layoutManager)
        mFragmentAthleteListBinding!!.athleteListRecyclerView.setItemAnimator(DefaultItemAnimator())
        mFragmentAthleteListBinding!!.athleteListRecyclerView.setAdapter(mAthleteAdapter)
    }

    private fun subscribeToLiveData() {
        viewModel.athleteListLiveData.observe(this, Observer<List<Athlete>> {
            athletes ->
                viewModel.addAthleteItemsToList(athletes!!)
                mAthletes = athletes
        }
        )
    }

    override fun handleError(throwable: Throwable) {
        // TODO: handle error
    }


    /** For handling toolbar_main actions  */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mActivity = activity as MainActivity
        when (item.itemId) {
            R.id.add_athlete_button -> {
                mActivity.viewModel.onAddAthlete()
                return true
            }
            android.R.id.home -> {
                mActivity.onBackPressed()
            }
            R.id.log_out_button -> {
                mActivity.viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_main, menu)

        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.search_athletes_button)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        // Define the listener
        val searchViewExpandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Do something when action item collapses

                return true // Return true to collapse action view
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do something when expanded
                mAthleteAdapter.addItems(mAthletes)
                return true // Return true to expand action view

            }
        }

        // Assign the searchview expansion listener to search item
        searchItem.setOnActionExpandListener(searchViewExpandListener)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        val filteredModelList = filter(mAthletes, newText)
        mAthleteAdapter.setSearchResult(filteredModelList)
        return true
    }

    private fun filter(models: List<Athlete>, query: String): List<Athlete> {
        val mQuery = query.toLowerCase()
        val filteredModelList = ArrayList<Athlete>()
        for (model in models) {
            val text = model.first_name?.toLowerCase() + " " + model.last_name?.toLowerCase()
            if (text.contains(mQuery)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    companion object {

        val TAG = AthleteListFragment::class.java.simpleName

        fun newInstance(): AthleteListFragment {
            val args = Bundle()
            val fragment = AthleteListFragment()
            fragment.setArguments(args)
            return fragment
        }
    }
}
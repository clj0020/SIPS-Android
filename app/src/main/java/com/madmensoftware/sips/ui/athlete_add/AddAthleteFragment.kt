package com.madmensoftware.sips.ui.athlete_add

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.*
import android.widget.Toast
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.R
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.databinding.FragmentAddAthleteBinding
import javax.inject.Inject
import com.madmensoftware.sips.ui.base.BaseFragment
import com.madmensoftware.sips.ui.main.MainActivity


/**
 * Created by clj00 on 3/2/2018.
 */
class AddAthleteFragment : BaseFragment<FragmentAddAthleteBinding, AddAthleteViewModel>(), AddAthleteNavigator {


    @Inject
    override lateinit var viewModel: AddAthleteViewModel
        internal set

    var mFragmentAddAthleteBinding: FragmentAddAthleteBinding? = null


    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_add_athlete


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentAddAthleteBinding = viewDataBinding
    }

    override fun addAthlete() {
        val firstName = mFragmentAddAthleteBinding!!.firstName.text.toString()
        val lastName = mFragmentAddAthleteBinding!!.lastName.text.toString()
        val email = mFragmentAddAthleteBinding!!.email.text.toString()
        if (viewModel.isFormDataValid(firstName, lastName, email)) {
            hideKeyboard()
            var athlete: Athlete = Athlete()
            athlete.first_name = firstName
            athlete.last_name = lastName
            athlete.email = email
//            athlete.createdAt = "January 21 2018" // TODO: Hardcoded
//            athlete.updatedAt = "January 21 2018" // TODO: Hardcoded data
//            athlete.organizationId = 0 // TODO: Hardcoded

            viewModel.addAthlete(athlete)
        } else {
            Toast.makeText(activity, getString(R.string.invalid_form_data), Toast.LENGTH_SHORT).show()
        }
    }

    override fun athleteAdded(athlete: Athlete) {
        showSuccess("Athlete Added", "")
        goBack()
    }

    override fun goBack() {
        (baseActivity as MainActivity).onBackPressed()
    }

    override fun handleError(throwable: Throwable) {
        showError("There was an error!", throwable.message!!)
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

        val TAG = AddAthleteFragment::class.java.simpleName

        fun newInstance(): AddAthleteFragment {
            val args = Bundle()
            val fragment = AddAthleteFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
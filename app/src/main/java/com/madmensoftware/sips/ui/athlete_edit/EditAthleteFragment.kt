package com.madmensoftware.sips.ui.athlete_edit

import android.os.Bundle
import android.view.*
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.R
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.ui.athlete_add.AddAthleteFragment
import com.madmensoftware.sips.ui.base.BaseFragment
import com.madmensoftware.sips.ui.main.MainActivity
import android.arch.lifecycle.Observer
import com.madmensoftware.sips.databinding.FragmentEditAthleteBinding
import kotlinx.android.synthetic.main.fragment_edit_athlete.*
import javax.inject.Inject
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.DatePicker
import java.util.*
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import android.databinding.adapters.TextViewBindingAdapter.setText
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog


class EditAthleteFragment : BaseFragment<FragmentEditAthleteBinding, EditAthleteViewModel>(), EditAthleteNavigator, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    @Inject
    override lateinit var viewModel: EditAthleteViewModel
        internal set

    var mFragmentEditAthleteBinding: FragmentEditAthleteBinding? = null

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_edit_athlete

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentEditAthleteBinding = viewDataBinding

        val athleteId: String = arguments!!.getString(KEY_ATHLETE_ID)
        subscribeToAthleteLiveData(athleteId)
    }

    private fun subscribeToAthleteLiveData(athleteId: String) {
        viewModel.athleteId = athleteId
        viewModel.fetchAthlete()
        viewModel.athleteLiveData.observe(this, Observer<Athlete> {
            athlete: Athlete? ->
            if (athlete != null) {
                mFragmentEditAthleteBinding!!.setAthlete(athlete)

                if (mFragmentEditAthleteBinding!!.athlete?.date_of_birth != null) {
                    btn_date_of_birth.setText(viewModel.formatDate(mFragmentEditAthleteBinding!!.athlete!!.date_of_birth!!))
                }
            }
        })
    }

    override fun editAthlete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goBack() {
        (baseActivity as MainActivity).onBackPressed()
    }

    override fun handleError(throwable: Throwable) {
        showError("There was an error!", throwable.message!!)
    }

    override fun openSelectBirthdayDialog(date_of_birth: String) {
        val cal = getCalendarFromISO(date_of_birth)
        val dpd = DatePickerDialog.newInstance(
                this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show(activity!!.fragmentManager, "Datepickerdialog")
    }

    override fun onDateSet(view: com.wdullaer.materialdatetimepicker.date.DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = "Birthday: " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year
        btn_date_of_birth.setText(date)
    }

    fun getCalendarFromISO(datestring: String): Calendar {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        try {
            val date = dateformat.parse(datestring)
            calendar.time = date
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return calendar
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

        val TAG = EditAthleteFragment::class.java.simpleName
        val KEY_ATHLETE_ID = "athleteId"

        fun newInstance(athleteId: String): EditAthleteFragment {
            val args = Bundle()
            args.putString(KEY_ATHLETE_ID, athleteId)
            val fragment = EditAthleteFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
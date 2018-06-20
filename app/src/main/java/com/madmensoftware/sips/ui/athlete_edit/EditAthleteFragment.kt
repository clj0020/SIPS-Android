package com.madmensoftware.sips.ui.athlete_edit

import android.os.Bundle
import android.view.*
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.R
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.ui.base.BaseFragment
import com.madmensoftware.sips.ui.main.MainActivity
import android.arch.lifecycle.Observer
import com.madmensoftware.sips.databinding.FragmentEditAthleteBinding
import kotlinx.android.synthetic.main.fragment_edit_athlete.*
import javax.inject.Inject
import java.util.*
import java.text.ParseException
import java.text.SimpleDateFormat
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import android.widget.*


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
                    input_date_of_birth.setText(mFragmentEditAthleteBinding!!.athlete!!.date_of_birth)
                }
                else {
                    btn_date_of_birth.setText(getString(R.string.hint_birthday))
                }

                setUpSpinner(mFragmentEditAthleteBinding!!.athlete)
            }
        })
    }

    override fun editAthlete() {
        val editedAthlete = mFragmentEditAthleteBinding!!.athlete!!
        editedAthlete.first_name = input_first_name.text.toString()
        editedAthlete.last_name = input_last_name.text.toString()
        editedAthlete.email = input_email.text.toString()
        editedAthlete.date_of_birth = input_date_of_birth.text.toString()

        // Check if integer value edit texts are empty
        // TODO: Refactor this to use TextUtils
        if (!input_height_feet.text.toString().equals("") || !input_height_inches.text.toString().equals("") || !input_weight.text.toString().equals("")) {
            editedAthlete.height = (input_height_feet.text.toString().toInt() * 12) + input_height_inches.text.toString().toInt()
            editedAthlete.weight = input_weight.text.toString().toInt()
        }
        
        editedAthlete.sport = sports_spinner_edit_athlete.selectedItem.toString()
        editedAthlete.position = position_spinner_edit_athlete.selectedItem.toString()

        if (viewModel.isFormDataValid(editedAthlete)) {
            hideKeyboard()
            viewModel.editAthlete(editedAthlete)
        } else {
            Toast.makeText(context, getString(R.string.invalid_form_data), Toast.LENGTH_SHORT).show()
        }
    }

    override fun athleteEdited() {
        showSuccess("Success", "Athlete profile has been edited!")
        goBack()
    }

    override fun goBack() {
        (baseActivity as MainActivity).onBackPressed()
    }

    override fun handleError(throwable: Throwable) {
        showError("There was an error!", throwable.message!!)
    }

    override fun openSelectBirthdayDialog(date_of_birth: String?) {
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
        val date = "" + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year
        btn_date_of_birth.setText(date)

        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val dateOb: Date = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        var birthday: String = dateFormat.format(dateOb)
        input_date_of_birth.setText(birthday)
    }

    fun getCalendarFromISO(datestring: String?): Calendar {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        if (datestring != null) {
            try {
                val date = dateformat.parse(datestring)
                calendar.time = date
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        return calendar
    }

    fun setUpSpinner(athlete: Athlete?) {
        val adapter = ArrayAdapter.createFromResource(context,
                R.array.sports_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sports_spinner_edit_athlete.setAdapter(adapter)
        sports_spinner_edit_athlete.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                setUpPositionSpinner(parentView.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }

        if (athlete?.sport != null) {
            // Find a match for current
            val androidStrings = resources.getStringArray(R.array.sports_array)
            for (s in androidStrings) {
                val i = s.indexOf(athlete.sport!!)
                if (i >= 0) {
                    sports_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                }
            }

            // Show corresponding positions based on sport
            when (athlete.sport!!) {
                "Baseball" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.baseball_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.baseball_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Basketball" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.basketball_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.basketball_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Cheerleading" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.cheerleading_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.cheerleading_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Cross Country Running" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.cross_country_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.cross_country_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Football" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.football_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.football_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Golf" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.golf_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.golf_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Hockey" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.hockey_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.hockey_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Soccer" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.soccer_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.soccer_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Swimming and Diving" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.swimming_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.swimming_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Tennis" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.tennis_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.tennis_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Track and Field" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.track_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.track_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Volleyball" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.volleyball_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.volleyball_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
                "Wrestling" -> {
                    val position_adapter = ArrayAdapter.createFromResource(context,
                            R.array.wrestling_positions_array, android.R.layout.simple_spinner_item)
                    position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    position_spinner_edit_athlete.setAdapter(position_adapter)

                    val strings = resources.getStringArray(R.array.wrestling_positions_array)
                    for (s in strings) {
                        val i = s.indexOf(athlete.position!!)
                        if (i >= 0) {
                            position_spinner_edit_athlete.setSelection(androidStrings.indexOf(s))
                        }
                    }
                }
            }
        }
    }

    fun setUpPositionSpinner(sport: String) {
        when (sport) {
            "Baseball" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.baseball_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Basketball" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.basketball_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Cheerleading" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.cheerleading_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Cross Country Running" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.cross_country_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Football" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.football_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Golf" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.golf_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Hockey" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.hockey_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Soccer" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.soccer_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Swimming and Diving" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.swimming_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Tennis" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.tennis_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Track and Field" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.track_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Volleyball" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.volleyball_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
            "Wrestling" -> {
                val position_adapter = ArrayAdapter.createFromResource(context,
                        R.array.wrestling_positions_array, android.R.layout.simple_spinner_item)
                position_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_spinner_edit_athlete.setAdapter(position_adapter)
            }
        }
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
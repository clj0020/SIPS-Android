package com.madmensoftware.sips.ui.athlete

import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.madmensoftware.sips.BR
import com.madmensoftware.sips.R
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.databinding.FragmentAthleteBinding

import com.madmensoftware.sips.ui.base.BaseFragment
import com.madmensoftware.sips.ui.main.MainActivity
import javax.inject.Inject
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.animation.AlphaAnimation
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AlertDialog
import java.io.File
import android.os.Environment
import android.support.v4.content.FileProvider
import com.madmensoftware.sips.util.PictureUtils
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by clj00 on 3/4/2018.
 */
class AthleteFragment : BaseFragment<FragmentAthleteBinding, AthleteViewModel>(), AthleteNavigator, TestDataListAdapter.TestDataAdapterListener, AppBarLayout.OnOffsetChangedListener {

    @Inject
    override lateinit var viewModel: AthleteViewModel
        internal set

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager

    @Inject
    lateinit var mTestDataAdapter: TestDataListAdapter

    var mFragmentAthleteBinding: FragmentAthleteBinding? = null

    override val bindingVariable: Int
        get() = BR.viewModel

    /** Sets layout file. **/
    override val layoutId: Int
        get() = R.layout.fragment_athlete

    /** Sets navigator and test data list adapter. **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        mTestDataAdapter.setListener(this)
    }

    /** Sets binding, sets up UI, subscribes to athlete object **/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentAthleteBinding = viewDataBinding

        setUp()

        val athleteId: String = arguments!!.getString(KEY_ATHLETE_ID)

        subscribeToAthleteLiveData(athleteId)
    }

    /** Just sets options menu. **/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /** Detaches test data list recyclerview when fragment is paused. **/
    override fun onPause() {
        super.onPause()
        mLayoutManager.detachAndScrapAttachedViews(mFragmentAthleteBinding!!.testDataListRecyclerView.Recycler())
    }

    /** Sets up UI elements. **/
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

    /** Subscribes to athlete retrieval call. **/
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

    /** Subscribes to test data retrieval call. **/
    private fun subscribeToTestDataLiveData() {
        viewModel.fetchTestData()
        viewModel.testDataListLiveData.observe(this, Observer<List<TestData>> {
            testDataList -> viewModel.addTestDataItemsToList(testDataList!!)
        })
    }

    /** Adds test data items to the test data list adapter. **/
    override fun updateTestDataList(testDataList: List<TestData>) {
        mTestDataAdapter.addItems(testDataList)
    }

    /** Sets the swipe refresh refreshing indicator. **/
    override fun setRefreshing(refreshing: Boolean) {
        mFragmentAthleteBinding!!.profileSwipeRefresh.isRefreshing = refreshing
    }

    /** Called when Test Data is empty and user clicks retry. **/
    override fun onRetryClick() {
        viewModel.fetchTestData()
    }

    /** Called when there is an error in the ViewModel. Shows alert dialog with error message. **/
    override fun handleError(throwable: Throwable) {
        // TODO: handle error
        showError("Oh No!", throwable.message!!)
    }

    /** Shows Test Athlete Fragment **/
    override fun showTestAthleteFragment(athleteId: String) {
        (activity as MainActivity).viewModel.onTestAthlete(athleteId)
    }

    /** Shows Edit Athlete Fragment **/
    override fun showEditAthleteFragment(athleteId: String) {
        (activity as MainActivity).viewModel.onEditAthlete(athleteId)
    }

    /** Shows a dialog that lets users change profile picture either with camera or from gallery. **/
    override fun showChangeProfilePictureDialog() {
        // setup the alert builder
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Profile Picture")
        builder.setMessage("Would you like to change your profile picture?")

        // Take Photo
        builder.setPositiveButton("Take Picture", { dialog, which ->
            openCameraIntent()
        })
        // Choose Photo
        builder.setNegativeButton("Choose Picture", { dialog, which ->
            openGalleryIntent()
        })
        builder.setNeutralButton("Cancel", null)

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    /** This is called when the camera or choose photo activities return back. **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)

        // When sending to activity and back, the activity changes the request code. This changes it back.
        val originalRequestCode = requestCode and 0xffff

        when (originalRequestCode) {
            // take picture chosen and camera app opens then comes back with a selected image.
            KEY_TAKE_PICTURE -> if (resultCode == Activity.RESULT_OK) {
                // Upload new picture and replace last picture
                viewModel.uploadProfileImage(File(viewModel.imageFilePath))
            }
            // select photo from gallery chosen and gallery app opens then comes back with a selected image.
            KEY_CHOOSE_PICTURE -> if (resultCode == Activity.RESULT_OK) {
                if (imageReturnedIntent != null) {
                    val selectedImageUri: Uri = imageReturnedIntent.data
                    val path = PictureUtils.getFilePathFromURI(context!!, selectedImageUri)
                    if (path != null) {
                        viewModel.uploadProfileImage(File(path))
                    }
                }
            }
        }
    }

    /** Opens the Camera Activity for taking a profile picture and saving the file to the DB **/
    private fun openCameraIntent() {
        val pictureIntent = Intent(
                MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(activity?.getPackageManager()) != null) {
            //Create a file to store the image
            var photoFile: File?  = null
            try {
                photoFile = createImageFile()
            }
            catch (ex:IOException) {
                // Error occurred while creating the File
            }
            if (photoFile != null && context != null) {
                val photoURI = FileProvider.getUriForFile(context!!, "com.madmensoftware.sips.android.provider", photoFile)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(pictureIntent, KEY_TAKE_PICTURE)
            }
        }
    }

    /** Opens the Choose Photo Activity for choosing a profile picture and saving the file to the DB **/
    private fun openGalleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), KEY_CHOOSE_PICTURE)
    }

    /** Creates a temporary image file for taking a new profile picture. **/
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        viewModel.imageFilePath = image.getAbsolutePath();
        return image
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

    /** Handles animation when scrolling. **/
    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)

        mFragmentAthleteBinding!!.profileSwipeRefresh.setEnabled(offset == 0)
    }

    /** Changes toolbar visibility and animation based on Scroll **/
    // TODO: Move to Util class. May need to keep it here.
    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= viewModel.PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!viewModel.mIsTheTitleVisible) {
                startAlphaAnimation(mFragmentAthleteBinding!!.athleteName, viewModel.ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                viewModel.mIsTheTitleVisible = true
            }
        }
        else {
            if (viewModel.mIsTheTitleVisible) {
                startAlphaAnimation(mFragmentAthleteBinding!!.athleteName, viewModel.ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                viewModel.mIsTheTitleVisible = false
            }
        }
    }

    /** Changes Toolbar Title based on Scroll **/
    // TODO: Move to Util class. May need to keep it here.
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

    /** Animates toolbar elements. **/
    // TODO: Move to Util class. May need to keep it here.
    private fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE)
            AlphaAnimation(0f, 1f)
        else
            AlphaAnimation(1f, 0f)

        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }

    /** For initialization of fragment and constants. **/
    companion object {
        val TAG = AthleteFragment::class.java.simpleName
        val KEY_ATHLETE_ID = "athleteId"
        val KEY_TAKE_PICTURE = 0
        val KEY_CHOOSE_PICTURE = 1

        fun newInstance(athleteId: String): AthleteFragment {
            val args = Bundle()
            args.putString(KEY_ATHLETE_ID, athleteId)
            val fragment = AthleteFragment()
            fragment.setArguments(args)
            return fragment
        }
    }
}
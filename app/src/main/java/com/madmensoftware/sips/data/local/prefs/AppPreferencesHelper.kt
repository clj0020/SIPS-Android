package com.madmensoftware.sips.data.local.prefs

import android.content.Context
import com.madmensoftware.sips.data.DataManager
import com.madmensoftware.sips.util.AppConstants
import com.madmensoftware.sips.di.PreferenceInfo
import javax.inject.Inject
import android.content.SharedPreferences



/**
 * Created by clj00 on 3/2/2018.
 */
class AppPreferencesHelper @Inject
constructor(context: Context, @PreferenceInfo prefFileName: String) : PreferencesHelper {

    private val mPrefs: SharedPreferences

    override var accessToken: String?
        get() = mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null)
        set(accessToken) = mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply()

    override var currentUserId: String?
        get() {
            val userId = mPrefs.getString(PREF_KEY_CURRENT_USER_ID, AppConstants.NULL_INDEX)
            return if (userId == AppConstants.NULL_INDEX) null else userId
        }
        set(userId) {
            val id = userId ?: AppConstants.NULL_INDEX
            mPrefs.edit().putString(PREF_KEY_CURRENT_USER_ID, id).apply()
        }

    override var currentUserFirstName: String?
        get() = mPrefs.getString(PREF_KEY_CURRENT_USER_FIRST_NAME, null)
        set(userFirstName) = mPrefs.edit().putString(PREF_KEY_CURRENT_USER_FIRST_NAME, userFirstName).apply()

    override var currentUserOrganizationId: String?
        get() {
            val organizationId = mPrefs.getString(PREF_KEY_CURRENT_USER_ORGANIZATION, AppConstants.NULL_INDEX)
            return if (organizationId == AppConstants.NULL_INDEX) null else organizationId
        }
        set(userId) {
            val organizationId = currentUserOrganizationId
            mPrefs.edit().putString(PREF_KEY_CURRENT_USER_ORGANIZATION, userId).apply()
        }


    override var currentUserLastName: String?
        get() = mPrefs.getString(PREF_KEY_CURRENT_USER_LAST_NAME, null)
        set(userLastName) = mPrefs.edit().putString(PREF_KEY_CURRENT_USER_LAST_NAME, userLastName).apply()

    override var currentUserEmail: String?
        get() = mPrefs.getString(PREF_KEY_CURRENT_USER_EMAIL, null)
        set(email) = mPrefs.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply()

    override val currentUserLoggedInMode: Int
        get() = mPrefs.getInt(PREF_KEY_USER_LOGGED_IN_MODE,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.type)

    override var currentUserProfilePicUrl: String?
        get() = mPrefs.getString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, null)
        set(profilePicUrl) =
            mPrefs.edit().putString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, profilePicUrl).apply()

    //    override var currentUserOrganization: Any?
//        get() {
//            val organizationId = mPrefs.getLong(PREF_KEY_ORGANIZATION_ID, AppConstants.NULL_INDEX)
//            return if (organizationId == AppConstants.NULL_INDEX) null else organizationId
//        }
//        set(organizationId) {
//            val id = organizationId ?: AppConstants.NULL_INDEX
//            mPrefs.edit().putLong(PREF_KEY_ORGANIZATION_ID, id).apply()
//        }

    override var currentUserStatus: String?
        get() = mPrefs.getString(PREF_KEY_CURRENT_USER_STATUS, null)
        set(status) = mPrefs.edit().putString(PREF_KEY_CURRENT_USER_STATUS, status).apply()

    override var currentUserKind: String?
        get() = mPrefs.getString(PREF_KEY_CURRENT_USER_KIND, null)
        set(kind) = mPrefs.edit().putString(PREF_KEY_CURRENT_USER_KIND, kind).apply()


    init {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    }

    override fun setCurrentUserLoggedInMode(mode: DataManager.LoggedInMode) {
        mPrefs.edit().putInt(PREF_KEY_USER_LOGGED_IN_MODE, mode.type).apply()
    }

    companion object {

        private val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"

        private val PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL"

        private val PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID"

        private val PREF_KEY_CURRENT_USER_FIRST_NAME = "PREF_KEY_CURRENT_USER_FIRST_NAME"

        private val PREF_KEY_CURRENT_USER_LAST_NAME = "PREF_KEY_CURRENT_USER_LAST_NAME"

        private val PREF_KEY_CURRENT_USER_ORGANIZATION = "PREF_KEY_CURRENT_USER_ORGANIZATION"

        private val PREF_KEY_CURRENT_USER_STATUS = "PREF_KEY_CURRENT_USER_STATUS"

        private val PREF_KEY_CURRENT_USER_KIND = "PREF_KEY_CURRENT_USER_KIND"

        private val PREF_KEY_CURRENT_USER_PROFILE_PIC_URL = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL"

        private val PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE"

        private val PREF_KEY_ORGANIZATION_ID = "PREF_KEY_ORGANIZATION_ID"
    }
}
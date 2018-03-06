package com.madmensoftware.sips.data.local.prefs

import com.madmensoftware.sips.data.DataManager



/**
 * Created by clj00 on 3/2/2018.
 */
interface PreferencesHelper {

    var accessToken: String?

    var currentUserEmail: String?

    var currentUserId: Long?

    val currentUserLoggedInMode: Int

    var currentUserName: String?

    var currentUserProfilePicUrl: String?

    var currentUserOrganizationId: Long?

    fun setCurrentUserLoggedInMode(mode: DataManager.LoggedInMode)

}
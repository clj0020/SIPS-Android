package com.madmensoftware.sips.data.local.prefs

import com.madmensoftware.sips.data.DataManager



/**
 * Created by clj00 on 3/2/2018.
 */
interface PreferencesHelper {

    var accessToken: String?

    var currentUserId: String?

    var currentUserFirstName: String?

    var currentUserLastName: String?

    var currentUserEmail: String?

    val currentUserLoggedInMode: Int

    var currentUserProfilePicUrl: String?

    var currentUserOrganizationId: String?

    var currentUserStatus: String?

    var currentUserKind: String?

    fun setCurrentUserLoggedInMode(mode: DataManager.LoggedInMode)

}
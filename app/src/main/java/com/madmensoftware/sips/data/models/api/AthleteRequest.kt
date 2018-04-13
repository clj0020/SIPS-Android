package com.madmensoftware.sips.data.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by clj00 on 3/2/2018.
 */

object AthleteRequest {

    class GetAllAthletesFromOrganizationRequest(@field:Expose
                             @field:SerializedName("organizationId")
                             val organizationId: String?
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }

            val that = other as GetAllAthletesFromOrganizationRequest?

            return if (organizationId != null) organizationId == that?.organizationId else that?.organizationId == null
        }

        override fun hashCode(): Int {
            return organizationId?.hashCode() ?: 0
        }
    }
}// This class is not publicly instantiable

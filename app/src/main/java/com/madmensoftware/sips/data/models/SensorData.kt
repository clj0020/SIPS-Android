package com.madmensoftware.sips.data.models

import android.arch.persistence.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SensorData {

    @Expose
    @SerializedName("_id")
    var _id: String? = null

    @Expose
    @SerializedName("time")
    var time: Float? = null

    @Expose
    @SerializedName("x")
    var x: Float? = null

    @Expose
    @SerializedName("y")
    var y: Float? = null

    @Expose
    @SerializedName("z")
    var z: Float? = null

}
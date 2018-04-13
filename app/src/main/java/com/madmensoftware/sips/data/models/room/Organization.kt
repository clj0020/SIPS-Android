package com.madmensoftware.sips.data.models.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by clj00 on 3/6/2018.
 */
@Entity(tableName = "organizations")
class Organization {

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null

    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = ""

    @Expose
    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String? = null

    @Expose
    @SerializedName("creator")
    @ColumnInfo(name = "creator")
    var creator: String? = null


}
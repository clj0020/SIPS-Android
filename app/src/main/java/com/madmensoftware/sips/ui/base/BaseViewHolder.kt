package com.madmensoftware.sips.ui.base

import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * Created by clj00 on 3/2/2018.
 */
abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(position: Int)
}
package com.madmensoftware.sips.ui.athlete_list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.madmensoftware.sips.data.models.room.Athlete
import com.madmensoftware.sips.databinding.ItemAthleteEmptyViewBinding
import com.madmensoftware.sips.databinding.ItemAthleteViewBinding
import com.madmensoftware.sips.ui.base.BaseViewHolder
import com.madmensoftware.sips.ui.main.MainActivity


/**
 * Created by clj00 on 3/2/2018.
 */

class AthleteListAdapter(val mAthleteList: MutableList<Athlete>?) : RecyclerView.Adapter<BaseViewHolder>() {

    private var mListener: AthleteAdapterListener? = null

    override fun getItemCount(): Int {
        return if (mAthleteList != null && mAthleteList.size > 0) {
            mAthleteList.size
        } else {
            1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mAthleteList != null && !mAthleteList.isEmpty()) {
            VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_EMPTY
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val athleteViewBinding = ItemAthleteViewBinding.inflate(LayoutInflater.from(parent.context),
                        parent, false)
                return AthleteViewHolder(athleteViewBinding)
            }
            VIEW_TYPE_EMPTY -> {
                val emptyViewBinding = ItemAthleteEmptyViewBinding.inflate(LayoutInflater.from(parent.context),
                        parent, false)
                return EmptyViewHolder(emptyViewBinding)
            }
            else -> {
                val emptyViewBinding = ItemAthleteEmptyViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return EmptyViewHolder(emptyViewBinding)
            }
        }
    }

    fun addItems(athleteList: List<Athlete>) {
        mAthleteList!!.addAll(athleteList)
        notifyDataSetChanged()
    }

    fun addItem(athlete: Athlete) {
        mAthleteList!!.add(athlete)
        notifyDataSetChanged()
    }

    fun clearItems() {
        mAthleteList!!.clear()
    }

    fun setListener(listener: AthleteAdapterListener) {
        this.mListener = listener
    }

    fun setSearchResult(result: List<Athlete>) {
        clearItems()
        addItems(result)
    }

    interface AthleteAdapterListener {
        fun onRetryClick()
    }

    inner class AthleteViewHolder(private val mBinding: ItemAthleteViewBinding) : BaseViewHolder(mBinding.getRoot()), AthleteItemViewModel.AthleteItemViewModelListener {

        private var mAthleteItemViewModel: AthleteItemViewModel? = null

        override fun onBind(position: Int) {
            val athlete = mAthleteList!![position]
            mAthleteItemViewModel = AthleteItemViewModel(athlete, this)
            mBinding.setViewModel(mAthleteItemViewModel)

            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings()
        }

        override fun onItemClick(id: String) {
            (itemView.context as MainActivity).viewModel.onAthleteDetails(id)
        }
    }

    inner class EmptyViewHolder(private val mBinding: ItemAthleteEmptyViewBinding) : BaseViewHolder(mBinding.getRoot()), AthleteEmptyItemViewModel.AthleteEmptyItemViewModelListener {

        override fun onBind(position: Int) {
            val emptyItemViewModel = AthleteEmptyItemViewModel(this)
            mBinding.setViewModel(emptyItemViewModel)
        }

        override fun onRetryClick() {
            mListener!!.onRetryClick()
        }
    }



    companion object {

        val VIEW_TYPE_EMPTY = 0

        val VIEW_TYPE_NORMAL = 1
    }
}
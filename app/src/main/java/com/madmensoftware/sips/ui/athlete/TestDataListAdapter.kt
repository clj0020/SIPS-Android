package com.madmensoftware.sips.ui.athlete

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.madmensoftware.sips.data.models.room.TestData
import com.madmensoftware.sips.databinding.ItemTestDataEmptyViewBinding
import com.madmensoftware.sips.databinding.ItemTestDataViewBinding
import com.madmensoftware.sips.ui.base.BaseViewHolder

/**
 * Created by clj00 on 3/4/2018.
 */
class TestDataListAdapter(val mTestDataList: MutableList<TestData>?) : RecyclerView.Adapter<BaseViewHolder>() {

    private var mListener: TestDataAdapterListener? = null

    override fun getItemCount(): Int {
        return if (mTestDataList != null && mTestDataList.size > 0) {
            mTestDataList.size
        } else {
            1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mTestDataList != null && !mTestDataList.isEmpty()) {
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
                val testDataViewBinding = ItemTestDataViewBinding.inflate(LayoutInflater.from(parent.context),
                        parent, false)
                return TestDataViewHolder(testDataViewBinding)
            }
            VIEW_TYPE_EMPTY -> {
                val emptyViewBinding = ItemTestDataEmptyViewBinding.inflate(LayoutInflater.from(parent.context),
                        parent, false)
                return EmptyViewHolder(emptyViewBinding)
            }
            else -> {
                val emptyViewBinding = ItemTestDataEmptyViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return EmptyViewHolder(emptyViewBinding)
            }
        }
    }

    fun addItems(testDataList: List<TestData>) {
        mTestDataList!!.addAll(testDataList)
        notifyDataSetChanged()
    }

    fun addItem(testData: TestData) {
        mTestDataList!!.add(testData)
        notifyDataSetChanged()
    }

    fun clearItems() {
        mTestDataList!!.clear()
    }

    fun setListener(listener: TestDataAdapterListener) {
        this.mListener = listener
    }

    interface TestDataAdapterListener {

        fun onRetryClick()
    }

    inner class TestDataViewHolder(private val mBinding: ItemTestDataViewBinding) : BaseViewHolder(mBinding.getRoot()), TestDataItemViewModel.TestDataItemViewModelListener {

        private var mTestDataItemViewModel: TestDataItemViewModel? = null

        override fun onBind(position: Int) {
            val testData = mTestDataList!![position]
            mTestDataItemViewModel = TestDataItemViewModel(testData, this)
            mBinding.setViewModel(mTestDataItemViewModel)

            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings()
        }

        override fun onItemClick(id: String) {
//            (itemView.context.applicationContext as MainActivity).showAthleteFragment(athleteId)
        }
    }

    inner class EmptyViewHolder(private val mBinding: ItemTestDataEmptyViewBinding) : BaseViewHolder(mBinding.getRoot()), TestDataEmptyItemViewModel.TestDataEmptyItemViewModelListener {

        override fun onBind(position: Int) {
            val emptyItemViewModel = TestDataEmptyItemViewModel(this)
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
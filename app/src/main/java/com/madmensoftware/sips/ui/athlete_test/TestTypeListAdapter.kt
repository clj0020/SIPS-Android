package com.madmensoftware.sips.ui.athlete_test

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.madmensoftware.sips.data.models.room.TestType
import com.madmensoftware.sips.databinding.ItemTestTypeEmptyViewBinding
import com.madmensoftware.sips.databinding.ItemTestTypeViewBinding
import com.madmensoftware.sips.ui.base.BaseViewHolder
import com.madmensoftware.sips.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*


class TestTypeListAdapter(val mTestTypeList: MutableList<TestType>?) : RecyclerView.Adapter<BaseViewHolder>() {

    private var mListener: TestTypeAdapterListener? = null

    override fun getItemCount(): Int {
        return if (mTestTypeList != null && mTestTypeList.size > 0) {
            mTestTypeList.size
        } else {
            1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mTestTypeList != null && !mTestTypeList.isEmpty()) {
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
                val testTypeViewBinding = ItemTestTypeViewBinding.inflate(LayoutInflater.from(parent.context),
                        parent, false)
                return TestTypeViewHolder(testTypeViewBinding)
            }
            VIEW_TYPE_EMPTY -> {
                val emptyViewBinding = ItemTestTypeEmptyViewBinding.inflate(LayoutInflater.from(parent.context),
                        parent, false)
                return EmptyViewHolder(emptyViewBinding)
            }
            else -> {
                val emptyViewBinding = ItemTestTypeEmptyViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return EmptyViewHolder(emptyViewBinding)
            }
        }
    }

    fun addItems(testTypeList: List<TestType>) {
        mTestTypeList!!.addAll(testTypeList)
        notifyDataSetChanged()
    }

    fun addItem(testType: TestType) {
        mTestTypeList!!.add(testType)
        notifyDataSetChanged()
    }

    fun clearItems() {
        mTestTypeList!!.clear()
    }

    fun setListener(listener: TestTypeAdapterListener) {
        this.mListener = listener
    }

    fun setSearchResult(result: List<TestType>) {
        clearItems()
        addItems(result)
    }

    interface TestTypeAdapterListener {
        fun onRetryClick()
    }

    inner class TestTypeViewHolder(private val mBinding: ItemTestTypeViewBinding) : BaseViewHolder(mBinding.getRoot()), TestTypeItemViewModel.TestTypeItemViewModelListener {

        private var mTestTypeItemViewModel: TestTypeItemViewModel? = null

        override fun onBind(position: Int) {
            val athlete = mTestTypeList!![position]
            mTestTypeItemViewModel = TestTypeItemViewModel(athlete, this)
            mBinding.setViewModel(mTestTypeItemViewModel)

            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings()
        }

        override fun onItemClick(testType: TestType) {
            val activity: MainActivity = itemView.context as MainActivity
            (activity.supportFragmentManager.findFragmentByTag(activity.mFragmentStack.peek()) as TestAthleteFragment).viewModel.onTestTypeSelected(testType)
        }
    }

    inner class EmptyViewHolder(private val mBinding: ItemTestTypeEmptyViewBinding) : BaseViewHolder(mBinding.getRoot()), TestTypeEmptyItemViewModel.TestTypeEmptyItemViewModelListener {

        override fun onBind(position: Int) {
            val emptyItemViewModel = TestTypeEmptyItemViewModel(this)
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
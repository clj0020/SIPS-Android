package com.madmensoftware.sips.ui.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import dagger.android.support.AndroidSupportInjection
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.madmensoftware.sips.R


/**
 * Created by clj00 on 3/2/2018.
 */
abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>>: Fragment() {

    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null

    private lateinit var mSuccessDialog: SweetAlertDialog
    private lateinit var mErrorDialog: SweetAlertDialog

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    init {
    }

    val isNetworkConnected: Boolean
        get() = baseActivity != null && baseActivity!!.isNetworkConnected

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context
            this.baseActivity = activity
            activity.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        mViewModel = viewModel
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = viewDataBinding!!.getRoot()
        return mRootView
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }

    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }

    fun showSuccess(title: String, content: String) {
        mSuccessDialog = SweetAlertDialog(baseActivity, SweetAlertDialog.SUCCESS_TYPE)
        mSuccessDialog.titleText = title
        mSuccessDialog.contentText = content
        mSuccessDialog.show()
    }

    fun hideSuccess() {
        mSuccessDialog.hide()
    }

    fun showError(title: String, content: String) {
        mErrorDialog = SweetAlertDialog(baseActivity, SweetAlertDialog.ERROR_TYPE)
        mErrorDialog.titleText = title
        mErrorDialog.contentText = content
        mErrorDialog.show()
    }

    fun hideError() {
        mErrorDialog.hide()
    }

    fun openActivityOnTokenExpire() {
        if (baseActivity != null) {
            baseActivity!!.openActivityOnTokenExpire()
        }
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }
}
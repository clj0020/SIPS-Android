package com.madmensoftware.sips.ui.base

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

/**
 * Created by clj00 on 3/2/2018.
 */
abstract class BaseDialog : DialogFragment() {

    var baseActivity: BaseActivity<*, *>? = null
        private set

    val isNetworkConnected: Boolean
        get() = baseActivity != null && baseActivity!!.isNetworkConnected

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val mActivity = context
            this.baseActivity = mActivity
            mActivity.onFragmentAttached()
        }
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        // the content
        val root = RelativeLayout(getActivity())
        root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        // creating the fullscreen dialog
        val dialog = Dialog(getContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun show(fragmentManager: FragmentManager, tag: String) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    fun dismissDialog(tag: String) {
        dismiss()
        baseActivity!!.onFragmentDetached(tag)
    }

    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }

    fun hideLoading() {
        if (baseActivity != null) {
            baseActivity!!.hideLoading()
        }
    }

    fun openActivityOnTokenExpire() {
        if (baseActivity != null) {
            baseActivity!!.openActivityOnTokenExpire()
        }
    }

    fun showLoading() {
        if (baseActivity != null) {
            baseActivity!!.showLoading()
        }
    }
}
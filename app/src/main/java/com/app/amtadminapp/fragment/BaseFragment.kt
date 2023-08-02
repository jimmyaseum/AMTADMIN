package com.app.amtadminapp.fragment

import android.app.ProgressDialog
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.amtadminapp.R
import com.app.amtadminapp.utils.CommonUtil
import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Jimmy
 */
abstract class BaseFragment : Fragment() {

    private var progressDialog: ProgressDialog? = null

    abstract fun initializeView()
    var flagTemp = true

    fun hideProgress() {
        if (progressDialog != null)
            progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    fun showProgress() {
        hideProgress()
        progressDialog = CommonUtil.showLoadingDialog(activity!!)
    }

    fun setSwipeRefreshColorSchemes(swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(activity!!, R.color.colorPrimary),
            ContextCompat.getColor(activity!!, R.color.colorAccent),
            ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
        )
    }

    fun replaceFragment(containerViewId: Int, fragment: Fragment, tag: String) {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(containerViewId, fragment, tag)
        fragmentTransaction?.commit()
    }

    fun setErrorForTIL(enabled: Boolean, editText: EditText, message: String) {

        if (editText.parent is ViewGroup && (editText.parent as? ViewGroup)?.parent is TextInputLayout) {
            if (enabled) {
                ((editText.parent as? ViewGroup)?.parent as?  TextInputLayout)?.error = message
            } else {
                ((editText.parent as? ViewGroup)?.parent as?  TextInputLayout)?.isErrorEnabled = false
            }
        } else {
            if (enabled) {
                editText.error = message
            }
        }
    }

    fun setTILError(editText: EditText, message: String): Boolean {

        if (editText.parent is ViewGroup && (editText.parent as? ViewGroup)?.parent is TextInputLayout) {

            if (editText.text.isEmpty()) {
                ((editText.parent as? ViewGroup)?.parent as?  TextInputLayout)?.error = message
                return false
            } else {
                ((editText.parent as? ViewGroup)?.parent as?  TextInputLayout)?.isErrorEnabled = false
                return true
            }
        } else {
            return false
        }
    }

    // Check EditText is blank with Optional Array of Id but right now Optional Array is commented
    // v- ViewGroup and isTemp - by default it is true means EditText is fill
    //    fun checkIfFieldLeftBlank(v: ViewGroup, optionalParams: IntArray) {
    fun checkIfFieldLeftBlank(v: ViewGroup, isTemp: Boolean): Boolean {

        flagTemp = isTemp

        for (i in 0 until v.childCount) {
//            LogUtil.e(TAG, "===========childCount====$i=====$flagTemp")
            if (v.getChildAt(i) is EditText) {


//                if ((v.getChildAt(i) as? EditText)?.text.toString().isEmpty() && !(optionalParams.contains(v.getChildAt(i).id))) {
                if ((v.getChildAt(i) as? EditText)?.text.toString().isEmpty()) {
                    flagTemp = false
                    /*edit text is empty*/
//                    LogUtil.e(TAG, "===========is Empty====" + (v.getChildAt(i) as? EditText))
                }
//                LogUtil.e(TAG, "Parent is Layout " + ((v.getChildAt(i) as? EditText)?.parent is TextInputLayout))
            } else if (v.getChildAt(i) is ViewGroup) {
                checkIfFieldLeftBlank((v.getChildAt(i) as? ViewGroup)!!, flagTemp)
            }
        }
//        LogUtil.e(TAG, "===========flag====$flag=========$flagTemp")

        return flagTemp
    }

    // Pass EditText object like validateEditTexts(etName,etMobile,etEmail)
    fun validateEditTexts(vararg editText: EditText): Boolean {
        for (i in editText.indices) {
            if (editText[i].text.toString().trim { it <= ' ' } == "")
                return false
        }
        return true
    }
}
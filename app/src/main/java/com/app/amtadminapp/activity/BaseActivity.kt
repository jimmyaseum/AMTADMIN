package com.app.amtadminapp.activity

import android.app.ProgressDialog
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.amtadminapp.R
import com.app.amtadminapp.utils.CommonUtil.showLoadingDialog
import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Jimmy
 */
abstract class BaseActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null

    abstract fun initializeView()
    var flagTemp = true
    var flagEnable = true

    fun hideProgress() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    fun showProgress() {
        hideProgress()
        progressDialog = showLoadingDialog(this)
    }

    fun setSwipeRefreshColorSchemes(swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.colorPrimary),
            ContextCompat.getColor(this, R.color.colorAccent),
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        )
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
//            LogUtil.e(TAG, "====childCount====$i=====$flagTemp")
            if (v.getChildAt(i) is EditText) {

//                if ((v.getChildAt(i) as? EditText)?.text.toString().isEmpty() && !(optionalParams.contains(v.getChildAt(i).id))) {
                if ((v.getChildAt(i) as? EditText)?.text.toString().isEmpty()) {
                    flagTemp = false
                    /*edit text is empty*/
                }
            } else if (v.getChildAt(i) is ViewGroup) {
                checkIfFieldLeftBlank((v.getChildAt(i) as? ViewGroup)!!, flagTemp)
            }
        }

        return flagTemp
    }

    fun enableDisableTIL(v: ViewGroup, isEnable: Boolean) {

        for (i in 0 until v.childCount) {
            if (v.getChildAt(i) is EditText) {
                (v.getChildAt(i) as? EditText)?.isEnabled = isEnable
            } else if (v.getChildAt(i) is ViewGroup) {
                enableDisableTIL((v.getChildAt(i) as? ViewGroup)!!, isEnable)
            }
        }
    }

    fun enableDisableViewGroup(v: ViewGroup, isEnable: Boolean) {

        for (i in 0 until v.childCount) {
            if (v.getChildAt(i) is TextView) {
                (v.getChildAt(i) as? TextView)?.isEnabled = isEnable
            } else if (v.getChildAt(i) is EditText) {
                (v.getChildAt(i) as? EditText)?.isEnabled = isEnable
//            } else if (v.getChildAt(i) is TextInputLayout) { //commented due to light gray place holder displayed
//                (v.getChildAt(i) as? TextInputLayout)?.isEnabled = isEnable
            } else if (v.getChildAt(i) is RadioButton) {
                (v.getChildAt(i) as? RadioButton)?.isEnabled = isEnable
            } else if (v.getChildAt(i) is CheckBox) {
                (v.getChildAt(i) as? CheckBox)?.isEnabled = isEnable
            } else if (v.getChildAt(i) is ImageView) {
                (v.getChildAt(i) as? ImageView)?.isEnabled = isEnable
            } else if (v.getChildAt(i) is RecyclerView) {
                (v.getChildAt(i) as? RecyclerView)?.isEnabled = isEnable
            } else if (v.getChildAt(i) is ViewGroup) {
                enableDisableViewGroup((v.getChildAt(i) as? ViewGroup)!!, isEnable)
            }
        }
    }

    /*Show and Hide Drawable Right in EditText*/
    fun showHideEditTextDrawableRight(isShow: Boolean, editText: EditText, drawable: Int) {
        if (isShow) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }
}
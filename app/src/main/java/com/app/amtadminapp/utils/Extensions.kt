package com.app.amtadminapp.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.utils.AppConstant.BLUR_RADIUS
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.reflect.TypeToken
import jp.wasabeef.glide.transformations.BlurTransformation
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created by Jimmy
 */
fun RecyclerView.setManager(
    isItHorizontal: Boolean = false,
    isItGrid: Boolean = false,
    spanCount: Int = 2
) {
    if (isItGrid)
        this.layoutManager = GridLayoutManager(this.context, spanCount)
    else {
        if (isItHorizontal)
            this.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        else
            this.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
    }


}

fun getRequestJSONBody(value: String): RequestBody =
    RequestBody.create(MediaType.parse("application/json"), value)

fun ImageView.loadUrl(url: String? = "", placeholder: Int) {
//    println("=======url======="+url)
    Glide.with(context).load(url).apply(RequestOptions().placeholder(placeholder)).into(this)
}

fun ImageView.loadUrlRounded(url: String? = "", placeholder: Int) {
    Glide.with(context).load(url).apply(RequestOptions().circleCrop().placeholder(placeholder)).into(this)
}

fun ImageView.loadDrawableRounded(placeholder: Int) {
    Glide.with(context).load(placeholder).apply(RequestOptions().circleCrop().placeholder(placeholder)).into(this)
}

fun ImageView.loadDrawableRounded(resourceID: Int, placeholder: Int) {
    Glide.with(context).load(resourceID).apply(RequestOptions().circleCrop().placeholder(placeholder)).into(this)
}

fun ImageView.loadURI(uri: Uri, placeholder: Int) {
    Glide.with(context).load(uri)
        .apply(RequestOptions().transform(FitCenter()).placeholder(placeholder)).into(this)
}

fun ImageView.loadUrlRoundedCorner(url: String? = "", placeholder: Int, radius: Int) {

    Glide.with(context).load(url)
        .fitCenter()
        .apply(RequestOptions().transform(FitCenter(), RoundedCorners(radius)).placeholder(placeholder))
        .into(this)
}

fun ImageView.loadUrlRoundedCorner2(url: String? = "", placeholder: Int, radius: Int) {

    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(250,150).transform(FitCenter(), RoundedCorners(radius)).placeholder(placeholder)

    Glide.with(context).load(url)
        .thumbnail(Glide.with(getContext()).load(placeholder))
        .apply(requestOptions)
        .into(this)
}


fun ImageView.loadDrawableRoundedCorner(placeholder: Int, radius: Int) {
    Glide.with(context).load(placeholder)
        .apply(RequestOptions().transform(FitCenter(), RoundedCorners(radius)).placeholder(placeholder)).into(this)
}

fun ImageView.loadDrawableRoundedCorner(resourceID: Int, placeholder: Int, radius: Int) {
    Glide.with(context).load(resourceID)
        .apply(RequestOptions().transform(FitCenter(), RoundedCorners(radius)).placeholder(placeholder)).into(this)
}

fun ImageView.loadURIRoundedCorner(uri: Uri, placeholder: Int, radius: Int) {
    Glide.with(context).load(uri).apply(RequestOptions().transform(FitCenter(), RoundedCorners(radius)).placeholder(placeholder)).into(this)
}

fun ImageView.loadUrlBlurred(url: String? = "", placeholder: Int) {
    Glide.with(context).load(url)
        .fitCenter()
        .apply(RequestOptions().transform(BlurTransformation(BLUR_RADIUS)))
        .into(this)
}

/*ImageView - Blurred Image
* 1st Paramter - placeholder*/
fun ImageView.loadDrawableBlurred(placeholder: Int) {
    Glide.with(context).load(placeholder)
        .fitCenter()
        .apply(RequestOptions().transform(BlurTransformation(BLUR_RADIUS)))
        .into(this)
}

/*ImageView - Blurred Image
* 1st Paramter - resourceId
* 2nd Paramter - placeholder*/
fun ImageView.loadDrawableBlurred(resourceID: Int, placeholder: Int) {
    Glide.with(context).load(resourceID)
        .fitCenter()
        .placeholder(placeholder)
        .apply(RequestOptions().transform(BlurTransformation(BLUR_RADIUS)))
        .into(this)
}

fun Context.toast(message: CharSequence, duration: Int) =
    Toast.makeText(this, message, duration).show()

fun String.toast(context: Context) =
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()

fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun hideKeyboard(context: Context, view: View?) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = connectivityManager.activeNetworkInfo
    return netInfo != null && netInfo.isConnected
}

// TAG - classname prints in main method only but can't prints in retrofit inner methods or others
val Any.TAG: String
    get() {
//        val tag = javaClass.simpleName
//        return if (tag.length <= 23) tag else tag.substring(0, 23)
        return if (!javaClass.isAnonymousClass) {
            val name = javaClass.simpleName
            if (name.length <= 23) name else name.substring(0, 23)// first 23 chars
        } else {
            val name = javaClass.name
            if (name.length <= 23) name else name.substring(name.length - 23, name.length)// last 23 chars
        }
    }

// TAG() - classname prints in main method only but can't prints in retrofit inner methods or others
/*
inline fun <reified T> T.TAG(): String = T::class.java.simpleName*/


inline fun FragmentManager.addFragmentTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, tag: String) {
    supportFragmentManager.addFragmentTransaction { add(frameId, fragment, tag) }
}

inline fun FragmentManager.replaceFragmentTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, tag: String, addToBackStack: Boolean = false) {
    supportFragmentManager.replaceFragmentTransaction {
        replace(frameId, fragment, tag)
        if (addToBackStack) addToBackStack(fragment.javaClass.name)
    }
}

/*fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, tag: String, addToBackStack: Boolean = false) {
    supportFragmentManager.replaceFragmentTransaction {
        replace(frameId, fragment, tag)
        if (addToBackStack) addToBackStack(fragment.javaClass.name)
    }
}*/

/*
* Convert in String for EditText value
*/
fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

/**
 * Wrapping try/catch to ignore catch block
 */
inline fun <T> justTry(block: () -> T) = try {
    block()
} catch (e: Throwable) {
    LogUtil.e("EXte", "=============${e.localizedMessage}")
    e.localizedMessage
}

/*
* genericType - Return type object
* */
inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Set an onclick listener
 */
fun View.click(block: () -> Unit) = setOnClickListener { block.invoke() }


var progressDialog: Dialog? = null
fun Context.launchProgress() {
    progressDialog = Dialog(this)
    progressDialog?.setContentView(R.layout.progress_dialog)
    progressDialog?.setCancelable(false)
    progressDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    progressDialog?.setCanceledOnTouchOutside(false)
    progressDialog?.show()
}

fun Fragment.launchProgress() {
    this.activity?.launchProgress()
}

fun disposeProgress() {
    if (progressDialog != null)
        progressDialog?.dismiss()
}
fun preventTwoClick(view: View) {
    view.isEnabled = false
    view.postDelayed({ view.isEnabled = true }, 1500)
}

fun isConnectivityAvailable(context: Context): Boolean {
    var activeNetwork: NetworkInfo? = null
    try {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        activeNetwork = connectivityManager.activeNetworkInfo
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return !(activeNetwork == null || !activeNetwork.isConnectedOrConnecting)
}

fun isValidPanCardNo(panCardNo: String?): Boolean {

    // Regex to check valid PAN Card number.
    val regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}"

    // Compile the ReGex
    val p: Pattern = Pattern.compile(regex)

    // If the PAN Card number
    // is empty return false
    if (panCardNo == null) {
        return false
    }

    // Pattern class contains matcher() method
    // to find matching between given
    // PAN Card number using regular expression.
    val m: Matcher = p.matcher(panCardNo)

    // Return if the PAN Card number
    // matched the ReGex
    return m.matches()
}

fun isValidGSTNo(str: String?): Boolean {
    // Regex to check valid
    // GST (Goods and Services Tax) number
    val regex = ("^[0-9]{2}[A-Z]{5}[0-9]{4}"
            + "[A-Z]{1}[1-9A-Z]{1}"
            + "Z[0-9A-Z]{1}$")

    // Compile the ReGex
    val p = Pattern.compile(regex)

    // If the string is empty
    // return false
    if (str == null) {
        return false
    }

    // Pattern class contains matcher()
    // method to find the matching
    // between the given string
    // and the regular expression.
    val m = p.matcher(str)

    // Return if the string
    // matched the ReGex
    return m.matches()
}

fun isValidPassportNo(str: String?): Boolean {
    // Regex to check valid.
    // passport number of India
    val regex = ("^[A-PR-WYa-pr-wy][1-9]\\d"
            + "\\s?\\d{4}[1-9]$")

    // Compile the ReGex
    val p = Pattern.compile(regex)

    // If the string is empty
    // return false
    if (str == null) {
        return false
    }

    // Find match between given string
    // and regular expression
    // using Pattern.matcher()
    val m = p.matcher(str)

    // Return if the string
    // matched the ReGex
    return m.matches()
}

fun isValidAadhaarNumber(str: String?) : Boolean {
    // Regex to check valid Aadhaar number.
    val regex = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$"

    // Compile the ReGex
    val p = Pattern.compile(regex)

    // If the string is empty
    // return false
    if (str == null) {
        return false
    }

    // Pattern class contains matcher() method
    // to find matching between given string
    // and regular expression.
    val m = p.matcher(str)

    // Return if the string
    // matched the ReGex
    return m.matches()
}
fun stringToHtml(string: String?): Spanned? {
    return Html.fromHtml(string)
}
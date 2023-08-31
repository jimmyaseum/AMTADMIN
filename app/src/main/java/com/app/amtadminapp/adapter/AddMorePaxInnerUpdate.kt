package com.app.amtadminapp.adapter

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.TourBookingFormActivity
import com.app.amtadminapp.adapter.dialog.DialogInitialAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.CommonResponse
import com.app.amtadminapp.model.response.paxDataModel
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.adapter_pax_info_edit.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AddMorePaxInnerUpdate(
    val context: Context?,
    private val arrayList: ArrayList<paxDataModel>,
    var paxid: Int
) : RecyclerView.Adapter<AddMorePaxInnerUpdate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_pax_info_edit, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position, arrayList,paxid)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItems(
            context: Context,
            position: Int,
            arrayList: ArrayList<paxDataModel>,
            paxid: Int
        ) {

            if(arrayList[position].FirstName != null && arrayList[position].FirstName != "") {
                if(arrayList[position].LastName != null && arrayList[position].LastName != "") {
                    itemView.txtMemberNo.text = arrayList[position].FirstName + " " + arrayList[position].LastName
                } else {
                    itemView.txtMemberNo.text = arrayList[position].FirstName
                }
            } else {
                itemView.txtMemberNo.text = "Member " + (position + 1)
            }

            itemView.CardEdit.setOnClickListener {
                selectDialog(context,arrayList[adapterPosition],paxid)
            }
        }

        private fun selectDialog(
            mcontext: Context,
            arrayListPAx: paxDataModel,
            paxid: Int
        ) {

            val arrInitialList: ArrayList<String> = ArrayList()
            var gender = ""

            val sharedPreference = SharedPreference(mcontext)

            val dialogMember = Dialog(mcontext)
            dialogMember.requestWindowFeature(Window.FEATURE_NO_TITLE)

            val dialogView = LayoutInflater.from(mcontext).inflate(R.layout.dialog_pax_edit, null)
            dialogMember.setContentView(dialogView)

            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialogMember.window!!.attributes)

            dialogMember.window!!.attributes = lp
            dialogMember.setCancelable(true)
            dialogMember.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialogMember.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            dialogMember.window!!.setGravity(Gravity.CENTER)

            val etInitial = dialogMember.findViewById(R.id.etInitial) as EditText
            val etFirstName = dialogMember.findViewById(R.id.etFirstName) as EditText
            val etLastName = dialogMember.findViewById(R.id.etLastName) as EditText
            val etDOB = dialogMember.findViewById(R.id.etDOB) as EditText
            val etAge = dialogMember.findViewById(R.id.etAge) as EditText
            val etMobile = dialogMember.findViewById(R.id.etMobile) as EditText
            val etAmount = dialogMember.findViewById(R.id.etAmount) as EditText
            val etExtraCost = dialogMember.findViewById(R.id.etExtraCost) as EditText
            val etDiscAmount = dialogMember.findViewById(R.id.etDiscAmount) as EditText
            val etRemarks = dialogMember.findViewById(R.id.etRemarks) as EditText
            val etPassportNo = dialogMember.findViewById(R.id.etPassportNo) as EditText
            val cbFemale = dialogMember.findViewById(R.id.cbFemale) as CheckBox
            val cbMale = dialogMember.findViewById(R.id.cbMale) as CheckBox
            val cbIsVisa = dialogMember.findViewById(R.id.cbIsVisa) as CheckBox
            val cbIsInsurance = dialogMember.findViewById(R.id.cbIsInsurance) as CheckBox

            if(AppConstant.Travel_Type.equals("TOUR")) {
                etAmount.isEnabled = false
                etExtraCost.isEnabled = false
                etDiscAmount.isEnabled = false
            } else {
                etAmount.isEnabled = true
                etExtraCost.isEnabled = true
                etDiscAmount.isEnabled = true
            }

            val ImgClose = dialogMember.findViewById(R.id.ImgClose) as ImageView
            val CardSave = dialogMember.findViewById(R.id.CardSave) as CardView

            if(arrayListPAx.Initial != null && arrayListPAx.Initial != "") {
                etInitial.setText(arrayListPAx.Initial)
            } else {
                etInitial.setText("")
            }
            if(arrayListPAx.FirstName != null && arrayListPAx.FirstName != "") {
                etFirstName.setText(arrayListPAx.FirstName)
            } else {
                etFirstName.setText("")
            }
            if(arrayListPAx.LastName != null && arrayListPAx.LastName != "") {
                etLastName.setText(arrayListPAx.LastName)
            } else {
                etLastName.setText("")
            }
            if(arrayListPAx.DOB != null && arrayListPAx.DOB != "") {
                val mdob = convertDateStringToString(
                    arrayListPAx.DOB,
                    AppConstant.ddMMyyyy_HHmmss,
                    AppConstant.dd_MM_yyyy_Slash
                )!!
                etDOB.setText(mdob)
            } else {
                etDOB.setText("")
            }
            if(arrayListPAx.Age != null && arrayListPAx.Age != "") {
                etAge.setText(arrayListPAx.Age)
            } else {
                etAge.setText("")
            }
            if(arrayListPAx.MobileNo != null && arrayListPAx.MobileNo != "") {
                etMobile.setText(arrayListPAx.MobileNo)
            } else {
                etMobile.setText("")
            }
            if(arrayListPAx.PassportNo != null && arrayListPAx.PassportNo != "") {
                etPassportNo.setText(arrayListPAx.PassportNo)
            } else {
                etPassportNo.setText("")
            }
            if(arrayListPAx.Gender != null && arrayListPAx.Gender != "") {
                if(arrayListPAx.Gender.equals("MALE")) {
                    cbMale.isChecked = true
                    cbFemale.isChecked = false
                    gender = "MALE"
                } else {
                    cbFemale.isChecked = true
                    cbMale.isChecked = false
                    gender = "FEMALE"
                }
            } else {
                cbFemale.isChecked = false
                cbMale.isChecked = false
                gender = ""
            }

            if(arrayListPAx.Amount != null) {
                etAmount.setText(arrayListPAx.Amount.toString())
            } else {
                etAmount.setText("")
            }

            if(arrayListPAx.ExtraCost != null) {
                etExtraCost.setText(arrayListPAx.ExtraCost.toString())
            } else {
                etExtraCost.setText("")
            }

            if(arrayListPAx.DiscountAmount != null) {
                etDiscAmount.setText(arrayListPAx.DiscountAmount.toString())
            } else {
                etDiscAmount.setText("")
            }

            if(arrayListPAx.Remarks != null && arrayListPAx.Remarks != "") {
                etRemarks.setText(arrayListPAx.Remarks)
            } else {
                etRemarks.setText("")
            }

            if(arrayListPAx.IsVisa != null) {
                cbIsVisa.isChecked = arrayListPAx.IsVisa
            }
            if(arrayListPAx.IsInsurance != null) {
                cbIsInsurance.isChecked = arrayListPAx.IsInsurance
            }

            // AI005 Mr, Mrs, Ms, Dr
            arrInitialList.add("Mr.")
            arrInitialList.add("Mrs.")
            arrInitialList.add("Ms.")
            arrInitialList.add("Dr.")

            ImgClose.setOnClickListener {
                dialogMember.dismiss()
            }

            cbMale.setOnClickListener {
                cbMale.isChecked = true
                cbFemale.isChecked = false
                gender = "MALE"

            }
            cbFemale.setOnClickListener {
                cbMale.isChecked = false
                cbFemale.isChecked = true
                gender = "FEMALE"
            }
            etDOB.setOnClickListener {
                var calendarNow: Calendar?
                var year = 0
                var month = 0
                var day = 0
                val strDate = ""

                if (year == 0 || month == 0 || day == 0 || strDate.isNullOrEmpty()) {
                    calendarNow = Calendar.getInstance()
                    year = calendarNow!!.get(Calendar.YEAR)
                    month = calendarNow.get(Calendar.MONTH)
                    day = calendarNow.get(Calendar.DAY_OF_MONTH)
                } else {
                    val parts = strDate.split("/")
                    day = parts[0].toInt()
                    month = parts[1].toInt() - 1
                    year = parts[2].toInt()
                }

                val dpd = DatePickerDialog(mcontext, DatePickerDialog.OnDateSetListener { view, year1, monthOfYear1, dayOfMonth1 ->

                    val selectDate = convertDateStringToString(
                        "$dayOfMonth1/${monthOfYear1 + 1}/$year1",
                        AppConstant.dd_MM_yyyy_Slash
                    )!!

                    year = year1
                    month = monthOfYear1
                    day = dayOfMonth1

                    val calendar1: Calendar = Calendar.getInstance()
                    val today = convertDateToString(calendar1.time, AppConstant.dd_MM_yyyy_Slash)

                    var days = getFormattedYear(selectDate, today, AppConstant.dd_MM_yyyy_Slash)

                    etDOB.setText(selectDate)
                    etAge.setText(days)
                    etAge.isEnabled = false

                }, year, month, day)
                dpd.show()
            }
            etInitial.setOnClickListener {
                var dialogSelectInitial = Dialog(mcontext)
                dialogSelectInitial.requestWindowFeature(Window.FEATURE_NO_TITLE)

                val dialogView = LayoutInflater.from(mcontext).inflate(R.layout.dialog_select, null)
                dialogSelectInitial.setContentView(dialogView)

                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialogSelectInitial.window!!.attributes)

                dialogSelectInitial.window!!.attributes = lp
                dialogSelectInitial.setCancelable(true)
                dialogSelectInitial.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                dialogSelectInitial.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                dialogSelectInitial.window!!.setGravity(Gravity.CENTER)

                val rvDialogCustomer = dialogSelectInitial.findViewById(R.id.rvDialogCustomer) as RecyclerView
                val edtSearchCustomer = dialogSelectInitial.findViewById(R.id.edtSearchCustomer) as EditText

                val itemAdapter = DialogInitialAdapter(mcontext, arrInitialList!!)
                itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                    override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                        etInitial.setText(arrInitialList!![pos])
                        dialogSelectInitial!!.dismiss()
                    }
                })

                rvDialogCustomer.adapter = itemAdapter

                edtSearchCustomer.gone()
                dialogSelectInitial!!.show()
            }

            CardSave.setOnClickListener {
                mcontext.launchProgress()
                val roomNo = arrayListPAx.RoomNo
                val ID = arrayListPAx.ID
                val jsonObject = JSONObject()
                val mdob = convertDateStringToString(
                    etDOB.text.toString().trim(),
                    AppConstant.dd_MM_yyyy_Slash,
                    AppConstant.yyyy_MM_dd_Dash
                )!!
                jsonObject.put("ID",ID)
                jsonObject.put("PaxTypeID",paxid)
                jsonObject.put("RoomNo",roomNo)
                jsonObject.put("TourBookingID", AppConstant.TOURBookingID)
                jsonObject.put("Initial", etInitial.text.toString().trim())
                jsonObject.put("FirstName", etFirstName.text.toString().trim())
                jsonObject.put("LastName", etLastName.text.toString().trim())
                jsonObject.put("MobileNo", etMobile.text.toString().trim())
                jsonObject.put("Gender", gender)
                jsonObject.put("DOB",mdob)
                jsonObject.put("Age",etAge.text.toString().trim())
                jsonObject.put("PassportNo", etPassportNo.text.toString().trim())
                jsonObject.put("Amount", etAmount.text.toString().trim())
                jsonObject.put("ExtraCost", etExtraCost.text.toString().trim())
                jsonObject.put("TaxableAmount", 0)
                jsonObject.put("GSTAmount", 0)
                jsonObject.put("CancellationCost", 0)
                jsonObject.put("FinalCost", 0)
                jsonObject.put("Remarks", etRemarks.text.toString().trim())
                jsonObject.put("DiscountAmount", etDiscAmount.text.toString().trim())
                jsonObject.put("IsInsurance", cbIsVisa.isChecked)
                jsonObject.put("IsVisa", cbIsInsurance.isChecked)

                val call = ApiUtils.apiInterface2.UpdateTourPax(getRequestJSONBody(jsonObject.toString()))

                call.enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {

                        if (response.code() == 200) {
                            LogUtil.e(TAG, "=====onResponse====")

                            if (response.body()?.code == 200) {
                                dialogMember.dismiss()

                                var fragment: Fragment = Fragment()
                                fragment = getVisibleFragment((mcontext) as TourBookingFormActivity)!!

                                fragment.fragmentManager!!.beginTransaction().detach(fragment).attach(fragment).commit()

                                disposeProgress()
                            } else {
                                disposeProgress()
                                mcontext!!.toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                            }
                        } else {
                            disposeProgress()
                            mcontext!!.toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                        }
                    }
                    override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                        disposeProgress()
                        mcontext.toast(mcontext.resources.getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
                    }
                })
            }
            dialogMember!!.show()
        }

        fun getVisibleFragment(tourBookingFormActivity: TourBookingFormActivity): Fragment? {
            val fragmentManager: FragmentManager = tourBookingFormActivity.getSupportFragmentManager()
            val fragments: List<Fragment> = fragmentManager.getFragments()
            if (fragments != null) {
                for (fragment in fragments) {
                    if (fragment != null && fragment.isVisible()) return fragment
                }
            }
            return null
        }
    }
}

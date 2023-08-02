package com.app.amtadminapp.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.PaxTourCostModel
import com.app.amtadminapp.model.response.TPIM
import com.app.amtadminapp.model.response.paxDataModel
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.adapter_add_more_pax_update.view.*
import kotlinx.android.synthetic.main.adapter_add_more_pax_update.view.rvPaxInfoInner
import kotlin.collections.ArrayList

class AddMorePaxUpdate(
    val arrayList: ArrayList<TPIM>?,
    val recyclerItemClickListener: RecyclerClickListener
) : RecyclerView.Adapter<AddMorePaxUpdate.ViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_add_more_pax_update, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position, arrayList!!, recyclerItemClickListener)

        arrayList[position].roomNo = (position+1).toString()

        arrayList[position].tilPaxType = holder.itemView.tilPaxType
        arrayList[position].txtRoomNo = holder.itemView.txtRoomNo
        arrayList[position].rvPaxInfoInner = holder.itemView.rvPaxInfoInner

        arrayList[position].txtRoomNo?.text = "Room No " + arrayList[position].roomNo

        if(arrayList[position].paxData.size > 0) {
            arrayList[position].rvPaxInfoInner!!.visible()
        } else {
            arrayList[position].rvPaxInfoInner!!.gone()
        }

        if (position == 0 && position == arrayList.size - 1) {
            holder.itemView.imgDelete.visibility = View.GONE
            holder.itemView.tvAddMore.visibility = View.VISIBLE
        } else if (position == arrayList.size - 1) {
            holder.itemView.imgDelete.visibility = View.VISIBLE
            holder.itemView.tvAddMore.visibility = View.VISIBLE
        } else {
            holder.itemView.imgDelete.visibility = View.VISIBLE
            holder.itemView.tvAddMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context,
                      position: Int,
                      arrayList: ArrayList<TPIM>?,
                      recyclerItemClickListener: RecyclerClickListener) {

            itemView.tilPaxType.editText?.text = arrayList!![position].paxType.toEditable()
            itemView.txtRoomNo.text = "Room No " + arrayList!![position].roomNo
            val arrList = arrayList!![position].paxData

            if(arrList!!.size > 0) {
                val adapterPAXInner = AddMorePaxInnerUpdate(context, arrList, arrayList!![position].paxTypeID)
                itemView.rvPaxInfoInner.adapter = adapterPAXInner
            }

            itemView.etPaxType.setOnClickListener {
                if(arrayList[adapterPosition].paxTypeID == 0) {
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, arrayList[adapterPosition].paxTypeID)
                }
            /*else{
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, arrayList[adapterPosition].paxTypeID)
                }*/
            }

            itemView.tvAddMore.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1)
            }

            itemView.imgDelete.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1)
            }

        }

        private fun EditText.setSimpleListener(listener: (p0: CharSequence?) -> Unit) {
            this.addTextChangedListener(TextWatcherFactory().create(listener))
        }

        class TextWatcherFactory {
            fun create(onTextChanged: (p0: CharSequence?) -> Unit): android.text.TextWatcher {
                return object : android.text.TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = onTextChanged(p0)
                }
            }
        }
    }

    fun addItem(model: TPIM, position: Int): Boolean {

        if (!arrayList.isNullOrEmpty()) {

            val flag = isValidateItem(position)
            if (flag) {
                arrayList.add(model)
                notifyDataSetChanged()
                return true
            } else {
                OpenDialog()
                return false
            }
        }
        return false
    }

    private fun OpenDialog() {
        val dialogMember = Dialog(context!!)
        dialogMember.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_otp_verify, null)
        dialogMember.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogMember.window!!.attributes)

        dialogMember.window!!.attributes = lp
        dialogMember.setCancelable(true)
        dialogMember.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogMember.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogMember.window!!.setGravity(Gravity.CENTER)

        val edtOTP = dialogMember.findViewById(R.id.edtOTP) as EditText
        val txtid = dialogMember.findViewById(R.id.txtid) as TextView
        val txtresend = dialogMember.findViewById(R.id.txtresend) as TextView
        val ImgClose = dialogMember.findViewById(R.id.ImgClose) as ImageView
        val CardInquiryNow = dialogMember.findViewById(R.id.CardSubmit) as CardView
        val txt = dialogMember.findViewById(R.id.txt) as TextView
        val txtmsg = dialogMember.findViewById(R.id.txtmsg) as TextView

        txtid.text = "Warning!!"
        txtmsg.text = "Please fill the details of all member to add new room."
        txt.text = "Okay"
        edtOTP.gone()
        txtresend.gone()
        ImgClose.gone()

        CardInquiryNow.setOnClickListener {
            dialogMember.dismiss()
        }

        dialogMember!!.show()
    }

    fun getAdapterArrayList(): ArrayList<TPIM>? {
        return arrayList
    }

    /*Update year*/
    fun updateItem(position: Int, name: String, paxid: Int, paxcount: Int, arral : ArrayList<PaxTourCostModel>) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].paxType = name
            arrayList[position].paxTypeID = paxid
            arrayList[position].tilPaxType?.editText!!.text = name.toEditable()

            var arrList : ArrayList<paxDataModel>? = ArrayList()
            arrayList[position].paxData = ArrayList()
            for(i in 0 until arral.size) {

                arrList?.add(paxDataModel(0,AppConstant.TOURBookingID,
                    arrayList[position].roomNo,"",
                    "","","","","","","",
                    arral[i].Discount,
                    arral[i].Rate!!.toDouble(),
                    arral[i].ExtraCost,
                    "",false,false))
            }

            arrayList[position].paxData = arrList!!

            notifyDataSetChanged()
        }
    }

    fun remove(position: Int) {
        if (!arrayList.isNullOrEmpty()) {
            hideErrorTIL(position)
            arrayList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    private fun hideErrorTIL(position: Int) {
        if (!arrayList.isNullOrEmpty()) {

            for (i in arrayList.indices) {
                arrayList[i].tilPaxType?.isErrorEnabled = false

            }
        }
    }

    fun isValidateItem(position: Int): Boolean {
        var emptyBox = true

        val arrayList1 = arrayList!![position].paxData
        if (!arrayList.isNullOrEmpty()) {

            if (TextUtils.isEmpty(arrayList!![position].tilPaxType?.editText!!.text.trim())) {
                arrayList!![position].tilPaxType?.error = context?.getString(R.string.error_empty_paxtype)
                emptyBox = false
            }
            else {
                arrayList!![position].tilPaxType?.isErrorEnabled = false
                for(j in 0 until arrayList1.size) {
                    if(arrayList1[j].FirstName.equals("") || arrayList1[j].FirstName == null && arrayList1[j].LastName.equals("") || arrayList1[j].LastName == null) {
                        emptyBox = false
                    }
                }
            }


        }
        return emptyBox
    }


}
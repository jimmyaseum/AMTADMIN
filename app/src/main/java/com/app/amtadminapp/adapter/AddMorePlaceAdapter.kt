package com.app.amtadminapp.adapter

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.interFase.RecyclerMultipleItemClickListener
import com.app.amtadminapp.model.response.AllCityDataModel
import com.app.amtadminapp.utils.toEditable
import kotlinx.android.synthetic.main.adapter_add_more_place.view.*
import kotlinx.android.synthetic.main.adapter_add_more_place.view.tilNights
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddMorePlaceAdapter(
    val arrayList: ArrayList<AllCityDataModel>?,
    val recyclerItemClickListener: RecyclerMultipleItemClickListener
) : RecyclerView.Adapter<AddMorePlaceAdapter.ViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_add_more_place, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems( position, arrayList!!, recyclerItemClickListener)

        arrayList[position].tilPlace = holder.itemView.tilPlace
        arrayList[position].tilHotel = holder.itemView.tilHotel
        arrayList[position].tilPlan = holder.itemView.tilPlan
        arrayList[position].tilCheckInDate = holder.itemView.tilCheckInDate
        arrayList[position].tilNights = holder.itemView.tilNights
        arrayList[position].tilCheckOutDate = holder.itemView.tilCheckOutDate

        if (position == 0 && position == arrayList.size - 1) {
            holder.itemView.imgDelete.visibility = View.VISIBLE // GONE
            holder.itemView.LLcardButtonAddMore.visibility = View.VISIBLE
        }
        else if (position == arrayList.size - 1) {
            holder.itemView.imgDelete.visibility = View.VISIBLE
            holder.itemView.LLcardButtonAddMore.visibility = View.VISIBLE
        }
        else {
            holder.itemView.imgDelete.visibility = View.VISIBLE
            holder.itemView.LLcardButtonAddMore.visibility = View.GONE // visible for every card
        }
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(position: Int,
                      arrayList: ArrayList<AllCityDataModel>?,
                      recyclerItemClickListener: RecyclerMultipleItemClickListener) {

            itemView.tilPlace.editText?.text = arrayList!![position].place.toEditable()
            itemView.tilHotel.editText?.text = arrayList!![position].hotel.toEditable()
            itemView.tilPlan.editText?.text = arrayList!![position].plan.toEditable()
            itemView.tilNights.editText?.text = arrayList!![position].nights.toEditable()

            if(position == 0) {
                //Calculate date from no of night
                val c = Calendar.getInstance()
                c.time = SimpleDateFormat("dd/MM/yyyy").parse(arrayList!![position].checkindate)
                c.add(Calendar.DATE, arrayList!![position].nights.toInt())
                val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                val checkoutdate = sdf1.format(c.time)

                arrayList[adapterPosition].checkindate = arrayList!![position].checkindate
                itemView.tilCheckInDate.editText?.text = arrayList!![position].checkindate.toEditable()

                arrayList[adapterPosition].checkoutdate = checkoutdate
                itemView.tilCheckOutDate.editText?.text = checkoutdate.toEditable()

            }
            else {
                //Calculate date from no of night
                val c = Calendar.getInstance()
                c.time = SimpleDateFormat("dd/MM/yyyy").parse(arrayList!![position-1].checkoutdate)
                c.add(Calendar.DATE, arrayList!![position].nights.toInt())
                val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                val checkoutdate = sdf1.format(c.time)

                arrayList[adapterPosition].checkindate = arrayList!![position-1].checkoutdate
                itemView.tilCheckInDate.editText?.text = arrayList!![position-1].checkoutdate.toEditable()

                arrayList[adapterPosition].checkoutdate = checkoutdate
                itemView.tilCheckOutDate.editText?.text = checkoutdate.toEditable()

            }

            itemView.etPlace.setOnClickListener {
                if(!arrayList[adapterPosition].place!!.isEmpty()) {
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1, arrayList[adapterPosition].place)
                } else{
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1, arrayList[adapterPosition].place)
                }
            }

            itemView.etHotel.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, arrayList[adapterPosition].placeid, arrayList[adapterPosition].hotel)
            }

            itemView.etPlan.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition,1, arrayList[adapterPosition].plan)
            }

            itemView.etCheckInDate.setOnClickListener {
                var night = 0
                if(!arrayList[adapterPosition].nights.equals("")) {
                    night = arrayList[adapterPosition].nights.toInt()
                }
                if(!arrayList[adapterPosition].checkindate!!.isEmpty()) {
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, night, arrayList[adapterPosition].checkindate)
                } else{
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, night, arrayList[adapterPosition].checkindate)
                }
            }

            itemView.LLcardButtonAddMore.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1, arrayList[adapterPosition].checkoutdate)
            }

            itemView.imgDelete.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 11, "")
            }

            /*itemView.etNights.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (char.toString().trim().isNotEmpty()) {
                        val strSearch = char.toString()
                        var night = "0"
                        if(!strSearch.equals("")) {
                            night = strSearch
                        }
                        recyclerItemClickListener.onItemClickEvent(itemView.etNights, adapterPosition, 1, "")
//                        recyclerItemClickListener.onItemClickEvent(itemView.etNights, adapterPosition, night.toInt(), "")

                    }
                }
            })
*/

            itemView.etNights.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    recyclerItemClickListener.onItemClickEvent(itemView.etNights, adapterPosition, 1, itemView.etNights.text.toString())
                    true
                } else {
                    false
                }
            }

//            itemView.etNights.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
//                if (!hasFocus) {
//                    recyclerItemClickListener.onItemClickEvent(itemView.etNights, adapterPosition, 1, itemView.etNights.text.toString())
//                }
//            })

            itemView.tilNights?.editText!!.setSimpleListener {
                arrayList[adapterPosition].nights = it.toString()
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

    fun addItem(position: Int, model: AllCityDataModel): Boolean {

        if (!arrayList.isNullOrEmpty()) {

            val flag =  isValidateItem()

            if(flag) {
//                arrayList.add(position,model)
                arrayList.add(model)
                notifyDataSetChanged()
                return true
            }
        }

        return false
    }

    fun getAdapterArrayList(): ArrayList<AllCityDataModel>? {
        return arrayList
    }

    fun updateItemPlace(position: Int, name: String, id: Int) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].placeid = id
            arrayList[position].place = name
            arrayList[position].tilPlace?.editText!!.text = name.toEditable()
        }
    }

    fun updateItemHotel(position: Int, name: String, id: Int) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].hotelid = id
            arrayList[position].hotel = name
            arrayList[position].tilHotel?.editText!!.text = name.toEditable()
        }
    }

    fun updateItemPlan(position: Int, name: String, id: Int) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].planid = id
            arrayList[position].plan = name
            arrayList[position].tilPlan?.editText!!.text = name.toEditable()
        }
    }

    fun updateItemDate(position: Int, checkin: String, checkout: String) {
        if (!arrayList.isNullOrEmpty()) {

            arrayList[position].checkindate = checkin
            arrayList[position].tilCheckInDate?.editText!!.text = checkin.toEditable()

            arrayList[position].checkoutdate = checkout
            arrayList[position].tilCheckOutDate?.editText!!.text = checkout.toEditable()

         /*   for(i in position+1 until arrayList.size) {

                val c = Calendar.getInstance()
                c.time = SimpleDateFormat("dd/MM/yyyy").parse(arrayList!![i-1].checkoutdate)
                c.add(Calendar.DATE, arrayList!![i].nights.toInt())
                val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                val mcheckoutdate = sdf1.format(c.time)
                arrayList[i].checkindate = arrayList!![i-1].checkoutdate
                arrayList[i].tilCheckInDate?.editText!!.text = arrayList!![i-1].checkoutdate.toEditable()

                arrayList[i].checkoutdate = mcheckoutdate
                arrayList[i].tilCheckOutDate?.editText!!.text = mcheckoutdate.toEditable()
            }*/
        }
    }

    fun updateItemDate1(position: Int) {
        if (!arrayList.isNullOrEmpty()) {

            for(i in 0 until arrayList.size) {
            }
            for(i in position+1 until arrayList.size) {

                   val c = Calendar.getInstance()
                   c.time = SimpleDateFormat("dd/MM/yyyy").parse(arrayList!![i-1].checkoutdate)
                   c.add(Calendar.DATE, arrayList!![i].nights.toInt())
                   val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                   val mcheckoutdate = sdf1.format(c.time)
                   arrayList[i].checkindate = arrayList!![i-1].checkoutdate
                   arrayList[i].tilCheckInDate?.editText!!.text = arrayList!![i-1].checkoutdate.toEditable()

                   arrayList[i].checkoutdate = mcheckoutdate
                   arrayList[i].tilCheckOutDate?.editText!!.text = mcheckoutdate.toEditable()
               }
        }
    }

    fun updateItemNight() {
        if (!arrayList.isNullOrEmpty()) {
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
                arrayList[i].tilPlace?.isErrorEnabled = false
                arrayList[i].tilNights?.isErrorEnabled = false
                arrayList[i].tilCheckInDate?.isErrorEnabled = false
                arrayList[i].tilCheckOutDate?.isErrorEnabled = false

            }
        }
    }

    fun isValidateItem(): Boolean {
        var emptyBox = true

        if (!arrayList.isNullOrEmpty()) {

            for (model in arrayList) {

                if (TextUtils.isEmpty(model.tilPlace?.editText!!.text.trim())) {
                    model.tilPlace?.error = context?.getString(R.string.error_empty_place)
                    emptyBox = false
                } else {
                    model.tilPlace?.isErrorEnabled = false
                }

                if (TextUtils.isEmpty(model.tilNights?.editText!!.text.trim())) {
                    model.tilNights?.error = context?.getString(R.string.error_empty_night)
                    emptyBox = false
                } else {
                    model.tilNights?.isErrorEnabled = false
                }

                /*
                if (TextUtils.isEmpty(model.tilCheckInDate?.editText!!.text.trim())) {
                    model.tilCheckInDate?.error = context?.getString(R.string.error_empty_checkin)
                    emptyBox = false
                } else {
                    model.tilCheckInDate?.isErrorEnabled = false
                }

                if (TextUtils.isEmpty(model.tilCheckOutDate?.editText!!.text.trim())) {
                    model.tilCheckOutDate?.error = context?.getString(R.string.error_empty_checkout)
                    emptyBox = false
                } else {
                    model.tilCheckOutDate?.isErrorEnabled = false
                }

                if (TextUtils.isEmpty(model.tilRemarks?.editText!!.text.trim())) {
                    model.tilRemarks?.error = context?.getString(R.string.error_empty_remarks)
                    emptyBox = false
                } else {
                    model.tilRemarks?.isErrorEnabled = false
                }
                */
            }
        }
        return emptyBox
    }

}
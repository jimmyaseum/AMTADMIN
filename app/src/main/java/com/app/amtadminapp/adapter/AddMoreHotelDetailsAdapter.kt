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
import com.app.amtadminapp.model.response.AddMoreHotelDetailsModel
import com.app.amtadminapp.utils.toEditable
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.*
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.LLcardButtonAddMore
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.etCheckInDate
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.etHotel
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.etNights
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.etPlace
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.etPlan
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.imgDelete
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.tilCheckInDate
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.tilCheckOutDate
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.tilHotel
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.tilNights
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.tilPlace
import kotlinx.android.synthetic.main.adapter_add_more_hotel_details.view.tilPlan
import kotlin.collections.ArrayList

class AddMoreHotelDetailsAdapter(
    val arrayList: ArrayList<AddMoreHotelDetailsModel>?,
    val recyclerItemClickListener: RecyclerMultipleItemClickListener
) : RecyclerView.Adapter<AddMoreHotelDetailsAdapter.ViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_add_more_hotel_details, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems( position, arrayList!!, recyclerItemClickListener)

        arrayList[position].tilPlace = holder.itemView.tilPlace
        arrayList[position].tilHotel = holder.itemView.tilHotel
        arrayList[position].tilCheckInDate = holder.itemView.tilCheckInDate
        arrayList[position].tilNights = holder.itemView.tilNights
        arrayList[position].tilCheckOutDate = holder.itemView.tilCheckOutDate

        arrayList[position].tilRoomCat = holder.itemView.tilRoomCat
        arrayList[position].tilRoom = holder.itemView.tilRoom
        arrayList[position].tilPlan = holder.itemView.tilPlan
        arrayList[position].tilExtrabed = holder.itemView.tilExtrabed
        arrayList[position].tilPickupFrom = holder.itemView.tilPickupFrom
        arrayList[position].tilPickuptime = holder.itemView.tilPickuptime
        arrayList[position].tilBy = holder.itemView.tilBy
        arrayList[position].tilSupplier = holder.itemView.tilSupplier

        if (position == 0 && position == arrayList.size - 1) {
            holder.itemView.imgDelete.visibility = View.GONE
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
                      arrayList: ArrayList<AddMoreHotelDetailsModel>?,
                      recyclerItemClickListener: RecyclerMultipleItemClickListener) {

            itemView.tilPlace.editText?.text = arrayList!![position].place.toEditable()
            itemView.tilHotel.editText?.text = arrayList!![position].hotel.toEditable()
            itemView.tilNights.editText?.text = arrayList!![position].nights.toEditable()
            itemView.tilCheckInDate.editText?.text = arrayList!![position].checkindate.toEditable()
            itemView.tilCheckOutDate.editText?.text = arrayList!![position].checkoutdate.toEditable()
            itemView.tilRoomCat.editText?.text = arrayList!![position].roomcat.toEditable()
            itemView.tilRoom.editText?.text = arrayList!![position].room.toEditable()
            itemView.tilPlan.editText?.text = arrayList!![position].plan.toEditable()
            itemView.tilExtrabed.editText?.text = arrayList!![position].extrabed.toEditable()
            itemView.tilPickupFrom.editText?.text = arrayList!![position].pickupfrom.toEditable()
            itemView.tilPickuptime.editText?.text = arrayList!![position].pickuptime.toEditable()
            itemView.tilBy.editText?.text = arrayList!![position].by.toEditable()
            itemView.tilSupplier.editText?.text = arrayList!![position].supplier.toEditable()

            itemView.tvAddMore.text = "PLACE - " + position + 1

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

            itemView.etRoomCat.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition,1, arrayList[adapterPosition].roomcat)
            }

            itemView.etSupplier.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition,1, arrayList[adapterPosition].supplier)
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

            itemView.etNights.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    recyclerItemClickListener.onItemClickEvent(itemView.etNights, adapterPosition, 1, itemView.etNights.text.toString())
                    true
                } else {
                    false
                }
            }

            itemView.LLcardButtonAddMore.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1, arrayList[adapterPosition].checkoutdate)
            }

            itemView.imgDelete.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 11, "")
            }

            itemView.tilNights?.editText!!.setSimpleListener {
                arrayList[adapterPosition].nights = it.toString()
            }

            itemView.tilRoom?.editText!!.setSimpleListener {
                arrayList[adapterPosition].room = it.toString()
            }

            itemView.tilExtrabed?.editText!!.setSimpleListener {
                arrayList[adapterPosition].extrabed = it.toString()
            }

            itemView.tilPickupFrom?.editText!!.setSimpleListener {
                arrayList[adapterPosition].pickupfrom = it.toString()
            }

            itemView.tilBy?.editText!!.setSimpleListener {
                arrayList[adapterPosition].by = it.toString()
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

    fun addItem(position: Int, model: AddMoreHotelDetailsModel): Boolean {

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

    fun getAdapterArrayList(): ArrayList<AddMoreHotelDetailsModel>? {
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

    fun updateItemRoomCategory(position: Int, name: String, id: Int) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].roomcatid = id
            arrayList[position].roomcat = name
            arrayList[position].tilRoomCat?.editText!!.text = name.toEditable()
        }
    }

    fun updateItemSupplier(position: Int, name: String, id: Int) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].supplierid = id
            arrayList[position].supplier = name
            arrayList[position].tilSupplier?.editText!!.text = name.toEditable()
        }
    }

    fun updateItemDate(position: Int, checkin: String, checkout: String) {
        if (!arrayList.isNullOrEmpty()) {

            arrayList[position].checkindate = checkin
            arrayList[position].tilCheckInDate?.editText!!.text = checkin.toEditable()

            arrayList[position].checkoutdate = checkout
            arrayList[position].tilCheckOutDate?.editText!!.text = checkout.toEditable()

        }
    }

    fun updateItemDate1(position: Int) {
        if (!arrayList.isNullOrEmpty()) {

            for(i in 0 until arrayList.size) {

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
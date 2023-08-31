package com.app.amtadminapp.adapter

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.interFase.RecyclerMultipleItemClickListener
import com.app.amtadminapp.model.response.booking.PaxDataModel
import com.app.amtadminapp.utils.toEditable
import kotlinx.android.synthetic.main.adapter_add_more_pax.view.*
import kotlinx.android.synthetic.main.adapter_add_more_pax.view.imgDelete
import kotlinx.android.synthetic.main.adapter_add_more_pax.view.tvAddMore
import kotlin.collections.ArrayList

class AddMorePaxAdapter(
    val arrayList: ArrayList<PaxDataModel>?,
    val recyclerItemClickListener: RecyclerMultipleItemClickListener,
    val state: String
) : RecyclerView.Adapter<AddMorePaxAdapter.ViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_add_more_pax, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems( position, arrayList!!, recyclerItemClickListener)

        arrayList[position].tilPaxType = holder.itemView.tilPaxType
        arrayList[position].tilNoofRooms = holder.itemView.tilNoofRooms

        if(state.equals("add")) {
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
        } else {
            holder.itemView.imgDelete.visibility = View.GONE
            holder.itemView.tvAddMore.visibility = View.GONE
            arrayList[position].tilPaxType?.editText?.isEnabled = false
            arrayList[position].tilNoofRooms?.editText?.isEnabled = false
        }
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(position: Int,
                      arrayList: ArrayList<PaxDataModel>?,
                      recyclerItemClickListener: RecyclerMultipleItemClickListener) {

            itemView.tilPaxType.editText?.text = arrayList!![position].pax.toEditable()
            itemView.tilNoofRooms.editText?.text = arrayList!![position].noofroom.toEditable()

            itemView.etPaxType.setOnClickListener {
                if(!arrayList[adapterPosition].pax!!.isEmpty()) {
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1, arrayList[adapterPosition].pax)
                } else{
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1, arrayList[adapterPosition].pax)
                }
            }

            itemView.tilNoofRooms?.editText!!.setSimpleListener {
                arrayList[adapterPosition].noofroom = it.toString()
                if(!arrayList[adapterPosition].pax!!.isEmpty()) {
                    recyclerItemClickListener.onItemClickEvent(itemView.etNoofRooms1, adapterPosition, 1, arrayList[adapterPosition].pax)
                } else{
                    recyclerItemClickListener.onItemClickEvent(itemView.etNoofRooms1, adapterPosition, 1, arrayList[adapterPosition].pax)
                }
            }

            itemView.tvAddMore.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1, "")
            }

            itemView.imgDelete.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1, "")
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

    fun addItem(model: PaxDataModel, mode: Int): Boolean {

        if (!arrayList.isNullOrEmpty()) {

            val flag = isValidateItem()

            if (flag && mode == 0) {
                return true
            } else {

                if (flag) {
                    arrayList.add(model)
                    notifyDataSetChanged()
                    return true

                } else {
                    return false
                }
            }
        }

        return false
    }

    fun getAdapterArrayList(): ArrayList<PaxDataModel>? {
        return arrayList
    }

    /*Update year*/
    fun updateItem(position: Int, name: String, paxid: Int) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].pax = name
            arrayList[position].paxid = paxid
            arrayList[position].tilPaxType?.editText!!.text = name.toEditable()

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
                arrayList[i].tilNoofRooms?.isErrorEnabled = false

            }
        }
    }

    fun isValidateItem(): Boolean {
        var emptyBox = true

        if (!arrayList.isNullOrEmpty()) {

            for (model in arrayList) {

                if (TextUtils.isEmpty(model.tilPaxType?.editText!!.text.trim())) {
                    model.tilPaxType?.error = context?.getString(R.string.error_empty_paxtype)
                    emptyBox = false
                } else {
                    model.tilPaxType?.isErrorEnabled = false
                }

                if (TextUtils.isEmpty(model.tilNoofRooms?.editText!!.text.trim())) {
                    model.tilNoofRooms?.error = context?.getString(R.string.error_empty_no_of_room)
                    emptyBox = false
                } else {
                    model.tilNoofRooms?.isErrorEnabled = false
                }
            }
        }
        return emptyBox
    }


}
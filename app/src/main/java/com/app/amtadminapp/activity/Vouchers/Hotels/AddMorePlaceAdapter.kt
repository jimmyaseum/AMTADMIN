package com.app.amtadminapp.activity.Vouchers.Hotels

import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.interFase.RecyclerMultipleItemClickListener
import com.app.amtadminapp.model.PlaceDataModel
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.loadURIRoundedCorner
import com.app.amtadminapp.utils.loadUrlRoundedCorner2
import com.app.amtadminapp.utils.toEditable
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.activity_add_airline_voucher.ll_Pdf
import kotlinx.android.synthetic.main.activity_add_airline_voucher.select_image
import kotlinx.android.synthetic.main.activity_add_airline_voucher.select_pdf
import kotlinx.android.synthetic.main.adapter_add_more_places.view.*
import kotlinx.android.synthetic.main.adapter_add_more_places.view.imgDelete
import kotlinx.android.synthetic.main.adapter_add_more_places.view.tvAddMore
import kotlin.collections.ArrayList

class AddMorePlaceAdapter(
    val arrayList: ArrayList<PlaceDataModel>?,
    val recyclerItemClickListener: RecyclerMultipleItemClickListener,
    val state: String
) : RecyclerView.Adapter<AddMorePlaceAdapter.ViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_add_more_places, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems( position, arrayList!!, recyclerItemClickListener)

        arrayList[position].tilCity = holder.itemView.tilCity
        arrayList[position].ll_Pdf = holder.itemView.ll_Pdf
        arrayList[position].select_pdf = holder.itemView.select_pdf
        arrayList[position].select_image = holder.itemView.select_image

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
            arrayList[position].tilCity?.editText?.isEnabled = false
        }

        if(arrayList[position].document.toString().contains(".pdf")) {
            arrayList[position].select_image?.gone()
            arrayList[position].ll_Pdf?.visible()
            arrayList[position].select_pdf?.text = arrayList[position].documentname
        } else {
            arrayList[position].select_image?.visible()
            arrayList[position].ll_Pdf?.gone()
            if(arrayList[position].document != null) {
                arrayList[position].select_image?.loadURIRoundedCorner(
                    arrayList[position].document!!,
                    R.drawable.ic_image, 1
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(position: Int,
                      arrayList: ArrayList<PlaceDataModel>?,
                      recyclerItemClickListener: RecyclerMultipleItemClickListener) {

            itemView.tilCity.editText?.text = arrayList!![position].place.toEditable()


            itemView.etCity.setOnClickListener {
                if(!arrayList[adapterPosition].place!!.isEmpty()) {
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1, arrayList[adapterPosition].place)
                } else{
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1, arrayList[adapterPosition].place)
                }
            }

            itemView.etUploadDocument.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 2, arrayList[adapterPosition].document.toString())
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

    fun addItem(model: PlaceDataModel, mode: Int): Boolean {

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

    fun getAdapterArrayList(): ArrayList<PlaceDataModel>? {
        return arrayList
    }

    /*Update city */
    fun updateItem(position: Int, name: String, paxid: Int) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].place = name
            arrayList[position].placeid = paxid
            arrayList[position].tilCity?.editText!!.text = name.toEditable()

        }
    }

    /*Update pdf */
    fun updateItem2(position: Int, uri: Uri?, name: String?) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].document = uri
            arrayList[position].documentname = name

            arrayList[position].select_image?.gone()
            arrayList[position].ll_Pdf?.visible()
            arrayList[position].select_pdf?.text = arrayList[position].documentname
        }
    }

    /*Update image */
    fun updateItem3(position: Int, uri: Uri?) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].document = uri

            arrayList[position].select_image?.visible()
            arrayList[position].ll_Pdf?.gone()
            arrayList[position].select_pdf?.text = ""

            arrayList[position].select_image?.loadURIRoundedCorner(
                uri!!,
                R.drawable.ic_image,
                1
            )
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
                arrayList[i].tilCity?.isErrorEnabled = false
                arrayList[i].tilUploadDocument?.isErrorEnabled = false

            }
        }
    }

    fun isValidateItem(): Boolean {
        var emptyBox = true

        if (!arrayList.isNullOrEmpty()) {

            for (model in arrayList) {

                if (TextUtils.isEmpty(model.tilCity?.editText!!.text.trim())) {
                    model.tilCity?.error = "Select City"
                    emptyBox = false
                } else {
                    model.tilCity?.isErrorEnabled = false
                }

            }
        }
        return emptyBox
    }


}
package com.app.amtadminapp.activity.Vouchers.Hotels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.placeList
import com.app.amtadminapp.utils.AppConstant
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.isOnline
import com.app.amtadminapp.utils.loadUrlRoundedCorner
import com.app.amtadminapp.utils.toast
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.adapter_places.view.*
import java.util.*

class HotelPlaceAdapter(private val mContext: Context, private val arrData: ArrayList<placeList>
    ,val recyclerItemClickListener: RecyclerClickListener ) : RecyclerView.Adapter<HotelPlaceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_places, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(mContext, arrData[position],recyclerItemClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            context: Context,
            model: placeList,
            recyclerClickListener: RecyclerClickListener
        ) {

            if(model.CityName != null && model.CityName != "") {
                itemView.tbCity.text = model.CityName
            }

            if(model.HotelVoucherImage != null && model.HotelVoucherImage != "") {
                if(model.HotelVoucherImage.contains(".pdf")) {
                    itemView.select_image.gone()
                    itemView.ll_Pdf.visible()
                } else {
                    itemView.select_image.visible()
                    itemView.ll_Pdf.gone()

                    itemView.select_image.loadUrlRoundedCorner(
                        model.HotelVoucherImage!!,
                        R.drawable.ic_image, 1
                    )
                }
            }

            itemView.tbEDIT.setOnClickListener { view ->
                recyclerClickListener!!.onItemClickEvent(view, position, 1)
            }
            itemView.tbDELETE.setOnClickListener { view ->
                recyclerClickListener!!.onItemClickEvent(view, position, 2)
            }

            itemView.select_image.setOnClickListener {
                if(isOnline(context)) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.HotelVoucherImage))
                    context.startActivity(browserIntent)
                } else {
                    context.toast(context.resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
                }
            }

            itemView.ll_Pdf.setOnClickListener {
                if(isOnline(context)) {
                    var format = "https://docs.google.com/gview?embedded=true&url=%s"
                    val fullPath: String = java.lang.String.format(Locale.ENGLISH, format, model.HotelVoucherImage)
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                    context.startActivity(browserIntent)
                } else {
                    context.toast(context.resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrData.size
    }


}
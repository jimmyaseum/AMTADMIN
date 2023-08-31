package com.app.amtadminapp.adapter.Dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.model.response.PaymentListModel
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.row_payment_list.view.*
import kotlinx.android.synthetic.main.row_payment_list.view.LL_More
import kotlinx.android.synthetic.main.row_payment_list.view.card
import kotlinx.android.synthetic.main.row_payment_list.view.txtBranch
import kotlinx.android.synthetic.main.row_payment_list.view.txtReadLess
import kotlinx.android.synthetic.main.row_payment_list.view.txtReadMore
import java.util.*

class PaymentListAdapter(private val mContext: Context, private val arrData: ArrayList<PaymentListModel>) : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_payment_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position],mContext)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            model: PaymentListModel,
            mContext: Context
        ) {
            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.PaymentDate != null) {
                itemView.txtPaymentDate.text = model.PaymentDate
            }
            if(model.ReceiptNo != null) {
                itemView.txtReceiptNo.text = model.ReceiptNo
                itemView.txtReceiptNo.setPaintFlags(itemView.txtReceiptNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }
            if(model.TourBookingNo != null) {
                itemView.txtTourBokingNo.text = model.TourBookingNo
                itemView.txtTourBokingNo.setPaintFlags(itemView.txtTourBokingNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }
            if(model.Name != null) {
                itemView.txtName.text = model.Name
            }
            if(model.PaymentFor != null) {
                itemView.txtPaymentFor.text = model.PaymentFor
            }
            if(model.CompanyName != null) {
                itemView.txtCompanyName.text = model.CompanyName
            }
            if(model.PaymentType != null) {
                itemView.txtPaymentType.text = model.PaymentType
            }
            if(model.BookBy != null) {
                itemView.txtReceivedBy.text = model.BookBy
            }
            if(model.Branch != null) {
                itemView.txtBranch.text = model.Branch
            }

            itemView.txtReceiptNo.setOnClickListener {
                val fullpath =  model.PaymentLink + model.ReceiptNo
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullpath))
                mContext.startActivity(browserIntent)
            }

            itemView.txtTourBokingNo.setOnClickListener {
                val fullpath =  model.TourbookingLink + model.TourBookingNo
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullpath))
                mContext.startActivity(browserIntent)
            }

            itemView.txtReadMore.setOnClickListener {
                    itemView.LL_More.visible()
                    itemView.txtReadMore.gone()
            }

            itemView.txtReadLess.setOnClickListener {
                itemView.LL_More.gone()
                itemView.txtReadMore.visible()
            }

            val reminder = position % 3
            itemView.card.setBackgroundDrawable(ContextCompat.getDrawable(mContext, colors[reminder]))
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

}
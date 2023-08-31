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

import com.app.amtadminapp.utils.AppConstant
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.isOnline
import com.app.amtadminapp.utils.toast

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
            var colors = arrayOf(
                R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple
            )


            if (model.PaymentDate != null) {
                itemView.txtPaymentDate.text = model.PaymentDate
            }
            if (model.ReceiptNo != null) {
                itemView.txtReceiptNo.text = model.ReceiptNo
                itemView.txtReceiptNo.setPaintFlags(itemView.txtReceiptNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }
            if (model.TourBookingNo != null) {
                itemView.txtTourBokingNo.text = model.TourBookingNo
                itemView.txtTourBokingNo.setPaintFlags(itemView.txtTourBokingNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }
            if (model.Name != null) {
                itemView.txtName.text = model.Name
            }
            if (model.PaymentFor != null) {
                itemView.txtPaymentFor.text = model.PaymentFor
            }
            if (model.CompanyName != null) {
                itemView.txtCompanyName.text = model.CompanyName
            }
            if (model.PaymentType != null) {
                itemView.txtPaymentType.text = model.PaymentType
            }
            if (model.BookBy != null) {
                itemView.txtReceivedBy.text = model.BookBy
            }
            if (model.Branch != null) {
                itemView.txtBranch.text = model.Branch

                if (model.PaymentDate != null && model.PaymentDate != "") {
                    itemView.txtPaymentDate.text = model.PaymentDate
                } else {
                    itemView.LLPaymentDate.gone()
                }

                if (model.ReceiptNo != null && model.ReceiptNo != "") {
                    itemView.txtReceiptNo.text = model.ReceiptNo
                    itemView.txtReceiptNo.setPaintFlags(itemView.txtReceiptNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                } else {
                    itemView.LLReceiptNo.gone()
                }

                if (model.TourBookingNo != null && model.TourBookingNo != "") {
                    itemView.txtTourBokingNo.text = model.TourBookingNo
                    itemView.txtTourBokingNo.setPaintFlags(itemView.txtTourBokingNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                } else {
                    itemView.LLTBN.gone()
                }

                if (model.Name != null && model.Name != "") {
                    itemView.txtName.text = model.Name
                } else {
                    itemView.LLName.gone()
                }

                if (model.PaymentFor != null && model.PaymentFor != "") {
                    itemView.txtPaymentFor.text = model.PaymentFor
                } else {
                    itemView.LLPaymentFor.gone()
                }

                if (model.CompanyName != null && model.CompanyName != "") {
                    itemView.txtCompanyName.text = model.CompanyName
                } else {
                    itemView.LLCompany.gone()
                }

                if (model.PaymentType != null && model.PaymentType != "") {
                    itemView.txtPaymentType.text = model.PaymentType
                } else {
                    itemView.LLPaymentType.gone()
                }

                if (model.BookBy != null && model.BookBy != "") {
                    itemView.txtReceivedBy.text = model.BookBy
                } else {
                    itemView.LLReceivedBy.gone()
                }

                if (model.Branch != null && model.Branch != "") {
                    itemView.txtBranch.text = model.Branch
                } else {
                    itemView.LLBranch.gone()
                }

                if (model.Amount != null && model.Amount != "") {
                    itemView.txtAmount.text = model.Amount
                } else {
                    itemView.LLAmount.gone()
                }

                if (model.ReceiptImage != null && model.ReceiptImage != "") {
                    itemView.cardAttachment.visible()
                    itemView.cardAttachment.setOnClickListener {
                        if (isOnline(mContext)) {
                            if (model.ReceiptImage!!.contains(".pdf")) {
                                var format = "https://docs.google.com/gview?embedded=true&url=%s"
                                val fullPath: String = java.lang.String.format(
                                    Locale.ENGLISH,
                                    format,
                                    model.ReceiptImage
                                )
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                                mContext.startActivity(browserIntent)
                            } else {
                                val browserIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(model.ReceiptImage))
                                mContext.startActivity(browserIntent)
                            }
                        } else {
                            mContext.toast(
                                mContext.resources.getString(R.string.msg_no_internet),
                                AppConstant.TOAST_SHORT
                            )
                        }
                    }
                } else {
                    itemView.cardAttachment.gone()

                }

                itemView.txtReceiptNo.setOnClickListener {
                    val fullpath = model.PaymentLink + model.ReceiptNo
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullpath))
                    mContext.startActivity(browserIntent)
                }

                itemView.txtTourBokingNo.setOnClickListener {
                    val fullpath = model.TourbookingLink + model.TourBookingNo
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
                itemView.card.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        colors[reminder]
                    )
                )
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
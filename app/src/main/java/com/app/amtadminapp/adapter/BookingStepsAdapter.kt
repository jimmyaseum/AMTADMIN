package com.app.amtadminapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.databinding.AdapterBookingStepsBinding
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.BookingStepsModel

/**
 * Created by Pratik on 25/6/19.
 */
class BookingStepsAdapter(private val mContext: Context, private val arrayList: ArrayList<BookingStepsModel>?) : RecyclerView.Adapter<BookingStepsAdapter.ViewHolder>() {

    private var recyclerRowClick: RecyclerClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinder: AdapterBookingStepsBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(mContext), R.layout.adapter_booking_steps, parent, false) as AdapterBookingStepsBinding
        return ViewHolder(mBinder)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = arrayList!![position]

        val num: Int = position + 1
        holder.mBinder.tvStepNumber1.text = num.toString()
        holder.mBinder.tvStepTitle.text = model.name

        if (position == 0) {
            holder.mBinder.viewLeftLine.visibility = View.INVISIBLE
            holder.mBinder.viewRightLine.visibility = View.VISIBLE
        } else if (position == arrayList.size - 1) {
            holder.mBinder.viewLeftLine.visibility = View.VISIBLE
            holder.mBinder.viewRightLine.visibility = View.INVISIBLE
        } else {
            holder.mBinder.viewLeftLine.visibility = View.VISIBLE
            holder.mBinder.viewRightLine.visibility = View.VISIBLE
        }

        if (model.isCompleted!!) {
            holder.mBinder.tvStepNumber1.text = mContext.getString(R.string.true_tick)
//                itemView.tvStepNumber.text = "✔"
        }

        if (model.isSelected!!) {
            holder.mBinder.tvStepNumber1.setTextColor(Color.parseColor("#FFFFFF"))
            holder.mBinder.tvStepNumber1.setBackgroundResource(R.drawable.bg_primary_semi_rounded_corner)
            holder.mBinder.llStepNumberContainer.setBackgroundResource(R.drawable.bg_primary_semi_rounded_corner)
        } else {
            holder.mBinder.tvStepNumber1.setTextColor(Color.parseColor("#32BDC8"))
            holder.mBinder.tvStepNumber1.setBackgroundResource(R.drawable.bg_transparent_with_primary_semi_rounded_corner)
            holder.mBinder.llStepNumberContainer.setBackgroundResource(R.drawable.bg_transparent_with_primary_semi_rounded_corner)
        }

        holder.mBinder.llStepNumber.setOnClickListener {
            recyclerRowClick!!.onItemClickEvent(it, position, 1)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    fun setRecyclerRowClick(recyclerRowClick: RecyclerClickListener) {
        this.recyclerRowClick = recyclerRowClick
    }

    inner class ViewHolder(var mBinder: AdapterBookingStepsBinding) :
        RecyclerView.ViewHolder(mBinder.root)

    fun updateStepsColor(position: Int) {

        if(position < 6) {
        for (i in arrayList?.indices!!) {
            arrayList[i].isSelected = false
            arrayList[i].isCompleted = i < position
        }

        arrayList[position].isSelected = true
        notifyDataSetChanged()
        }
    }
}
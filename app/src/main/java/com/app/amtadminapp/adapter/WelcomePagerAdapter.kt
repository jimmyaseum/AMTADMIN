package com.app.amtadminapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.app.amtadminapp.R
import kotlinx.android.synthetic.main.adapter_welcome_pager.view.*

class WelcomePagerAdapter(val context: Context) : PagerAdapter() {

    private val inflater: LayoutInflater
    private val arrayImages = intArrayOf(
        R.drawable.welcome111, R.drawable.welcome222,
        R.drawable.welcome333
    )

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val itemView = inflater.inflate(R.layout.adapter_welcome_pager, container, false)!!
        itemView.imgTutorial.setImageResource(arrayImages[position])
        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return arrayImages.size
    }
}
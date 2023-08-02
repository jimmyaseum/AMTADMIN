package com.app.amtadminapp.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.Dashboard.*
import com.app.amtadminapp.model.response.DashBoardResponse
import com.app.amtadminapp.model.response.SectorModel
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_explore.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ExploreFragment : BaseFragment(), View.OnClickListener {

    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    var calDateFrom = Calendar.getInstance()
    var FromDate: String = ""

    var calDateTo = Calendar.getInstance()
    var ToDate: String = ""

    var sectorList: ArrayList<SectorModel> = ArrayList()

    // variable for our bar chart
    var barChart: BarChart? = null

    // variable for our bar data set.
    var barDataSet1: BarDataSet? = null

    // array list for storing entries.
    lateinit var barEntries: ArrayList<BarEntry>

    // creating a string array for displaying days.
//    var days = arrayOf("","Sunday", "Monday", "Tuesday", "Thursday", "Friday", "Saturday")
    lateinit var days: ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_explore, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        initListeners()
    }

    private fun initListeners() {

        views!!.edtFromDate.setOnClickListener(this)
        views!!.edtToDate.setOnClickListener(this)
        views!!.cardBooking.setOnClickListener(this)
        views!!.cardPayments.setOnClickListener(this)
        views!!.cardRouteVoucher.setOnClickListener(this)
        views!!.cardHotelVoucher.setOnClickListener(this)
        views!!.cardFlightTickets.setOnClickListener(this)
        views!!.cardPendingPayments.setOnClickListener(this)
        views!!.cardNext15DayDeparture.setOnClickListener(this)
        views!!.cardPendingForms.setOnClickListener(this)

        FromDate = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(calDateFrom.time)
        views!!.edtFromDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(calDateFrom.time))

        calDateTo.add(Calendar.DATE , 15)
        ToDate = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(calDateTo.time)
        views!!.edtToDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(calDateTo.time))

        callDashBoardApi()

    }

    override fun onClick(v: View?) {
        hideKeyboard(activity!!, v)
        when (v?.id) {
            R.id.edtFromDate -> {
                preventTwoClick(v)
                val dpd = DatePickerDialog(
                    activity!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDateFrom.set(Calendar.YEAR, year)
                        calDateFrom.set(Calendar.MONTH, monthOfYear)
                        calDateFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateSelectPickUpDate(calDateFrom)
                    },
                    calDateFrom.get(Calendar.YEAR),
                    calDateFrom.get(Calendar.MONTH),
                    calDateFrom.get(Calendar.DAY_OF_MONTH)
                )
                dpd.show()
            }
            R.id.edtToDate -> {
                preventTwoClick(v)
                val dpd = DatePickerDialog(
                    activity!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDateTo.set(Calendar.YEAR, year)
                        calDateTo.set(Calendar.MONTH, monthOfYear)
                        calDateTo.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateSelectDropDate(calDateTo)
                    },
                    calDateTo.get(Calendar.YEAR),
                    calDateTo.get(Calendar.MONTH),
                    calDateTo.get(Calendar.DAY_OF_MONTH)
                )
                dpd.show()
            }
            R.id.cardBooking -> {
                val intent = Intent(activity, BookingListActivity::class.java)
                intent.putExtra("FromDate",FromDate)
                intent.putExtra("ToDate",ToDate)
                startActivity(intent)
            }
            R.id.cardPayments -> {
                val intent = Intent(activity, PaymentListActivity::class.java)
                intent.putExtra("FromDate",FromDate)
                intent.putExtra("ToDate",ToDate)
                startActivity(intent)
            }
            R.id.cardRouteVoucher -> {
                val intent = Intent(activity, RouteVoucherListActivity::class.java)
                intent.putExtra("FromDate",FromDate)
                intent.putExtra("ToDate",ToDate)
                startActivity(intent)
            }
            R.id.cardHotelVoucher -> {
                val intent = Intent(activity, HotelVoucherListActivity::class.java)
                intent.putExtra("FromDate",FromDate)
                intent.putExtra("ToDate",ToDate)
                startActivity(intent)
            }
            R.id.cardFlightTickets -> {
                val intent = Intent(activity, FlightVoucherListActivity::class.java)
                intent.putExtra("FromDate",FromDate)
                intent.putExtra("ToDate",ToDate)
                startActivity(intent)
            }
            R.id.cardPendingPayments -> {
                val intent = Intent(activity, PendingPaymentListActivity::class.java)
                intent.putExtra("FromDate",FromDate)
                intent.putExtra("ToDate",ToDate)
                startActivity(intent)
            }
            R.id.cardNext15DayDeparture -> {
                val intent = Intent(activity, DepartureListActivity::class.java)
                intent.putExtra("FromDate",FromDate)
                intent.putExtra("ToDate",ToDate)
                startActivity(intent)
            }
            R.id.cardPendingForms -> {
                val intent = Intent(activity, PendingFormListActivity::class.java)
                intent.putExtra("FromDate",FromDate)
                intent.putExtra("ToDate",ToDate)
                startActivity(intent)
            }
        }
    }

    /** AI005
     * This method is to date change
     */
    private fun updateSelectPickUpDate(cal: Calendar) {
        FromDate = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        views!!.edtFromDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
        callDashBoardApi()
    }

    /** AI005
     * This method is to date change
     */
    private fun updateSelectDropDate(cal: Calendar) {
        ToDate = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        views!!.edtToDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
        callDashBoardApi()
    }

    // region Get DashBoard Api
    private fun callDashBoardApi() {

        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(activity!!)
        }

        val jsonObject = JSONObject()

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toString()

        jsonObject.put("CreatedBy", CreatedBy)
        jsonObject.put("FromDate", FromDate)
        jsonObject.put("ToDate", ToDate)

        val call = ApiUtils.apiInterface.GetDashBoard(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<DashBoardResponse> {
            override fun onResponse(call: Call<DashBoardResponse>, response: Response<DashBoardResponse>) {
                if (response.code() == 200) {
                    val arrayList =  response.body()?.Data
                    if (response.body()?.Status == 200) {

                        views!!.txt_Booking.text = arrayList!!.TotalBooking.toString()
                        views!!.txt_Payments.text = arrayList!!.TotalPayment.toString()
                        views!!.txt_RouteVoucher.text = arrayList!!.TotalRouteVoucher.toString()
                        views!!.txt_HotelVoucher.text = arrayList!!.TotalHotelVoucher.toString()
                        views!!.txt_FlightTickets.text = arrayList!!.TotalAirlineVoucher.toString()
                        views!!.txt_PendingPayments.text = arrayList!!.PendingPayment.toString()
                        views!!.txt_Next15DayDeparture.text = arrayList!!.TotalDeparture.toString()
                        views!!.txt_PendingForms.text = arrayList!!.TotalPendingForms.toString()

                        sectorList = arrayList!!.SectorList
//                        if(sectorList.size > 0)
//                        ShowChart(sectorList)
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<DashBoardResponse>, t: Throwable) {
                hideProgress()
            }
        })
    }

    private fun ShowChart(sectorList: ArrayList<SectorModel>) {
        barChart = views!!.findViewById(R.id.idBarChart)

        // creating a new bar data set.
        barDataSet1 = BarDataSet(getBarEntriesOne(sectorList), "Sectors")
        barDataSet1!!.color = activity!!.resources.getColor(R.color.color1)

        val data = BarData(barDataSet1)
        barChart!!.data = data
        barChart!!.description.isEnabled = false

        val xAxis = barChart!!.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(days)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        barChart!!.isDragEnabled = true
        barChart!!.setVisibleXRangeMaximum(3f)
        data.barWidth = 0.20f
//        barChart!!.xAxis.axisMinimum = 0f
        barChart!!.animate()
        barChart!!.invalidate()

    }

    private fun getBarEntriesOne(sectorList: ArrayList<SectorModel>): ArrayList<BarEntry> {

        barEntries = ArrayList()
        days = ArrayList()
        days.add("")

        for(i in 0 until sectorList.size) {
            val pos: Float = (i + 1).toFloat()
            val value: Float = sectorList[i].TotalTour!!.toFloat()
            barEntries.add(BarEntry(pos, value))
            days.add(sectorList[i].SectorName!!)
        }

        return barEntries
    }

}
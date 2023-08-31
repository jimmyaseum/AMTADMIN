package com.app.amtadminapp.activity.Vouchers.hotel

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.adapter.AddMoreHotelDetailsAdapter
import com.app.amtadminapp.adapter.dialog.*
import com.app.amtadminapp.fragment.BaseFragment
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.interFase.RecyclerMultipleItemClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.model.response.booking.MealListModel
import com.app.amtadminapp.model.response.booking.MealListResponse
import com.app.amtadminapp.model.response.booking.RoomCategoryListModel
import com.app.amtadminapp.model.response.booking.RoomCategoryListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.fragment_hotel_details.view.*
import kotlinx.android.synthetic.main.fragment_hotel_details.view.llContent
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HotelDetailsFragment(var state: String) : BaseFragment(), RecyclerMultipleItemClickListener {

    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    var calDate = Calendar.getInstance()

    var arrayListPlace: ArrayList<AllCityListModel>? = ArrayList()

    var arrayListP: ArrayList<AddMoreHotelDetailsModel>? = null
    lateinit var adapterHotelDetails: AddMoreHotelDetailsAdapter

    var arrayListMeal: ArrayList<MealListModel>? = ArrayList()
    var arrayListRoomCategory: ArrayList<RoomCategoryListModel>? = ArrayList()
    var arrayListVender: ArrayList<VenderListModel>? = ArrayList()

    var mealplanID: Int = 0
    var mealplanName: String = ""
    var roomCatID: Int = 0
    var roomCatName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_hotel_details, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)

        views!!.rvHOTEL.layoutManager = LinearLayoutManager(
            activity!!.applicationContext,
            RecyclerView.VERTICAL, false
        )

        views!!.rvHOTEL.isNestedScrollingEnabled = false

        GetAllMealAPI(1)
        GetAllRoomCategoryAPI(1)
        GetAllVenderAPI(1)

        if(state.equals("add")) {
//            callDetailApi(tourbookingid.toInt())
        } else {
//            callHotelBookingApi(hotelid)
        }
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int, name: String) {
        hideKeyboard(activity!!.applicationContext, view)

        when (view.id)
        {
            R.id.imgDelete -> {
                if (::adapterHotelDetails.isInitialized) {
                    adapterHotelDetails.remove(position)
                }
            }
            R.id.LLcardButtonAddMore -> {
                preventTwoClick(view)
                if (::adapterHotelDetails.isInitialized) {
                    if(name != "") {
                        //Calculate date from no of night
                        val c = Calendar.getInstance()
                        c.time = SimpleDateFormat("dd/MM/yyyy").parse(name)
                        c.add(Calendar.DATE, type)
                        val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                        val mcheckoutdate = sdf1.format(c.time)

                        adapterHotelDetails.addItem(position + 1, AddMoreHotelDetailsModel(checkindate = name , nights = type.toString() , checkoutdate = mcheckoutdate))
                    }
                }
            }
            R.id.etPlace -> {
                preventTwoClick(view)
                if (::adapterHotelDetails.isInitialized) {
                    showPlaceDialog(position)
                }
            }
            R.id.etHotel -> {
                preventTwoClick(view)
                if (::adapterHotelDetails.isInitialized) {
                    callHotelDetailApi(type,position)
                }
            }
            R.id.etPlan -> {
                preventTwoClick(view)
                if (::adapterHotelDetails.isInitialized) {
                    selectMealDialog(position)
                }
            }
            R.id.etRoomCat -> {
                preventTwoClick(view)
                if (::adapterHotelDetails.isInitialized) {
                    selectRoomCategoryDialog(position)
                }
            }
            R.id.etSupplier -> {
                preventTwoClick(view)
                if (::adapterHotelDetails.isInitialized) {
                    selectVenderDialog(position)
                }
            }
            R.id.etCheckInDate -> {
                if (::adapterHotelDetails.isInitialized) {
                    showDatePickerDialog(position, 1, type)
                }
            }
            R.id.etNights -> {
                if (::adapterHotelDetails.isInitialized) {
//                    adapterHotelDetails.updateItemDate1(position)
                    adapterHotelDetails.updateItemNight()
                }
            }
        }
    }

    // region Place By Sector
    /* AI005 */
    private fun GetAllCityBYSectorAPI(sectorId: Int) {

        val jsonObject = JSONObject()
        jsonObject.put("SectorID", sectorId)

        val call = ApiUtils.apiInterface.getAllCityBySector(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<AllCityListResponse> {
            override fun onResponse(call: Call<AllCityListResponse>, response: Response<AllCityListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        arrayListPlace!!.clear()
                        arrayListPlace = response.body()?.Data!!
                    }
                }
            }

            override fun onFailure(call: Call<AllCityListResponse>, t: Throwable) {

                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open Pax Type dialog
     */
    private fun showPlaceDialog(position: Int) {
        var dialogSelectPlace = Dialog(activity!!)
        dialogSelectPlace.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectPlace.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectPlace.window!!.attributes)

        dialogSelectPlace.window!!.attributes = lp
        dialogSelectPlace.setCancelable(true)
        dialogSelectPlace.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectPlace.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectPlace.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectPlace.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectPlace.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogPlaceAdapter(activity!!, arrayListPlace!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                val id = arrayListPlace!![pos].CityID!!
                val name = arrayListPlace!![pos].City!!
                adapterHotelDetails.updateItemPlace(position, name, id)
                dialogSelectPlace!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectPlace!!.show()
    }
    // endregion

    private fun setDefaultDataPlace(pos: Int) {
        arrayListP = ArrayList()
        arrayListP?.add(pos, AddMoreHotelDetailsModel())
        setAdapterData(arrayListP)
    }

    private fun setAdapterData(arrayList: ArrayList<AddMoreHotelDetailsModel>?) {

        views!!.llContent.visible()

        adapterHotelDetails = AddMoreHotelDetailsAdapter(arrayList, this)
        views!!.rvHOTEL.adapter = adapterHotelDetails

        Handler().postDelayed({  hideProgress() }, 1500)
    }

    private fun showDatePickerDialog(position: Int, i: Int, night: Int) {
        val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calDate.set(Calendar.YEAR, year)
            calDate.set(Calendar.MONTH, monthOfYear)
            calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val checkindate = SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(calDate.time)

            //Calculate date from no of night
            val c = Calendar.getInstance()
            c.time = SimpleDateFormat("dd/MM/yyyy").parse(checkindate)
            c.add(Calendar.DATE, night)
            val sdf1 = SimpleDateFormat("dd/MM/yyyy")
            val checkoutdate = sdf1.format(c.time)

            adapterHotelDetails.updateItemDate(position, checkindate , checkoutdate)
        },
            calDate.get(Calendar.YEAR),
            calDate.get(Calendar.MONTH),
            calDate.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }

    private fun callHotelDetailApi(placeid: Int, position: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.getHotelByPlace(placeid)

        call.enqueue(object : Callback<HotelListResponse> {
            override fun onResponse(call: Call<HotelListResponse>, response: Response<HotelListResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        showHotelDialog(arrayList, position)
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<HotelListResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    private fun showHotelDialog(arrayList: ArrayList<HotelListModel>, position: Int) {
        var dialogSelectHotel = Dialog(activity!!)
        dialogSelectHotel.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectHotel.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectHotel.window!!.attributes)

        dialogSelectHotel.window!!.attributes = lp
        dialogSelectHotel.setCancelable(true)
        dialogSelectHotel.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectHotel.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectHotel.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectHotel.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectHotel.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogHotelAdapter(activity!!, arrayList)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                val id = arrayList!![pos].ID!!
                val name = arrayList!![pos].HotelName!!
                adapterHotelDetails.updateItemHotel(position, name, id)
                dialogSelectHotel!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectHotel!!.show()
    }

    // region meal
    /** AI005
     * This method is to retrived meal plan data from api
     */
    private fun GetAllMealAPI(mode: Int) {

        val call = ApiUtils.apiInterface.getAllMeal()
        call.enqueue(object : Callback<MealListResponse> {
            override fun onResponse(call: Call<MealListResponse>, response: Response<MealListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {

                        arrayListMeal = response.body()?.Data!!

                        if(arrayListMeal!!.size > 0) {
                            for(i in 0 until arrayListMeal!!.size) {
                                if(arrayListMeal!![i].Title.equals("EP")) {
                                    mealplanID = arrayListMeal!![i].ID!!
                                    mealplanName = arrayListMeal!![i].Title!!
                                }
                            }
                        }
                    } else {
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<MealListResponse>, t: Throwable) {
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open meal dialog
     */
    private fun selectMealDialog(position: Int) {
        var dialogSelectMeal = Dialog(activity!!)
        dialogSelectMeal.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectMeal.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectMeal.window!!.attributes)

        dialogSelectMeal.window!!.attributes = lp
        dialogSelectMeal.setCancelable(true)
        dialogSelectMeal.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectMeal.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectMeal.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectMeal.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectMeal.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogMealAdapter(activity!!, arrayListMeal!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                val id = arrayListMeal!![pos].ID!!
                val name = arrayListMeal!![pos].Title!!
                adapterHotelDetails.updateItemPlan(position, name, id)
                dialogSelectMeal!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectMeal!!.show()
    }
    // endregion

    // region RoomCategory
    /** AI005
     * This method is to retrived RoomCategory data from api
     */
    private fun GetAllRoomCategoryAPI(mode: Int) {
        val call = ApiUtils.apiInterface.getAllRoomCategory()
        call.enqueue(object : Callback<RoomCategoryListResponse> {
            override fun onResponse(call: Call<RoomCategoryListResponse>, response: Response<RoomCategoryListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        arrayListRoomCategory = response.body()?.Data!!
                        if(arrayListRoomCategory!!.size > 0) {
                            for(i in 0 until arrayListRoomCategory!!.size) {
                                if(arrayListRoomCategory!![i].Title.equals("STANDARD")) {
                                    roomCatID = arrayListRoomCategory!![i].ID!!
                                    roomCatName = arrayListRoomCategory!![i].Title!!
                                }
                            }
                        }
                    } else {
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<RoomCategoryListResponse>, t: Throwable) {
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open RoomCategory dialog
     */
    private fun selectRoomCategoryDialog(position: Int) {
        var dialogSelectRoomCategory = Dialog(activity!!)
        dialogSelectRoomCategory.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectRoomCategory.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectRoomCategory.window!!.attributes)

        dialogSelectRoomCategory.window!!.attributes = lp
        dialogSelectRoomCategory.setCancelable(true)
        dialogSelectRoomCategory.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectRoomCategory.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectRoomCategory.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectRoomCategory.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectRoomCategory.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogRoomCategoryAdapter(activity!!, arrayListRoomCategory!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                val id = arrayListRoomCategory!![pos].ID!!
                val name = arrayListRoomCategory!![pos].Title!!
                adapterHotelDetails.updateItemRoomCategory(position, name, id)
                dialogSelectRoomCategory!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectRoomCategory!!.show()
    }
    // endregion

    //region Supplier

    /** AI005
     * This method is to retrived Vender List data from api
     */
    private fun GetAllVenderAPI(mode: Int) {
        val call = ApiUtils.apiInterface.getAllVender()
        call.enqueue(object : Callback<VenderListResponse> {
            override fun onResponse(call: Call<VenderListResponse>, response: Response<VenderListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        arrayListVender = response.body()?.Data!!
                    } else {
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<VenderListResponse>, t: Throwable) {
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open Vender dialog
     */
    private fun selectVenderDialog(position: Int) {
        var dialogSelectVender = Dialog(activity!!)
        dialogSelectVender.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectVender.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectVender.window!!.attributes)

        dialogSelectVender.window!!.attributes = lp
        dialogSelectVender.setCancelable(true)
        dialogSelectVender.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectVender.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectVender.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectVender.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectVender.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogVenderAdapter(activity!!, arrayListVender!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                val id = arrayListVender!![pos].ID!!
                val name = arrayListRoomCategory!![pos].Title!!
                adapterHotelDetails.updateItemSupplier(position, name, id)

                dialogSelectVender!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter
        edtSearchCustomer.gone()
        dialogSelectVender!!.show()
    }

    //  endregion

    private fun callDetailApi(tourbookingid: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.GetMultiTourBookingView(tourbookingid)

        call.enqueue(object : Callback<MultiTourBookingViewResponse> {
            override fun onResponse(call: Call<MultiTourBookingViewResponse>, response: Response<MultiTourBookingViewResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        val NoofRooms = arrayList[0].TotalNoOfRooms.toString()

                        if(arrayList!!.size > 0) {
                            val arrayListPlace = arrayList[0].places
                            if(arrayListPlace!!.size > 0) {
                                val arrayList: ArrayList<AddMoreHotelDetailsModel> = ArrayList()

                                for (i in 0 until arrayListPlace.size) {

                                    var mealid = 0
                                    var mealname = ""
                                    if(arrayListMeal!!.size > 0) {
                                        for (j in 0 until arrayListMeal!!.size) {
                                            if (arrayListPlace[i].MealPlanID != null) {
                                                if (arrayListMeal!![j].ID == arrayListPlace!![i].MealPlanID!!) {
                                                    mealid = arrayListMeal!![j].ID!!
                                                    mealname = arrayListMeal!![j].Title!!
                                                }
                                            }
                                        }
                                    }

                                    var roomcatids = 0
                                    var roomcatnames = ""
                                    if(arrayListRoomCategory!!.size > 0) {
                                        for(j in 0 until arrayListRoomCategory!!.size) {
                                            if(arrayListPlace[i].RoomCategoryID != null) {
                                                if (arrayListRoomCategory!![j].ID == arrayListPlace[i].RoomCategoryID) {
                                                    roomcatids = arrayListRoomCategory!![j].ID!!
                                                    roomcatnames = arrayListRoomCategory!![j].Title!!
                                                }
                                            }
                                        }
                                    }

                                    arrayList.add(AddMoreHotelDetailsModel(
                                        placeid = arrayListPlace[i].CityID!!,
                                        place = arrayListPlace[i].City!!,
                                        nights = arrayListPlace[i].NoOfNights.toString() ,
                                        checkindate = arrayListPlace[i].CheckInDate!!,
                                        checkoutdate = arrayListPlace[i].CheckOutDate!!,
                                        roomcatid = roomcatids,
                                        roomcat = roomcatnames,
                                        planid = mealid,
                                        plan = mealname,
                                        room = NoofRooms
                                    ))
                                }

                                setAdapterData(arrayList)
                            }
                    }
                }
            }
            }
            override fun onFailure(call: Call<MultiTourBookingViewResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun callHotelBookingApi(hotelid: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.GetHotelBookingFindBy(hotelid)

        call.enqueue(object : Callback<HotelBookingViewResponse> {
            override fun onResponse(call: Call<HotelBookingViewResponse>, response: Response<HotelBookingViewResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        GetAllCityBYSectorAPI(arrayList.SectorID!!)
//                        SetAPIData(arrayList)
                    }
                }
            }
            override fun onFailure(call: Call<HotelBookingViewResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

}
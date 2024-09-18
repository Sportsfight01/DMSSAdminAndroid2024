package com.dmss.dmssadminmaintanance.model

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Html
import com.dmss.dmssadminmaintanance.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object Utils {

    var selectedGender:String=""
    lateinit var progressDialog:ProgressDialog
    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("d-M-yyyy")
        val currentDate = sdf.format(Date())

        return currentDate
    }
     fun showAlertDialog(context:Context,title:String)
    {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(title)
        builder.setTitle(context.getString(R.string.dmss))
        builder.setCancelable(false)
        builder.setPositiveButton(
            Html.fromHtml("<font color=" + context.resources.getColor(R.color.black) + ">OK</font>"),
            DialogInterface.OnClickListener {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()
                dialog.dismiss()


            })
        /* builder.setNegativeButton(
             Html.fromHtml("<font color=" + this.resources.getColor(R.color.black) + ">No</font>"),
             DialogInterface.OnClickListener {
                     dialog: DialogInterface, _: Int -> dialog.dismiss()
                 dialog.dismiss()
             })*/
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }
    fun chooseGender(context:Context,title:String)
    {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(title)
        builder.setTitle(context.getString(R.string.dmss))
        builder.setCancelable(false)
        builder.setPositiveButton(
            Html.fromHtml("<font color=" + context.resources.getColor(R.color.black) + ">OK</font>"),
            DialogInterface.OnClickListener {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()
                dialog.dismiss()


            })
        /* builder.setNegativeButton(
             Html.fromHtml("<font color=" + this.resources.getColor(R.color.black) + ">No</font>"),
             DialogInterface.OnClickListener {
                     dialog: DialogInterface, _: Int -> dialog.dismiss()
                 dialog.dismiss()
             })*/
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }
     fun setCalender(context: Context,callback: (String) -> Unit){

        val c = Calendar.getInstance()

        // on below line we are getting
        // our day, month and year.
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // on below line we are creating a
        // variable for date picker dialog.
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            context,
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                val date = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
//                binding.etSelectDateInput.setText(date)
                callback.invoke(date)

            },
            // on below line we are passing year, month
            // and day for the selected date in our date picker.
            year,
            month,
            day
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
    }
    fun postCalenderCalback(callback: (String) -> Unit) {
        // make request and return immediately
        // arrange callback to be invoked later
    }
    fun showProgressDialog(context: Context){
         progressDialog = ProgressDialog(context)
//        progressDialog.setTitle("Kotlin Progress Bar")
        progressDialog.setMessage("please wait")
        progressDialog.show()
    }
    fun dismissProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss()
        }
    }
    fun getCurrentHour():String{
        val sdf = SimpleDateFormat("HH")
        val currentDate = sdf.format(Date())
//        System.out.println(" C DATE is  "+currentDate)
        return currentDate+":00"
    }
    fun confirmationAlertAlertDialog(context:Context,title:String, callback:(Boolean)->Unit)
    {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(title)
        builder.setTitle(context.getString(R.string.dmss))
        builder.setCancelable(false)
        builder.setPositiveButton(
            Html.fromHtml("<font color=" + context.resources.getColor(R.color.black) + ">Yes</font>"),
            DialogInterface.OnClickListener {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()
                callback.invoke(true)
                dialog.dismiss()


            })
         builder.setNegativeButton(
             Html.fromHtml("<font color=" + context.resources.getColor(R.color.black) + ">No</font>"),
             DialogInterface.OnClickListener {
                     dialog: DialogInterface, _: Int -> dialog.dismiss()
                 callback.invoke(false)

                 dialog.dismiss()
             })
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }
}
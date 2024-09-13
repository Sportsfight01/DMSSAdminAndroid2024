package com.dmss.dmssadminmaintanance.model

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Html
import com.dmss.dmssadminmaintanance.R
import java.text.SimpleDateFormat
import java.util.Date

object Utils {

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
}
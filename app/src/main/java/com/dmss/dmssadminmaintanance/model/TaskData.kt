package com.dmss.dmssadminmaintanance.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskData(val category:String,
                    val subCategory:String,
                    val image:String,
                    val date:String,
                    val month:String,
                    val assignTo:String,

                    val status:String) : Parcelable
@Parcelize
data class SuppliesData(val itemName:String,
                    val NoOfItems:Int,
                    ) : Parcelable

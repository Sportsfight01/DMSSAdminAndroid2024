package com.dmss.dmssadminmaintanance.model

data class CheckBoxModel (
    /**
     * the position of the item in the list
     */
    var position:Int= 0,
    var checked:Boolean = false,
    var text:String = "",
    var date:String = "",
    var id:Int = 0

)
data class PantryListItems (
    /**
     * the position of the item in the list
     */
    var position:Int= 0,
    var checked:Boolean = false,
    var text:String = ""

)

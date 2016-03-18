package com.EWIT.FrenchCafe.model.dao

import com.google.gson.annotations.SerializedName

/**
 * Created by Euro on 3/18/16 AD.
 */
object NetworkModel {
    data class TravelInfo(val rows: List<Elements>, @SerializedName("error_message") val errorMessage: String, val status: String)
    data class Elements(val elements: List<Element>)
    data class Element(val distance: ElementValue, val duration: ElementValue, @SerializedName("duration_in_traffic") val durationInTraffic: ElementValue)
    data class ElementValue(val text: String, val value: Long)
}
package task.emango.rest.response

import com.google.gson.annotations.SerializedName

data class CountryListData(
    @SerializedName("name") val name : String,
    @SerializedName("exam") val exam : String,
    @SerializedName("college") val college : String,
)

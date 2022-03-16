package task.emango.rest.response

import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @SerializedName("success") val success : Boolean,
    @SerializedName("data") val data : List<CountryListData>?,
    @SerializedName("error") val error : String?
)

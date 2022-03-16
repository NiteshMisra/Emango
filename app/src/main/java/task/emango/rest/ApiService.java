package task.emango.rest;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiService {

    @FormUrlEncoded
    @POST("/student/study-abroad-country-list-mobile/")
    Observable<Response<ResponseBody>> getCountryList(
            @Field("tokenKey") String tokenKey
    );

}

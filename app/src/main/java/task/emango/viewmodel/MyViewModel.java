package task.emango.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import task.emango.rest.RetrofitClient;
import task.emango.rest.response.CountryListData;
import task.emango.rest.response.CountryResponse;
import task.emango.utils.Constants;

public class MyViewModel extends ViewModel {

    public LiveData<CountryResponse> getCountryList(Context context){
        MutableLiveData<CountryResponse> data = new MutableLiveData<>();

        RetrofitClient retrofitClient = new RetrofitClient(context);
        retrofitClient.getApiService().getCountryList(Constants.TOKEN_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        data.setValue(null);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful()) {
                                String body = response.body().string();
                                Type type = new TypeToken<ArrayList<CountryListData>>(){}.getType();
                                ArrayList<CountryListData> list = new Gson().fromJson(body, type);
                                data.setValue(new CountryResponse(true,list,null));
                            } else {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                CountryResponse errorResponse = new CountryResponse(false,null,jObjError.getString("error"));
                                data.setValue(errorResponse);
                            }
                        }catch (Exception e){
                            data.setValue(null);
                        }
                    }
                });

        return data;
    }

}

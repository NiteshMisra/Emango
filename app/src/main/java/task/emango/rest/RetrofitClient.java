package task.emango.rest;

import static task.emango.utils.UtilsKt.isConnectedToInternet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.BuildConfig;
import task.emango.utils.Constants;

public class RetrofitClient {

    private final Retrofit mRetrofit;
    private final Context context;
    private BroadcastReceiver connectionReceiver;
    private LocalBroadcastManager lbm;
    private NetworkConnectionInterceptor connectionInterceptor;

    public RetrofitClient(Context context) {
        this.context = context;
        registerReceiver();
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient client = getAuthHttpClient()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    private void registerReceiver() {
        connectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnectedToInternet(context)) {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                if (lbm != null) lbm.unregisterReceiver(connectionReceiver);
            }
        };

        connectionInterceptor = new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                boolean isConnected = isConnectedToInternet(context);
                if (!isConnected) {
                    IntentFilter intentFilter = new IntentFilter("connection");
                    lbm = LocalBroadcastManager.getInstance(context.getApplicationContext());
                    lbm.registerReceiver(connectionReceiver, intentFilter);
                }
                return isConnected;
            }

            @Override
            public void onInternetUnavailable() {
                // we can broadcast this event to activity/fragment/service
                // through LocalBroadcastReceiver or
                // RxBus/EventBus
                // also we can call our own interface method
                // like this.
                lbm.sendBroadcast(new Intent("connection"));
            }
        };
    }

    private OkHttpClient.Builder getAuthHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.addInterceptor(chain -> {
            // Customize the request
            Request request = chain.request().newBuilder()
                    .method(chain.request().method(), chain.request().body())
                    .build();
            return chain.proceed(request);
        });

        HttpLoggingInterceptor mLogging = new HttpLoggingInterceptor();
        mLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(mLogging);

        httpClient.addInterceptor(connectionInterceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);

        return httpClient;
    }

    public ApiService getApiService() {
        return mRetrofit.create(ApiService.class);
    }

}
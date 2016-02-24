package okhttp.demo.com.okhttpdemo.retrofitDemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import okhttp.demo.com.okhttpdemo.R;
import okhttp.demo.com.okhttpdemo.retrofitDemo.models.RepositoryModel;
import okhttp.demo.com.okhttpdemo.retrofitDemo.services.RepoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends Activity implements View.OnClickListener {
    private final static String TAG = RetrofitActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        findViewById(R.id.retrofit_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "retrofit button click");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RepoService repoService = retrofit.create(RepoService.class);
        Call<List<RepositoryModel>> call = repoService.getRepositories();

        call.enqueue(new Callback<List<RepositoryModel>>() {
            @Override
            public void onResponse(Call<List<RepositoryModel>> call, Response<List<RepositoryModel>> response) {
                Log.d(TAG, response.getClass().toString());
                if (!response.isSuccess()) {
                    Log.d(TAG, "response ok, but something's wrong!");
                } else {
                    Log.d(TAG, response.body().get(0).getLanguage());
                }
            }

            @Override
            public void onFailure(Call<List<RepositoryModel>> call, Throwable t) {
                Log.d(TAG, "Failed");
            }
        });
    }
}

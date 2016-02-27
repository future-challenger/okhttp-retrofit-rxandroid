package okhttp.demo.com.okhttpdemo.retrofitDemo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View

import okhttp.demo.com.okhttpdemo.R
import okhttp.demo.com.okhttpdemo.retrofitDemo.models.RepositoryModel
import okhttp.demo.com.okhttpdemo.retrofitDemo.services.RepoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitActivity : Activity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)

        findViewById(R.id.retrofit_button).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        Log.d(TAG, "retrofit button click")

        val retrofit = Retrofit.Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create()).build()
        val repoService = retrofit.create(RepoService::class.java)
        val call = repoService.repositories

        call.enqueue(object : Callback<List<RepositoryModel>> {
            override fun onResponse(call: Call<List<RepositoryModel>>, response: Response<List<RepositoryModel>>) {
                Log.d(TAG, response.javaClass.toString())
                if (!response.isSuccess) {
                    Log.d(TAG, "response ok, but something's wrong!")
                } else {
                    Log.d(TAG, response.body()[0].language)
                }
            }

            override fun onFailure(call: Call<List<RepositoryModel>>, t: Throwable) {
                Log.d(TAG, "Failed")
            }
        })
    }

    companion object {
        private val TAG = RetrofitActivity::class.java.simpleName
    }
}

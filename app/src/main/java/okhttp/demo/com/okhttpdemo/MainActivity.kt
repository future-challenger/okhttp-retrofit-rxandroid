package okhttp.demo.com.okhttpdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import okhttp.demo.com.okhttpdemo.KotlinDemo.KotlinActivity
import okhttp.demo.com.okhttpdemo.RecycleView.KotlinRecycleActivity
import okhttp.demo.com.okhttpdemo.RxJavaDemo.RxJavaActivity
import okhttp.demo.com.okhttpdemo.retrofitDemo.RetrofitActivity
import okhttp3.*
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors

class MainActivity : Activity(), AdapterView.OnItemClickListener {

    private val requestHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                REQUEST_SUCCESS -> Toast.makeText(this@MainActivity, "SUCCESSFUL", Toast.LENGTH_SHORT).show()
                REQUEST_FAIL -> Toast.makeText(this@MainActivity, "request failed", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }

    private val mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionListView = findViewById(R.id.section_listview) as ListView
        val ds = ArrayList<String>()
        ds.add("sync request")
        ds.add("async request")
        ds.add("retrofit")
        ds.add("RxJava in Android")
        ds.add("Kotlin demo")
        ds.add("Recyeler View")
        sectionListView.adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, ds)
        sectionListView.onItemClickListener = this

        val cachedExecutor = Executors.newCachedThreadPool()
        cachedExecutor.execute { }

    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (position == 0) {
            performSyncHttpRequest()
        } else if (position == 1) {
            performAsyncHttpRequest()
        } else if (position == 2) {
            val i = Intent(this@MainActivity, RetrofitActivity::class.java)
            startActivity(i)
        } else if (position == 3) {
            val i = Intent(this@MainActivity, RxJavaActivity::class.java)
            startActivity(i)
        } else if (position == 4) {
            val i = Intent(this@MainActivity, KotlinActivity::class.java)
            startActivity(i)
        } else if (position == 5) {
            val i = Intent(this@MainActivity, KotlinRecycleActivity::class.java)
            startActivity(i)
        }
    }

    private fun performSyncHttpRequest() {

        val requestTask = Runnable {
            val msg = requestHandler.obtainMessage()
            try {
                val client = OkHttpClient()

                val request = Request.Builder().url("http://www.baidu.com").build()
                val call = client.newCall(request)
                val response = call.execute()

                if (!response.isSuccessful) {
                    msg.what = REQUEST_FAIL
                } else {
                    Log.d(TAG, response.body().string())
                    msg.what = REQUEST_SUCCESS
                }
            } catch (ex: IOException) {
                msg.what = REQUEST_FAIL
            } finally {
                // send the message
                msg.sendToTarget()
            }
        }

        val requestThread = Thread(requestTask)
        requestThread.start()
    }

    private fun performAsyncHttpRequest() {
        val client = OkHttpClient()

        val request = Request.Builder().url("http://www.baidu.com").build()
        val call = client.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val errorMMessage = e.message

                if (Looper.getMainLooper().thread === Thread.currentThread()) {
                    Log.d(TAG, "Main Thread")
                } else {
                    Log.d(TAG, "Not Main Thread")
                }

                this@MainActivity.runOnUiThread { Toast.makeText(this@MainActivity, errorMMessage, Toast.LENGTH_SHORT).show() }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (Looper.getMainLooper().thread === Thread.currentThread()) {
                    Log.d(TAG, "Main Thread")
                } else {
                    Log.d(TAG, "Not Main Thread")
                }

                this@MainActivity.runOnUiThread {
                    Log.d(TAG, "code: ")

                    Toast.makeText(this@MainActivity, response.code().toString(), Toast.LENGTH_SHORT).show()
                }

                this@MainActivity.mView?.post { Log.d("UI thread", "I am the UI thread") }
            }
        })
    }

    //    private class ItemAdapter(context: Context, resource: Int, objects: List<String>) : ArrayAdapter<String>(context, resource, objects) {
    //
    //        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    //            return super.getView(position, convertView, parent)
    //        }
    //    }

    companion object {
        private val TAG = "###okHttp"

        private val REQUEST_FAIL = 10101010
        private val REQUEST_SUCCESS = 2001010101
    }
}

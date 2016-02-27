package okhttp.demo.com.okhttpdemo.KotlinDemo

import android.app.Fragment
import android.util.Log

/**
 * Created by uncle_charlie on 27/2/16.
 */
open class MyFragment : Fragment() {

    companion object {
        fun newInstance(): MyFragment = MyFragment()
    }
}

interface SessionCloseable {
    fun closeSession() {
        Log.d(SessionCloseable::class.java.simpleName, "Closing...")
    }
}
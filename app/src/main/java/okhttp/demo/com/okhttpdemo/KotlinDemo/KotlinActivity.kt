package okhttp.demo.com.okhttpdemo.KotlinDemo

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import okhttp.demo.com.okhttpdemo.R

/**
 * Created by uncle_charlie on 25/2/16.
 */
class KotlinActivity : Activity(), SessionCloseable {

    var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        textView = findViewById(R.id.kotlin_textview) as TextView

        // for extension
        setUI()

        val button = findViewById(R.id.kotlin_button).setOnClickListener { v ->
            when (v) {
                is Button -> println("it's button")
                else -> println("nothing")
            }
        }

        findViewById(R.id.kotlin_button_2).setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@KotlinActivity, "object click", Toast.LENGTH_SHORT).show()
            }
        })

        val fragment = MyFragment.newInstance()
    }

    override fun onStop() {
        super.onStop()
        closeSession()
    }
}

fun KotlinActivity.setUI() {
    this.textView?.text = "Hello Kotlin!"
}
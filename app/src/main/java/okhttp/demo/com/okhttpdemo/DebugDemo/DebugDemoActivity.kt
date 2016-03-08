package okhttp.demo.com.okhttpdemo.DebugDemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import okhttp.demo.com.okhttpdemo.R

class DebugDemoActivity : Activity(), SensorEventListener {
    val ALPHA: Float = 1.0f
    //    var popupFired = false
    lateinit var sensorManager: SensorManager
    var accelerometer: Sensor? = null
    var sensorInited = false
    var poped = false

    var xValue: Float? = null
    var yValue: Float? = null
    var zValue: Float? = null

    val ERROR: Float = 7.0f

    var shakeTextView: TextView? = null
    var xTextView: TextView? = null
    var yTextView: TextView? = null
    var zTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_demo)

        sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        registerSensor(sensorManager, Sensor.TYPE_ACCELEROMETER)

        xTextView = findViewById(R.id.x_text_view) as TextView
        yTextView = findViewById(R.id.y_text_view) as TextView
        zTextView = findViewById(R.id.z_text_view) as TextView
        shakeTextView = findViewById(R.id.shake_text_view) as TextView
    }

    fun registerSensor(sensorManager: SensorManager, sensorType: Int): Boolean {
        var findSensor = false

        if (sensorManager == null) {
            return findSensor
        }

        for (sensor in sensorManager.getSensorList(Sensor.TYPE_ALL)) {
            if (sensor.type != sensorType) {
                continue
            }

            // find specified sensor
            findSensor = true
            // register the sensor
            accelerometer = sensor
            sensorManager.registerListener(this@DebugDemoActivity, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            break
        }

        return findSensor
    }

    fun calculageGravityForce(currentVal: Float, index: Int): Float {
        throw NotImplementedError()
    }

    override fun onSensorChanged(se: SensorEvent?) {
        var x: Float = se?.values?.get(0) ?: 0.0f
        var y: Float = se?.values?.get(1) ?: 0.0f
        var z: Float = se?.values?.get(2) ?: 0.0f

        if (!sensorInited) {
            xValue = x
            yValue = y
            zValue = z

            sensorInited = true
            return
        }

        var diffX = Math.abs(xValue!! - x)
        var diffY = Math.abs(yValue!! - y)
        var diffZ = Math.abs(zValue!! - z)

        diffX = if (diffX!! < ERROR) 0.0f else diffX
        diffY = if (diffY!! < ERROR) 0.0f else diffY
        diffZ = if (diffZ!! < ERROR) 0.0f else diffZ

        xValue = x
        yValue = y
        zValue = z

        // horizental shake dected
        if (diffX > diffY && !poped) {
            shakeTextView?.text = "shaked x: $diffX | y: $diffY"
            var i = Intent(this@DebugDemoActivity, URLListActivity::class.java)
            startActivity(i)
            overridePendingTransition(R.anim.enter_anim, R.anim.still_anim)
            poped = true
            //            finish()
        }

        xTextView?.text = xValue!!.toString()
        yTextView?.text = yValue!!.toString()
        zTextView?.text = zValue!!.toString()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //...
    }

    override fun onResume() {
        super.onResume()
        if (accelerometer != null) {
            sensorManager.registerListener(this@DebugDemoActivity, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        poped = false
    }

    override fun onStop() {
        super.onStop()
        if (accelerometer != null) {
            sensorManager.unregisterListener(this@DebugDemoActivity)
        }
    }
}

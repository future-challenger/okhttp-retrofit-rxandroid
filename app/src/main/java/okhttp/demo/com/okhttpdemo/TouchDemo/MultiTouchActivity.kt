package okhttp.demo.com.okhttpdemo.TouchDemo

import android.app.Activity
import android.os.Bundle
import android.support.v4.view.MotionEventCompat
import android.util.Log
import android.view.MotionEvent
import okhttp.demo.com.okhttpdemo.R

class MultiTouchActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_touch)
    }

    val TAG = MultiTouchActivity::class.java.simpleName
    var mActivePointerId: Int? = null


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // pointer sample
        //        mActivePointerId = event?.getPointerId(0)
        //
        //        // Other touch events...
        //
        //        var pointerIndex = event?.findPointerIndex(mActivePointerId!!)
        //
        //        //
        //        var x = event?.getX(pointerIndex!!)
        //        var y = event?.getY(pointerIndex!!)
        //
        //        return true
        var action = MotionEventCompat.getActionMasked(event!!)

        var index: Int = MotionEventCompat.getActionIndex(event!!)
        var xPos = -1.0f
        var yPos = -1.0f

        Log.d(TAG, "The action is " + actionToSring(action))

        if (event!!.pointerCount > 1) {
            Log.d(TAG, "Mutipletouch event")

            // 坐标系是相对于处理这个事件的View或者Activity的
            xPos = MotionEventCompat.getX(event!!, index)
            yPos = MotionEventCompat.getY(event!!, index)
        } else {
            //单点触控
            Log.d(TAG, "Single touch event")
            xPos = MotionEventCompat.getX(event!!, index)
            yPos = MotionEventCompat.getY(event!!, index)
        }

        return true
    }

    fun actionToSring(action: Int): String {
        when (action) {
            MotionEvent.ACTION_DOWN -> return "Down"
            MotionEvent.ACTION_MOVE -> return "Move"
            MotionEvent.ACTION_POINTER_DOWN -> return "Pointer down"
            MotionEvent.ACTION_UP -> return "UP"
            MotionEvent.ACTION_POINTER_UP -> return "Pointer up"
            MotionEvent.ACTION_OUTSIDE -> return "Outside"
            MotionEvent.ACTION_CANCEL -> return "Cancel"
        }
        return ""
    }
}

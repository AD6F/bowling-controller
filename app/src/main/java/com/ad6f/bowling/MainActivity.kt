package com.ad6f.bowling

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ad6f.bowling.cast.SessionManagerListenerImpl
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    // This is a singleton in the class, all the content of the object are static.
    // This object is associated with the class.
    companion object {
        var loadingCastDialog: Dialog? = null
        var isCastActivated = false
        var isCastLoading = false
    }

    private fun refreshButton() {
        findViewById<View>(R.id.play).isEnabled = isCastActivated
        findViewById<View>(R.id.setting).isEnabled = isCastActivated
    }

    fun setAreButtonVisible(areButtonVisible: Boolean) {
        isCastActivated = areButtonVisible
        refreshButton()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (loadingCastDialog == null) {
            loadingCastDialog = AlertDialog.Builder(this)
                .setTitle("Bowling")
                .setMessage("Connecting to chromecast...")
                .setCancelable(false)
                .create()
        }

        // Lier le boutton au Cast
        CastButtonFactory.setUpMediaRouteButton(applicationContext, findViewById(R.id.cast_media))
        val castContext = CastContext.getSharedInstance(applicationContext)
        val sessionManager = castContext.sessionManager
        val sessionManagerListener = SessionManagerListenerImpl(this)
        sessionManager.addSessionManagerListener(sessionManagerListener, CastSession::class.java)
    }

    var sensorManager: SensorManager? = null
    var sensor: Sensor? = null

    @Throws(JSONException::class)
    fun leave(view: View?) {
        println("Hello World!")
        if (sensorManager == null) {
            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

            val sensorEventListener: SensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    println(event.values[1])
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
            }
            sensorManager?.registerListener(sensorEventListener, sensor, 1)
        }
        println(sensor?.maximumRange)
    }

    fun play(view: View?) {
        this.startActivity(Intent(this, GameSetup::class.java))
    }

    private fun refreshLoadingCastDialog() {
        if (isCastLoading) {
            loadingCastDialog?.show()
        } else {
            loadingCastDialog?.hide()
        }
    }

    fun popupLoadingCast(isLoading: Boolean) {
        isCastLoading = isLoading
        refreshLoadingCastDialog()
    }

    // This is override because, this function is executed after the onCreate function and when we
    // rotate the function onCreate is recalled and this function too, we use this function to put the popup and
    // the buttons to their currentState.
    override fun onStart() {
        super.onStart()
        refreshButton()
        refreshLoadingCastDialog()
    }
}
package io.skaly.example

import android.app.AlertDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.skaly.sdk.Sex
import io.skaly.sdk.Skaly
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var skaly: Skaly
    // Simulate a logged in user
    private val handle = "DemoUser"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        skaly =  Skaly(this, "test")
        setContentView(R.layout.activity_main)
    }

    fun addScale(v: View) {
        skaly.addScale {
            alert("Scale added. Success: $it")
        }
    }

    fun addUser(v: View) {
        if(skaly.hasIdentity(handle)) {
            alert("$handle already exists!")
        }
        else {
            val sex = Sex.Male
            val birthday = Date()
            val day = DateFormat.format("dd", birthday) as String
            val monthNumber = DateFormat.format("MM", birthday) as String
            val year = DateFormat.format("yyyy", birthday) as String
            val birthdayStr = "$day/$monthNumber/$year"
            val length = BigDecimal("175")

            skaly.addIdentity(handle, sex, birthdayStr, length) {
                println("addIdentity callback: $it")
                alert("$handle added. Success: $it")
            }
        }
    }

    fun allowConsent(v: View) {
        skaly.allowAccessToData(skaly.rootKey, handle) {
            alert("$handle allowed consent for dashboard.")
        }
    }

    fun sendReading(v: View) {
        skaly.startReading() { success, scannedHandle, readings ->
            alert("startReading success: $success, scannedHandle: $scannedHandle readings: ${readings.toString()}")
        }
    }

    private fun alert(message: String) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Skaly")
        //set message for alert dialog
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("OK"){ _, _ ->

        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
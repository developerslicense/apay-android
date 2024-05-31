package kz.airbapay.apay_android.ui.pages.card_reader.bl

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.SystemClock
import kz.airbapay.apay_android.ui.pages.card_reader.ScanActivity

internal class ExecutorML(
    private val activity: Activity,
    private val releaseSemaphore: () -> Unit
) : OnScanListener {

    var mSentResponse = false
    var mIsActivityActive = false
    private var firstResultMs: Long = 0
    private var numberResults = HashMap<String, Int>()

    private var errorCorrectionDurationMs: Long = 0

    private val numberResult: String?
        get() {
            // Ugg there has to be a better way
            var result: String? = null
            var maxValue = 0

            for (number in numberResults.keys) {
                var value = 0
                val count = numberResults[number]

                if (count != null) {
                    value = count
                }

                if (value > maxValue) {
                    result = number
                    maxValue = value
                }
            }
            return result
        }

    override fun onFatalError() {
        val intent = Intent()
        intent.putExtra(ScanActivity.RESULT_FATAL_ERROR, true)
        activity.setResult(Activity.RESULT_CANCELED, intent)
        activity.finish()
    }

    override fun onPrediction(
        number: String?, bitmap: Bitmap?,
        digitBoxes: List<DetectedBox?>?
    ) {
        if (!mSentResponse && mIsActivityActive) {
            if (number != null && firstResultMs == 0L) {
                firstResultMs = SystemClock.uptimeMillis()
            }
            number?.let { incrementNumber(it) }
            val duration = SystemClock.uptimeMillis() - firstResultMs
            if (firstResultMs != 0L && duration >= errorCorrectionDurationMs) {
                mSentResponse = true
                val numberResult = numberResult
                onCardScanned(numberResult)
            }
        }

        releaseSemaphore()
    }

    fun onResume() {
        mIsActivityActive = true
        firstResultMs = 0
        numberResults = HashMap()
        mSentResponse = false
    }

    private fun onCardScanned(numberResult: String?) {
        println("$numberResult")
        val intent = Intent()
        intent.putExtra(ScanActivity.RESULT_CARD_NUMBER, numberResult)
        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }

    private fun incrementNumber(number: String) {
        var currentValue = numberResults[number]
        if (currentValue == null) {
            currentValue = 0
        }
        numberResults[number] = currentValue + 1
    }

}
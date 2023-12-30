package app.android.gallery.util

import android.text.format.DateFormat
import java.util.Calendar

fun Long.getDate(
    format: CharSequence = "EEE, MMMM d",
): String {
    val mediaDate = Calendar.getInstance()
    mediaDate.timeInMillis = this * 1000L
    return DateFormat.format(format, mediaDate).toString()
}
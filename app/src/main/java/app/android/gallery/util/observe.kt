package app.android.gallery.util

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun ContentResolver.observe(uri: Uri) = callbackFlow {
  val scope = CoroutineScope(Dispatchers.IO)
  val observer = object : ContentObserver(null) {
      override fun onChange(selfChange: Boolean) {
          trySend(selfChange)
      }
  }
  registerContentObserver(uri, true, observer)
  // trigger first.
  trySend(false)
  awaitClose {
      unregisterContentObserver(observer)
  }
}

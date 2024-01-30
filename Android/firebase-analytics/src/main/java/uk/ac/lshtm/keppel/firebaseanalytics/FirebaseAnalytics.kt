package uk.ac.lshtm.keppel.firebaseanalytics

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import uk.ac.lshtm.keppel.core.Analytics

class FirebaseAnalytics(application: Application) : Analytics {

    private val instance = FirebaseAnalytics.getInstance(application)

    override fun log(event: String) {
        instance.logEvent(event, null)
    }
}

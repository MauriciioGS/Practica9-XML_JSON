package mx.mauriciogs.xml_json

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyAPI constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleyAPI? = null
        fun getInstance (context: Context) = INSTANCE?: synchronized(this) {
            INSTANCE?: VolleyAPI(context).also { INSTANCE = it }
        }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }

    fun<T> add(req: Request<T>) {
        requestQueue.add(req)
    }
}
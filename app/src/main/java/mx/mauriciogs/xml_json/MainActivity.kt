package mx.mauriciogs.xml_json

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import mx.mauriciogs.xml_json.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var volleyAPI: VolleyAPI
    private lateinit var url: String
    private val ipPuerto =  "192.168.0.236:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volleyAPI = VolleyAPI(this)

        binding.search.setOnClickListener {
            binding.outText.text = ""

            url = "https://www.google.com/search?q=" + URLEncoder.encode(
                binding.searchText.text.toString(), "UTF-8"
            )

            buscar()
        }

        binding.btnResXml.setOnClickListener {
            studentXml()
        }

        binding.btnResJson.setOnClickListener {
            studentJSON()
        }
        binding.btnResJsonId.setOnClickListener {
            studentID()
        }
        binding.btnJsonPost.setOnClickListener {
            studentAdd()
        }
        binding.btnDelete.setOnClickListener {
            studentDelete()
        }
    }

    private fun studentDelete() {
        val urlJson = "http://$ipPuerto/borrarestudiante/${binding.searchText.text}"
        var cadena = ""
        val jsonRequest = object : JsonArrayRequest(
            urlJson,
            Listener { response ->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    val materias = estudiante.getJSONArray("materias")
                    cadena = "${estudiante.get("cuenta")}<"
                    (0 until materias.length()).forEach {
                        val datos = materias.getJSONObject(it)
                        cadena += "${datos.get("nombre")}**${datos.get("creditos")}\n"
                    }
                    cadena += ">\n"
                }
                binding.outText.text = cadena
            },
            ErrorListener { responseError ->
                binding.outText.text = "No se encuentra la información"
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return header
            }

            override fun getMethod(): Int {
                return Method.DELETE
            }
        }

        volleyAPI.add(jsonRequest)
    }

    private fun studentAdd() {
        val urlJson = "http://$ipPuerto/agregarestudiante"
        var cadena = ""
        val jsonRequest = object : JsonArrayRequest(
            urlJson,
            Listener { response ->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    val materias = estudiante.getJSONArray("materias")
                    cadena = "${estudiante.get("cuenta")}<"
                    (0 until materias.length()).forEach {
                        val datos = materias.getJSONObject(it)
                        cadena += "${datos.get("nombre")}**${datos.get("creditos")}\n"
                    }
                    cadena += ">\n"
                }
                binding.outText.text = cadena
            },
            ErrorListener { responseError ->
                binding.outText.text = "No se encuentra la información"
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return header
            }

            override fun getBody(): ByteArray {
                val estudiante = JSONObject()
                estudiante.put("cuenta", "A000")
                estudiante.put("nombre", "Android")
                estudiante.put("edad", "23")
                val materias = JSONArray()
                val itemMaterias = JSONObject()
                itemMaterias.put("id", "1")
                itemMaterias.put("nombre", "Nueva materia")
                itemMaterias.put("creditos", "100")
                materias.put(itemMaterias)
                estudiante.put("materias", materias)
                return estudiante.toString().toByteArray(charset = Charsets.UTF_8)
            }

            override fun getMethod(): Int {
                return Method.POST
            }
        }

        volleyAPI.add(jsonRequest)
    }

    private fun studentID() {
        val urlJSON = "http://" + ipPuerto + "/id/" + binding.searchText.text.toString()
        val jsonRequest = object : JsonObjectRequest(
            Method.GET,
            urlJSON,
            null,
            Listener { response ->
                binding.outText.text = response.get("cuenta")
                    .toString() + "----" + response.get("nombre").toString() + "\n"
            },
            ErrorListener {
                binding.outText.text = "No se encuentra la información ${it.localizedMessage}"
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return headers
            }
        }
        volleyAPI.add(jsonRequest)

    }

    private fun studentJSON() {
        val urlJson = "http://$ipPuerto/estudiantesJSON"
        var cadena = ""
        val jsonRequest = object : JsonArrayRequest(
            urlJson,
            Listener { response ->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    val materias = estudiante.getJSONArray("materias")
                    cadena = "${estudiante.get("cuenta")}<"
                    (0 until materias.length()).forEach {
                        val datos = materias.getJSONObject(it)
                        cadena += "${datos.get("nombre")}**${datos.get("creditos")}\n"
                    }
                    cadena += ">\n"
                }
                binding.outText.text = cadena
            },
            ErrorListener { responseError ->
                binding.outText.text = "No se encuentra la información"
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return header
            }
        }

        volleyAPI.add(jsonRequest)
    }

    private fun studentXml() {
        val urlXML = "http://$ipPuerto/estudiantesXML"
        val stringRequest = object : StringRequest(
            Method.GET, urlXML,
            Listener<String> { response ->
                binding.outText.text = response
            },
            ErrorListener { responseError ->
                binding.outText.text = "No se encuentra la información: ${responseError.localizedMessage}"
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return header
            }
        }

        volleyAPI.add(stringRequest)
    }

    private fun buscar() {
        val stringRequest = object: StringRequest(
            Method.GET, url,
            Listener { response ->
                binding.outText.text = response
            },
            ErrorListener {
                binding.outText.text = "No se encuentra la información"
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return header
            }
        }

        volleyAPI.add(stringRequest)
    }


}
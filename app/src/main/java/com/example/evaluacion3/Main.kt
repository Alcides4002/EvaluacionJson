package com.example.evaluacion3

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.evaluacion3.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class MainActivity : AppCompatActivity() {

    private lateinit var Binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(Binding.root)

        obtenerDatos(Binding.textView)

    }


    interface ApiService {
        @GET("Api/mostrar.php")
        suspend fun obtenerDatos(): ResponseBody
    }

    object RetrofitClient {
        private const val BASE_URL = "http://10.0.2.2:80/"
        private val client = OkHttpClient.Builder().build()


        val apiService: ApiService = Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }


    fun obtenerDatos(textView: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            var response: ResponseBody? = null
            try {
                response = RetrofitClient.apiService.obtenerDatos()
                val jsonResponse = response.string() // Extrae la respuesta JSON como cadena de texto
                val jsonArray = JSONArray(jsonResponse) // Crea un array JSON a partir de la cadena de texto
                val datos = mutableListOf<String>()
                for (i in 0 until jsonArray.length()) {
                    val objeto = jsonArray.getJSONObject(i)
                    val idC = objeto.getString("idC").toInt()
                    val nombres = objeto.getString("nombres")
                    val apellidos = objeto.getString("apellidos")
                    val fechaNac = objeto.getString("fechaNac")
                    val titulo = objeto.getString("titulo")
                    val email = objeto.getString("email")
                    val facultad = objeto.getString("facultad")
                    val textoMostrado = "Id: $idC\nNombre: $nombres $apellidos\nFecha de Nacimiento: $fechaNac\nTítulo: $titulo\nEmail: $email\nFacultad: $facultad"
                    datos.add(textoMostrado)
                }
                val datosFormatted = datos.joinToString("\n\n") // Convierte los datos a una cadena de texto con formato legible
                withContext(Dispatchers.Main) {
                    textView.text = datosFormatted // Muestra la respuesta en el TextView
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error al realizar la solicitud HTTP", e)
                // Maneja el error si es necesario
            } finally {
                response?.close()
            }
        }


    }
}
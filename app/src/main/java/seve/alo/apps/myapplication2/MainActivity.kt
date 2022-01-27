package seve.alo.apps.myapplication2


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Forzar icono en el Action Bar
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_launcher)

        longToast(getString(R.string.main_longtoast))

        val buttonCapturar = findViewById<Button>(R.id.buttonCalcular)
        val editText = findViewById<EditText>(R.id.editTextNumber)
        val btnAnko = findViewById<Button>(R.id.btnAnko)
        val btnLista = findViewById<Button>(R.id.btnLista)
        val btnProgreso = findViewById<Button>(R.id.btnProgreso)

        buttonCapturar.text = "Calcula tu edad"

        buttonCapturar.setOnClickListener {
            val anoNacimiento : Int = editText.text.toString().toInt()
            val anoActual = Calendar.getInstance().get(Calendar.YEAR)
            val miEdad = anoActual - anoNacimiento

            startActivity<SecondActivity>("edad" to miEdad)
        }

        btnAnko.onClick {
            alert(getString(R.string.main_alert_mensaje), getString(R.string.main_alert_titulo)){
                yesButton { longToast(getString(R.string.main_alert_btn_positivo)) }
                noButton { longToast(getString(R.string.main_alert_btn_negativo)) }
            }.show()
        }

        btnLista.onClick {
            val paises = listOf("MEX", "ESP", "ARG", "BOL", "CHL", "COL")
            selector(getString(R.string.main_alert_lista), paises, { dialogInterface, i -> longToast("Genial!, entonces vives en ${paises[i]}, cierto?") })
        }

        btnProgreso.onClick {
            val dialog = progressDialog(message = "Por favor espera un momento...", title = "Cargando datos")
        }


    }
}
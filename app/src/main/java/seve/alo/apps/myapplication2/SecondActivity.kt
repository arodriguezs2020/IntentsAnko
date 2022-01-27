package seve.alo.apps.myapplication2


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.startActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Flecha para regresar al Activity
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val textView = findViewById<TextView>(R.id.textViewIntent)
        val btnThirdActivity = findViewById<Button>(R.id.btnThirdActivity)

        val bundle = intent.extras

        val edad = bundle?.getInt("edad")
        textView.text = edad.toString()

        btnThirdActivity.setOnClickListener{
            startActivity<ThirdActivity>()
        }
    }
}
package seve.alo.apps.myapplication2

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ThirdActivity : AppCompatActivity() {
    private val PHONE_CODE = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        // Flecha para regresar al Activity
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val imageButtonPhone = findViewById<ImageButton>(R.id.imageButtonPhone)
        val imageButtonWeb = findViewById<ImageButton>(R.id.imageButtonWeb)
        val imageButtonCamera = findViewById<ImageButton>(R.id.imageButtonCamera)
        val buttonEmail = findViewById<Button>(R.id.buttonEmail)
        val buttonContactPhone = findViewById<Button>(R.id.buttonContactPhone)
        val editTextPhone = findViewById<EditText>(R.id.editTextPhone)
        val editTextWeb = findViewById<EditText>(R.id.editTextWeb)

        // --- Botón para la llamada --- //
        imageButtonPhone!!.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onClick(v: View?) {
                val phoneNumber = editTextPhone!!.text.toString()
                if (phoneNumber.isNotEmpty()) {
                    // --- Comprobar versión actual VS version MarshMallow --- //
                    doFromSdk (Build.VERSION_CODES.LOLLIPOP) {
                        // --- Comprobar el permiso --- //
                        if (checkedPermission(android.Manifest.permission.CALL_PHONE)){
                            // --- Si está el permiso en el manifest ha aceptado --- //
                            //val intentAceptado = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                            if (ActivityCompat.checkSelfPermission(this@ThirdActivity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                                return
                            }
                            makeCall(phoneNumber)
                        }else{ // --- Preguntarle por el permiso --- //
                            if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CALL_PHONE)){
                                requestPermissions(arrayOf(android.Manifest.permission.CALL_PHONE), PHONE_CODE)
                            } else {
                                /* --- Si ya denegó el permiso y quiere acceder nuevamente
                                        lo dirigimos a los Ajustes para que habilite el permiso */
                                longToast("Por favor habilita el permiso correspondiente para continuar")
                                val intentSetting = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intentSetting.addCategory(Intent.CATEGORY_DEFAULT)
                                intentSetting.data = Uri.parse("package:$packageName")
                                intentSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intentSetting.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                intentSetting.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                startActivity(intentSetting)
                            }
                        }
                    }
                    doIfSdk(Build.VERSION_CODES.LOLLIPOP){
                        versionAntigua(phoneNumber)
                    }
                } else {
                    longToast("Debes marcar un número, intenta nuevamente")
                }
            }
            fun versionAntigua(phoneNumber: String) {
                //val intentCall = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                if (checkedPermission(android.Manifest.permission.CALL_PHONE)) {
                    if (ActivityCompat.checkSelfPermission(this@ThirdActivity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        return
                    }
                    makeCall(phoneNumber)
                }
            }
        })

        // --- Botón para la Web --- //
        imageButtonWeb!!.onClick {
            val url = editTextWeb!!.text.toString()
            browse("https://$url")
        }
        // --- Botón para el Email --- //
        buttonEmail!!.onClick{
            val email = "algunemail@gmail.com"
            email(email, "Titulo del mail", "Hola este es un Anko Email")
        }
        // --- Botón para la llamada sin permisos --- //
        buttonContactPhone!!.onClick {
           makeCall("99991234671")
        }
        // --- Botón para la cámara --- //
        imageButtonCamera!!.onClick {
            val intentCamera = Intent("android.media.action.IMAGE_CAPTURE")
            startActivity(intentCamera)
        }
    }

    // Implementar menú
    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // Implementar funcion para cuando selecciona una de las opciones del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuContactos -> {
                val intentContact = Intent(Intent.ACTION_VIEW, Uri.parse("content://contact/people"))
                startActivity(intentContact)
            }
            R.id.menuCompartir -> { share("Anko está genial!", "Anko for Android") }
            R.id.menuMensaje -> { sendSMS("9991223198", "Esto es un Anko SMS") }
        }
        return super.onOptionsItemSelected(item)
    }

    // --- Método asíncrono para comprobar permisos --- //
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val editTextPhone = findViewById<EditText>(R.id.editTextPhone)
        when (requestCode) {
            // Caso de uso
            PHONE_CODE ->{
                val permiso = permissions[0]
                val resultado = grantResults[0]

                if (permiso == android.Manifest.permission.CALL_PHONE){
                    // --- Comprobar si ha sido aceptado o denegado la peticion de permiso --- //
                    if (resultado == PackageManager.PERMISSION_GRANTED){
                        // --- Concedió su permiso --- //
                        val phoneNumber = editTextPhone!!.text.toString()
                        val intentCall = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                        // --- Debemos verificar que exista el permiso en el manifest explicitamente --- //
                        // --- ya que el usuario puede rechazar esta petición de permiso --- //
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            return
                        }
                        startActivity(intentCall)
                    }else{
                        // --- Denegó el permiso --- //
                        Toast.makeText(this, "Has denegado el permiso", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else ->super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    // --- Verificamos que el permiso esté aceptado --- //
    fun checkedPermission(permission: String): Boolean {
        val result = this.checkCallingOrSelfPermission(permission)
        return result == PackageManager.PERMISSION_GRANTED
    }
}
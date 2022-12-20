package com.example.fixofinal

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.fixofinal.api.MySingleton
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    private var id_centro = ""
    private var Limpeza = ""
    private var idSala = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main)



        val nome_sala = findViewById<TextView>(R.id.nome_sala)
        val lotacao = findViewById<TextView>(R.id.lotacao)
        val estado = findViewById<TextView>(R.id.estado)
        val percentagem = findViewById<TextView>(R.id.percentagem)
        val lotacao1 = findViewById<TextView>(R.id.lotacao1)
        val Limpeza1 = findViewById<TextView>(R.id.Limpeza1)




        fun updateTimer() {
            runOnUiThread {
                var simpleDate      = SimpleDateFormat("HH:mm")
                simpleDate.timeZone = TimeZone.getTimeZone("Europe/Lisbon")
                val currentDate     = simpleDate.format(Date())
                var tvHoraAtual= (currentDate.toString())

            }
        }

        idSala = intent.getIntExtra("id_sala",0)
        Log.d("O valor da sala é x4", idSala.toString())

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                updateTimer()
                fetchdata(nome_sala,lotacao,estado,percentagem,lotacao1,Limpeza1,idSala)

            }
        }, 0, 20000)







        preferences = getSharedPreferences("sharedpref3", Context.MODE_PRIVATE)
        id_centro = preferences.getString("Id_centro","").toString()
        Log.d("O valor da id_centro é", id_centro)

        Limpeza = Limpeza1.text.toString()


        setQRCode()

    }
    private fun fetchdata(nome_sala: TextView, lotacao: TextView, estado: TextView, percentagem: TextView,lotacao1: TextView, Limpeza1: TextView, idSala: Int) {

        Log.e("O VALOR DO ID CENTRO É", idSala.toString())
        val url = "https://backend-pint-final.herokuapp.com/api/mobile/sala/$idSala"
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try{
                var jsonArray = JSONArray()
                jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val JsonResp = jsonArray.getJSONObject(i)

                    nome_sala.text = JsonResp.getString("nome_sala")
                    lotacao.text = JsonResp.getString("lotacao")
                    estado.text = JsonResp.getString("estado")
                    percentagem.text = JsonResp.getString("limite")
                    Limpeza1.text = JsonResp.getString("limpeza")
                    Limpeza = Limpeza1.text.toString()
                    val x = "true"
                        verificar(x)
                    if ( Limpeza == x)
                    {
                        val bu = findViewById<Button>(R.id.butonz)
                        bu.setOnClickListener {
                            updatesala(idSala)
                        }
                    }
                    Log.d("O valor dá limpezax2",Limpeza)
                    lotacao1.text = JsonResp.getString("maximo")

                }


            }catch (e: NumberFormatException)
            {
                e.printStackTrace()
            }
        },
            { error ->
                Log.d("API", "error => $error")
                Toast.makeText(applicationContext,"ERRO: $error", Toast.LENGTH_SHORT).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(request)
    }

    private fun verificar(x: String) {

        Log.d("O valor dá limpezax3",Limpeza)


        if(Limpeza=="true")
        {
            val vall = findViewById<ImageView>(R.id.verificar)
            vall.drawable.constantState == resources.getDrawable(R.drawable.warning).constantState
            vall.setImageResource(R.drawable.warning)
        }
        else{
            val vall = findViewById<ImageView>(R.id.verificar)
            vall.drawable.constantState == resources.getDrawable(R.drawable.warning).constantState
            vall.setImageResource(R.drawable.checked)

        }

    }

    private fun updatesala(idSala: Int) {

        val jsonObject = JSONObject()

        val vall = false
        jsonObject.put("limpeza", vall)
        val url ="https://backend-pint-final.herokuapp.com/api/mobile/sala-limpa/${idSala}"
        val req = JsonObjectRequest(Request.Method.PUT, url,jsonObject, { response ->
            // response
            if (response.has("dados")) {
                Toast.makeText(applicationContext,"Limpeza realizada", Toast.LENGTH_SHORT).show()

            } else {
                var strErro = response.getJSONObject("data").getString("name")
                Toast.makeText(applicationContext, "ERRO: $strErro", Toast.LENGTH_SHORT).show()
            }

        },
            { error -> Log.d("API", "error => $error")
                Toast.makeText(applicationContext, "ERRO: $error", Toast.LENGTH_SHORT).show()
            }
        )

        MySingleton.getInstance(this).addToRequestQueue(req)

    }


    private fun setQRCode(){

        idSala = intent.getIntExtra("id_sala",0)
        Log.d("O valor da sala é x5", idSala.toString())

        var ivQRcode: ImageView = findViewById(R.id.iv_qrCode)

        val writer = QRCodeWriter()
        try{
            val bitMatrix   = writer.encode(idSala.toString(), BarcodeFormat.QR_CODE, 1024, 1024)
            val bmp         = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565)
            for (x in 0 until bitMatrix.width){
                for (y in 0 until bitMatrix.height){
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            ivQRcode.setImageBitmap(bmp)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


}
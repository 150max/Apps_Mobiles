package com.example.fixofinal

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.fixofinal.adapter.SalaListAdapter
import com.example.fixofinal.adapter.SalasItemClicked
import com.example.fixofinal.api.MySingleton
import com.example.fixofinal.model.Centro
import org.json.JSONObject

class ListaCentros : AppCompatActivity(), SalasItemClicked {
    private lateinit var mAdapter: SalaListAdapter
    lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_centros)

        val rvSalas = findViewById<RecyclerView>(R.id.recyclerViewsalas)
        rvSalas.layoutManager = LinearLayoutManager(this)
        mAdapter = SalaListAdapter(this)

        rvSalas.adapter = mAdapter

        val url ="https://backend-pint-final.herokuapp.com/api/mobile/centros"
        val request = StringRequest(
            Request.Method.GET, url,
            {response ->
                val data = response.toString()
                val jobj = JSONObject(data)
                val jarray = jobj.getJSONArray("data")
                val list : MutableList<Centro> = ArrayList()
                for (i in 0 until jarray.length()){
                    val jobj2 = jarray.getJSONObject(i)
                    val id_centro = jobj2.getString("id_centro")
                    val nome_centro = jobj2.getString("nome_centro")
                    val morada = jobj2.getString("morada")
                    val estado = jobj2.getString("estado")
                    val m= Centro(id_centro,nome_centro,morada,estado)
                    list.add(m)
                }
                val spinner = findViewById<Spinner>(R.id.spinner)
                val adapter = SpinnerAdapter(this,list)
                spinner.adapter= adapter
                spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val txt1 = view?.findViewById<TextView>(R.id.id_centro)
                        val Id_centro: String = txt1?.text.toString()

                        sharedPreferences = getSharedPreferences("sharedpref3", MODE_PRIVATE)
                        val editor:SharedPreferences.Editor=sharedPreferences.edit()
                        editor.putString("Id_centro",Id_centro)
                        editor.apply()
                        Log.d("O valor do centro é", Id_centro)



                        fetchdata(Id_centro)


                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }

            },
            {error -> val er = error.toString()
                Log.e("error",er)
            })

        MySingleton.getInstance(this).addToRequestQueue(request)

    }

    private fun fetchdata(Id_centro: String) {

        Log.e("O VALOR DO ID CENTRO É",Id_centro)
        val url = "https://backend-pint-final.herokuapp.com/api/mobile/salas-centro/$Id_centro"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, {
            try {
                val salasJsonArray = it.getJSONArray("novo")
                val salasArray = ArrayList<Sala>()
                for(i in 0 until salasJsonArray.length()) {
                    val salasJsonObject = salasJsonArray.getJSONObject(i)
                    val salas = Sala(
                        salasJsonObject.getInt("id_sala"),
                        salasJsonObject.getString("nome_sala"),
                        salasJsonObject.getString("lotacao"),
                        salasJsonObject.getString("estado"),
                        salasJsonObject.getString("limite"),
                        salasJsonObject.getString("maximo"),
                    )
                    Log.d("API", "testReservas => $salas")
                    salasArray.add(salas)
                }
                mAdapter.updateSalas(salasArray)
            }catch (e: NumberFormatException)
            {
                var error = it.getJSONObject("error").getString("message")
                Toast.makeText(applicationContext,"ERRO: $error", Toast.LENGTH_SHORT).show()
            }
        },
            { error ->
                Log.d("API", "errorLista => $error")
                Toast.makeText(applicationContext,"ERRO: $error", Toast.LENGTH_SHORT).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: Sala) {
        val idsalaClicked = item.id_sala

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("id_sala",idsalaClicked)
        startActivity(intent)




    }


}
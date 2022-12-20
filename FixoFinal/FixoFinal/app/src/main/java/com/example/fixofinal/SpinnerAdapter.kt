package com.example.fixofinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.fixofinal.model.Centro

class SpinnerAdapter(ctx : Context, list:List<Centro>) : ArrayAdapter<Centro>(ctx,0,list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return myView(position,convertView,parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return myView(position,convertView,parent)
    }

    private fun myView(position: Int, convertView: View?, parent: ViewGroup): View {
        val list = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item,
            parent,
            false
        )
        list.let {
            val txt1 = view.findViewById<TextView>(R.id.id_centro)
            val txt2 = view.findViewById<TextView>(R.id.nome_centro)
            val txt3 = view.findViewById<TextView>(R.id.morada)
            val txt4 = view.findViewById<TextView>(R.id.estado)

            txt1.text= list?.id_centro.toString()
            txt2.text= list?.nome_centro
            txt3.text= list?.morada
            txt4.text= list?.estado

        }

        return view
    }
}
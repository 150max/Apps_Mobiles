package com.example.fixofinal.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fixofinal.R
import com.example.fixofinal.Sala
import com.example.fixofinal.Salas

class SalaListAdapter(private val listener: SalasItemClicked): RecyclerView.Adapter<SalasViewHolder>() {
    private val items : ArrayList<Sala> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_salas, parent, false)
        val viewHolder= SalasViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SalasViewHolder, position: Int) {
        val currentItem = items[position]

        val textid_centro =  currentItem.nome_sala
        val textid_sala =  currentItem.lotacao
        val textdata =  currentItem.estado
        val texthorainicio =  currentItem.limite
        val texthorafim =  currentItem.maximo


        holder.nome_sala.text = textid_centro
        holder.lotacao.text = textid_sala
        holder.estado.text = textdata
        holder.limite.text = texthorainicio
        holder.maximo.text = texthorafim


    }
    fun updateSalas(updateSalas: ArrayList<Sala>){
        items.clear()
        items.addAll(updateSalas)
        notifyDataSetChanged()
    }
}

class SalasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val nome_sala = itemView.findViewById<TextView>(R.id.nome_sala)
    val lotacao = itemView.findViewById<TextView>(R.id.lotacao)
    val estado = itemView.findViewById<TextView>(R.id.estado)
    val limite = itemView.findViewById<TextView>(R.id.limite)
    val maximo = itemView.findViewById<TextView>(R.id.maximo)
}
interface SalasItemClicked {
    fun onItemClicked(item: Sala)
}
package br.digitalhouse.cookbook.ui.dashboard.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.data.dto.ReceitasItem
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(
    private val context: Context,
    private val results: ArrayList<ReceitasItem>,
    private val onItemClicked: (name: String, num: String, image: String, height: String, weight: String, type: String, weaknesses: String, prevevo: String, nextevo: String) -> Unit
) :
    RecyclerView.Adapter<SearchAdapter.HomeHolder>(), Filterable {

    var listFull = ArrayList<ReceitasItem>()

    init {
        listFull = this.results
    }

    private var pEvo = ""
    private var nEvo = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HomeHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_cardrecipes, parent, false)
    )

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.itemView.rootView.setOnClickListener {
//            onItemClicked.invoke(
//                results[position].nomePokemon,
//                results[position].num,
//                results[position].imgPokemon,
//                results[position].alturaPokemon,
//                results[position].pesoPokemon,
//                results[position].typePokemon[0],
//                results[position].pontosFracosPokemon[0],
//                pEvo,
//                nEvo
//            )
        }

        holder.nome.text = results[position].nome
    }

    override fun getItemCount(): Int = results.size

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter(){

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val resultList = ArrayList<ReceitasItem>()
            val charSearch = constraint.toString()
            if (charSearch.isEmpty()) {
                listFull = results
            } else {

                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }

                results.filter {
                    (it.nome.toLowerCase().contains(filterPattern))

                }
                    .forEach { resultList.add(it) }
                listFull = resultList

            }
            val filterResults = FilterResults()
            filterResults.values = listFull
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            listFull = results?.values as ArrayList<ReceitasItem>
            Log.d("RETORNO", Gson().toJson(results))
            notifyDataSetChanged()
        }

    }

    inner class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.textRecipe)
    }
}
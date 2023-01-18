package br.digitalhouse.cookbook.ui.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.data.dto.RecipesFireBaseDataClass

class FavoritesAdapter(
    private val context: Context,
    private val results: MutableList<RecipesFireBaseDataClass> = mutableListOf(),
    private val onItemClicked: (name: String, num: String, image: String, height: String, weight: String, type: String, weaknesses: String, prevevo: String, nextevo: String) -> Unit
) :
    RecyclerView.Adapter<FavoritesAdapter.HomeHolder>() {

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
//            )
        }

        holder.nome.text = results[position].nameP
    }

    override fun getItemCount(): Int = results.size

    inner class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.textRecipe)
    }
}
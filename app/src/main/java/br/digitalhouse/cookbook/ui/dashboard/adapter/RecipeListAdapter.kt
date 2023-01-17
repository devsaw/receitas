package br.digitalhouse.cookbook.ui.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.data.dto.Receitas
import br.digitalhouse.cookbook.data.dto.ReceitasItem

class RecipeListAdapter(
    private val context: Context,
    private val results: MutableList<ReceitasItem> = mutableListOf(),
    private val onItemClicked: (name: String, num: String, image: String, height: String, weight: String, type: String, weaknesses: String, prevevo: String, nextevo: String) -> Unit
) :
    RecyclerView.Adapter<RecipeListAdapter.HomeHolder>() {

    private var pEvo = ""
    private var nEvo = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HomeHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_cardrecipes, parent, false)
    )

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.itemView.rootView.setOnClickListener {
//            if (results[position].prevEvolution != null){
//                for (iEvo in results[position].prevEvolution!!){
//                    if (iEvo.namePrevEvolution == results[position].nomePokemon){
//                        pEvo = ""
//                    } else{
//                        pEvo = iEvo.namePrevEvolution
//                    }
//                }
//            }else{
//                pEvo = ""
//            }
//
//            if (results[position].nextEvolution != null){
//                if (results[position].nextEvolution!![0].nameNextEvolution == results[position].nomePokemon){
//                        nEvo = ""
//                    }else{
//                        nEvo = results[position].nextEvolution!![0].nameNextEvolution
//                    }
//            }else{
//                nEvo = ""
//            }

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

    fun update(recipesObject: Receitas) {
        this.results.clear()
        this.results.addAll(recipesObject.recipes)
        this.notifyDataSetChanged()
    }

    inner class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.textViewRecipe)
    }
}
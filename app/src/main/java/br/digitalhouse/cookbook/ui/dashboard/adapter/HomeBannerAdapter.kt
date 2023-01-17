package br.digitalhouse.cookbook.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.digitalhouse.alugueapp.mockkdata.BannerDataClass
import br.digitalhouse.cookbook.R

class HomeBannerAdapter(private val listData: List<BannerDataClass>):
    RecyclerView.Adapter<HomeBannerAdapter.DataHolder>() {
    private lateinit var mlistener: ItemClickListener

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: ItemClickListener) {
        mlistener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_highlights, parent, false)
        return DataHolder(layoutInflater, mlistener)

    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        holder.images.setImageResource(listData[position].image)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class DataHolder(itemView: View, listener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val images = itemView.findViewById<ImageView>(R.id.imageViewRecipe)
        val text = itemView.findViewById<TextView>(R.id.textViewRecipe)

        init {
            itemView.setOnClickListener { listener.onItemClick(adapterPosition) }
        }

    }

}
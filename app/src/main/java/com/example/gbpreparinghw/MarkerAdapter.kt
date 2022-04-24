package com.example.gbpreparinghw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gbpreparinghw.databinding.ItemMarkersRecyclerBinding
import com.google.android.gms.maps.model.Marker

class MarkerAdapter(private var data: List<Marker>) : RecyclerView.Adapter<MarkerAdapter.ViewHolder>() {

    fun setData(data: List<Marker>) {
        this.data = data
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerAdapter.ViewHolder {
        return ViewHolder(ItemMarkersRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MarkerAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
    
    
    inner class ViewHolder(val binding: ItemMarkersRecyclerBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Marker) {
            binding.markerTitleEditText.text = data.title
            binding.annotationMarkerEditText.text = data.snippet
        }

    }
}
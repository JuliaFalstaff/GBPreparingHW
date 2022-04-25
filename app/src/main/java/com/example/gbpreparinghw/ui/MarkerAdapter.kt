package com.example.gbpreparinghw.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gbpreparinghw.databinding.ItemMarkersRecyclerBinding
import com.google.android.gms.maps.model.MarkerOptions

class MarkerAdapter(
    private var data: MutableList<MarkerOptions>,
    private val listener: OnListItemClickListener
) :
    RecyclerView.Adapter<MarkerAdapter.ViewHolder>() {

    fun setData(data: MutableList<MarkerOptions>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMarkersRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(val binding: ItemMarkersRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: MarkerOptions) {
            binding.markerTitleTextView.text = data.title
            binding.annotationMarkerTextView.text = data.snippet
            binding.latTextView.text = "Lat: ${data.position.latitude}"
            binding.lonTextView.text = "Lon: ${data.position.longitude}"
            itemView.setOnClickListener {
                listener.onClickTitle(data)
            }
        }
    }

    interface OnListItemClickListener {
        fun onClickTitle(data: MarkerOptions)
    }
}
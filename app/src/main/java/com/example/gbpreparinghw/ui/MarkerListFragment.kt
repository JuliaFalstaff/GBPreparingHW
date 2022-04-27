package com.example.gbpreparinghw.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gbpreparinghw.R
import com.example.gbpreparinghw.databinding.FragmentMarkerRecyclerviewBinding
import com.example.gbpreparinghw.model.repo.AppState
import com.example.gbpreparinghw.utils.toEntity
import com.example.gbpreparinghw.viewmodel.MarkersListViewModel
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class MarkerListFragment : Fragment() {

    companion object {
        fun newInstance() = MarkerListFragment()
    }

    private var _binding: FragmentMarkerRecyclerviewBinding? = null
    private val binding get() = _binding!!
    private val adapter: MarkerAdapter? = null
    private val markersViewModel: MarkersListViewModel by viewModel()

    private val listener: MarkerAdapter.OnListItemClickListener =
        object : MarkerAdapter.OnListItemClickListener {
            override fun onClickTitle(data: MarkerOptions) {
                openDialog(data)
            }
        }

    private fun openDialog(data: MarkerOptions) {
        val v = activity?.layoutInflater?.inflate(R.layout.update_dialog, null)

        val editTitle = v?.findViewById<EditText>(R.id.edit_name)
        val editSnippet = v?.findViewById<EditText>(R.id.edit_snippet)

        editTitle?.setText(data.title)
        editSnippet?.setText(data.snippet)

        val builder = AlertDialog.Builder(requireActivity())
            .setView(v)
            .setPositiveButton(getString(R.string.save)) { p0, p1 ->
                val title = editTitle?.text.toString()
                val snippet = editSnippet?.text.toString()
                data.snippet(snippet).title(title)
                markersViewModel.updateMarker(data.toEntity())
                adapter?.notifyDataSetChanged()
            }
            .setCancelable(true)
        builder.create().show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarkerRecyclerviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.markersRecyclerView.adapter = adapter
        markersViewModel.getData().observe(viewLifecycleOwner, Observer { renderData(it) })
        markersViewModel.getMarkersData()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val data = appState.markersData
                binding.markersRecyclerView.adapter = MarkerAdapter(data, listener)
                binding.markersRecyclerView.setHasFixedSize(true)
                binding.markersRecyclerView.layoutManager = LinearLayoutManager(context)
                adapter?.setData(data)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
package com.anthony.neighbors.fragments

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthony.neighbors.R
import com.anthony.neighbors.adapters.ListNeighborHandler
import com.anthony.neighbors.adapters.ListNeighborsAdapter
import com.anthony.neighbors.data.NeighborRepository
import com.anthony.neighbors.models.Neighbor

class ListNeighborsFragment : Fragment(), ListNeighborHandler {
    /**
     * Fonction permettant de définir une vue à attacher à un fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_neighbors_fragment, container, false)
        recyclerView = view.findViewById(R.id.neighbors_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh()
    }

    private fun refresh() {
        NeighborRepository.getInstance().getNeighbours().observe(viewLifecycleOwner) {
            var adapter = ListNeighborsAdapter(this, it)
            recyclerView.adapter = adapter
        }
    }

    fun reloadDisplay() {
        NeighborRepository.getInstance().getNeighbours().observe(
            viewLifecycleOwner,
            Observer {
                val adapter = ListNeighborsAdapter(this, it)
                recyclerView.adapter = adapter
            }
        )
    }

    private lateinit var recyclerView: RecyclerView
    override fun onDeleteNeighbor(neighbor: Neighbor) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Alerte")
                setMessage("Voulez vraiment supprimer le voisin ?")
                setPositiveButton(
                    "Valider",
                    DialogInterface.OnClickListener { dialog, id ->
                        NeighborRepository.getInstance().deleteNeighbor(neighbor)
                        reloadDisplay()
                    }
                )
                setNegativeButton(
                    "Annuler",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    }
                )
            }

            // Create the AlertDialog
            builder.create()
            builder.show()
        }
    }

    override fun onAddFavorite(neighbor: Neighbor) {
        NeighborRepository.getInstance().addFavNeighbor(neighbor)
        reloadDisplay()
    }

    override fun openWebsite(neighbor: Neighbor) {
        var url = Uri.parse("https://${neighbor.webSite}")
        val i = Intent(Intent.ACTION_VIEW, url)
        startActivity(i)
    }
}

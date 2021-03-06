package com.anthony.neighbors.data

import androidx.lifecycle.LiveData
import com.anthony.neighbors.data.service.DummyNeighborApiService
import com.anthony.neighbors.data.service.NeighborApiService
import com.anthony.neighbors.models.Neighbor

class NeighborRepository {
    private val apiService: NeighborApiService

    init {
        apiService = DummyNeighborApiService()
    }

    fun getNeighbours(): LiveData<List<Neighbor>> = apiService.neighbours
    fun deleteNeighbor(neighbor: Neighbor) = apiService.deleteNeighbour(neighbor)
    fun addFavNeighbor(neighbor: Neighbor) = apiService.updateFavoriteStatus(neighbor)
    fun createNeighbor(neighbor: Neighbor) = apiService.createNeighbour(neighbor)

    companion object {
        private var instance: NeighborRepository? = null
        fun getInstance(): NeighborRepository {
            if (instance == null) {
                instance = NeighborRepository()
            }
            return instance!!
        }
    }
}
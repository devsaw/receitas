package br.digitalhouse.cookbook.ui.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import br.digitalhouse.cookbook.data.dto.JsonRepository
import br.digitalhouse.cookbook.data.dto.Receitas

class RecipesViewModel: ViewModel() {
    val recipesRepository = JsonRepository()

    suspend fun fetchRecipes(): Receitas = recipesRepository.fetchReceitas()
}
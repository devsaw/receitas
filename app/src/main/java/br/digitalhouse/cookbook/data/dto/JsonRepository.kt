package br.digitalhouse.cookbook.data.dto

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JsonRepository {
    private val api = receitasApi

    suspend fun fetchReceitas(): Receitas = withContext(Dispatchers.IO){
        api.fetchReceitas()
    }
}
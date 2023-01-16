package br.digitalhouse.cookbook.data.dto

import retrofit2.http.GET

interface JsonInterface {
    @GET("master/afrodite.json")
    suspend fun fetchReceitas(): Receitas
}
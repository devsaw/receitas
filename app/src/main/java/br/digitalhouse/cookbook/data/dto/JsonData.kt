package br.digitalhouse.cookbook.data.dto

import android.os.Parcelable
import br.digitalhouse.cookbook.data.utils.ConfigFirebase
import kotlinx.parcelize.Parcelize

data class Receitas(
    val recipes : ArrayList<ReceitasItem>
)

data class ReceitasItem(
    val _id: Id,
    val nome: String,
    val secao: List<Secao>
)

data class Id(
    val `$oid`: String
)

data class Secao(
    val conteudo: List<String>,
    val nome: String
)

@Parcelize
data class PokemonsDataClass(
    var imageP: String? = null,
    var elementP: String? = null,
    var nameP: String? = null,
    var numP: String? = null,
    var heightP: String? = null,
    var weightP: String? = null,
    var weaknessP: String? = null,
    var prevP: String? = null,
    var nextP: String? = null,
    var idP: String? = null
): Parcelable {
    init {
        this.idP = ConfigFirebase().getDatabase().push().key ?: ""
    }
}
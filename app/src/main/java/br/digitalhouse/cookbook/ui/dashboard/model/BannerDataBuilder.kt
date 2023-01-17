package br.digitalhouse.alugueapp.mockkdata

import android.widget.TextView

class BannerDataBuilder {

    var listaDados = mutableListOf<BannerDataClass>()

    fun add(image: Int, text: String){
        var item = BannerDataClass(image, text)
        listaDados.add(item)
    }

}
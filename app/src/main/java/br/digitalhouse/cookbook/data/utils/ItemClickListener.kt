package br.digitalhouse.cookbook.data.utils

import android.view.View


interface ItemClickListener {

    fun onClickListenerItem(item: Any?){
        //optional body
    }

    fun onClickListenerItemWithType(v: View?, item: Any?, type: Int){
        //optional body
    }

}
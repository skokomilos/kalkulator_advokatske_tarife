package com.eklitstudio.advokatkotilin.ui.lista_dodatih_radnji

import com.eklitstudio.advokatkotilin.R
import com.eklitstudio.advokatkotilin.data.db.entity.IzracunatTrosakRadnje
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.izvrsena_radnja_item.*

class IzvrsenaRadnjaItem(val izracunataRadnja: IzracunatTrosakRadnje): Item() {


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply { cena_izracunate_radnje.text =  izracunataRadnja.cena.toString()}
        viewHolder.apply { textView_naziv_izvrsene_radnje.text = izracunataRadnja.naziv_radnje}
        viewHolder.apply { textView_date.text = izracunataRadnja.datum}
    }

    override fun getLayout() = R.layout.izvrsena_radnja_item
}
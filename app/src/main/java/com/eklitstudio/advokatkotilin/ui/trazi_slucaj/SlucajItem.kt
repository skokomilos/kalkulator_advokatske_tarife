package com.eklitstudio.advokatkotilin.ui.trazi_slucaj

import com.eklitstudio.advokatkotilin.R
import com.eklitstudio.advokatkotilin.data.db.entity.Slucaj
import com.eklitstudio.advokatkotilin.utilities.*
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.slucaj_item.*


class SlucajItem(val slucaj: Slucaj): Item() {

    fun odrediImePostupka(postupakId: Int): Int{
        var imePostupak : Int = R.string.no_string
        when (postupakId) {
            KRIVICNI_POSTUPAK -> imePostupak = R.string.krivicni_postupak
            PARNICNI_POSTUPAK -> imePostupak = R.string.parnicni_postupak
            PREKRSAJNI_POSTUPAK -> imePostupak = R.string.prekrsajni_postupak
            IZVRSNI_POSTUPAK -> imePostupak = R.string.izvrsni_postupak
            VANPARNICNI_POSTUPAK -> imePostupak = R.string.vanparnicni_postupak
            REGISTAR_NEPOKRETNOSTI -> imePostupak = R.string.registar_nepokretnosti
            STECAJNI_LIKVIDACIONI_POSTUPAK -> imePostupak = R.string.stecajni_postupak
            UPRAVNI_POSTUPAK -> imePostupak = R.string.upravni_postupak
            UPRAVNI_SPOROVI -> imePostupak = R.string.upravni_sporovi
            POSTUPAK_APR -> imePostupak = R.string.postupak_pred_apr
            POSTUPAK_PRED_POSLODAVCEM -> imePostupak = R.string.postupak_pred_poslodavcem
            POSTUPAK_PRED_USTAVNIM_SUDOM -> imePostupak = R.string.postupak_pred_ustavnim_sudom
            POSTUPAK_PRED_MEDJUNARODNIM_SUDOM -> imePostupak = R.string.postupak_pored_medjunarodnim_sudom
            MEDIJACIJA -> imePostupak = R.string.medijacija
            OSTALI_POSTUPCI -> imePostupak = R.string.ostali_postupci
//            else -> { // Note the block
//                imePostupak
//            }
        }
        return imePostupak
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            slucaj_item_naziv_slucaja.setText("sifra slucaja: ${slucaj.sifra_slucaja.toString()}")
            slucaj_item_postupak_naziv.setText(odrediImePostupka(slucaj.postupak_id.toInt()))
        }
    }

    override fun getLayout() = R.layout.slucaj_item



}
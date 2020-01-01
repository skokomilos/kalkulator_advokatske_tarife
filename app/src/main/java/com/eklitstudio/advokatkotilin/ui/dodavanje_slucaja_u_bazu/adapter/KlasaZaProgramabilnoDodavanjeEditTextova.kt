package com.eklitstudio.advokatkotilin.ui.dodavanje_slucaja_u_bazu.adapter

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class KlasaZaProgramabilnoDodavanjeEditTextova {

    fun kreirajTextViewKojiCePreikazivatiBrojStranaka(context : Context, broj : String) : TextView{
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val textView = TextView(context)
        textView.setPadding(10, 10, 0, 0)
        textView.setTextColor(Color.rgb(0, 0, 0))
        textView.layoutParams = layoutParams
        textView.maxEms = 2
        textView.setText(broj + ".")
        return textView
    }

    fun kreirajEditTextZaIme(context: Context): EditText{
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val editText = EditText(context)
        val id = 0
        editText.id = id
        editText.hint = "Ime i Prezime"
        editText.layoutParams = layoutParams
        editText.setTextColor(Color.rgb(0, 0, 0))
        editText.inputType = InputType.TYPE_CLASS_TEXT
        return editText
    }

    fun kreirajEditTextZaAdresu(context: Context): EditText {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val editText = EditText(context)
        val id = 1
        editText.id = id
        editText.hint = "Adresa"
        editText.layoutParams = layoutParams
        editText.setTextColor(Color.rgb(0, 0, 0))
        editText.inputType = InputType.TYPE_CLASS_TEXT
        return editText
    }

    fun kreirajEditTextZaMesto(context: Context): EditText {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val editText = EditText(context)
        val id = 2
        editText.id = id
        editText.hint = "Mesto"
        editText.layoutParams = layoutParams
        editText.setTextColor(Color.rgb(0, 0, 0))
        editText.inputType = InputType.TYPE_CLASS_TEXT
        return editText
    }
}
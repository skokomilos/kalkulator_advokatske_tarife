package com.eklitstudio.advokatkotilin.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "stranka",
    foreignKeys = [
    ForeignKey(
        entity = Slucaj::class,
        parentColumns = ["id"],
        childColumns = ["slucajId"]
    )],
    indices = arrayOf(
        Index(value = ["slucajId"],
            unique = true)
    ))
class Stranka(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    var ime_i_prezime: String?,
    var adresa: String?,
    var mesto: String?,
    var slucajId: String
) {

    constructor(
        ime_i_prezime: String?,
        adresa: String?,
        mesto: String?): this(null, ime_i_prezime, adresa, mesto, "")
}
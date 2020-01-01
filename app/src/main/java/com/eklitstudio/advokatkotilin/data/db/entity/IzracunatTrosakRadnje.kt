package com.eklitstudio.advokatkotilin.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "izracunat_trosak_radnje",
    foreignKeys = [
        ForeignKey(
            entity = Slucaj::class,
            parentColumns = ["id"],
            childColumns = ["slucaj_id"]
        )
    ],
    indices = arrayOf(
        Index(value = ["slucaj_id"],
            unique = false)
    ))
data class IzracunatTrosakRadnje @JvmOverloads constructor(
    @PrimaryKey()
    @NotNull
    val id: String = UUID.randomUUID().toString(),
    val naziv_radnje: String?,
    val datum: String?,
    val cena: Double?,
    val slucaj_id: String
){


}
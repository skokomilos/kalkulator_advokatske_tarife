package com.eklitstudio.advokatkotilin.data.db.entity

import androidx.room.*
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "slucaj",
    foreignKeys = [
        ForeignKey(
            entity = Postupak::class,
            parentColumns = ["id"],
            childColumns = ["postupak_id"])
    ],
            indices = arrayOf(
            Index(value = ["sifra_slucaja"],
                unique = true)
            )
)
data class Slucaj @JvmOverloads constructor(
    @PrimaryKey()
    @NotNull
    var id: String = UUID.randomUUID().toString(),
    val sifra_slucaja: Int,
    val broj_stranaka: Int,
    val okrivljen_ostecen: Int,
    val vrsta_odbrane: Int,
    val tabela_bodova: Long,
    @ColumnInfo(index = true)
    val postupak_id: Long
) {

    override fun toString(): String {
        return sifra_slucaja.toString()
    }
}
package com.eklitstudio.advokatkotilin.data.db.entity

import androidx.room.*
import org.jetbrains.annotations.NotNull

@Entity(tableName = "tabela_bodova",
        foreignKeys = [
            ForeignKey(
            entity = Postupak::class,
            parentColumns = ["id"],
            childColumns = ["postupak_id"]),
            ForeignKey(
                entity = VrstaParnice::class,
                parentColumns = ["id"],
                childColumns = ["vrste_parnica_id"]
            )
    ],
    indices = arrayOf(
        Index(value = ["postupak_id", "vrste_parnica_id"],
            unique = false)
    )
)
class TabelaBodova(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    val id: Long,
    @NotNull
    val tarifni_uslov: String,
    @NotNull
    val bodovi: Int,
    @ColumnInfo(index = true)
    val postupak_id: Long?,
    @ColumnInfo(index = true)
    val vrste_parnica_id: Long?) {

    override fun toString(): String {
        return tarifni_uslov
    }
}

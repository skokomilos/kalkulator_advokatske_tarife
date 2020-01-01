package com.eklitstudio.advokatkotilin.ui.callback

import com.eklitstudio.advokatkotilin.data.db.entity.Slucaj

interface SlucajCallback {
    abstract fun onClick(slucaj: Slucaj
    )
}
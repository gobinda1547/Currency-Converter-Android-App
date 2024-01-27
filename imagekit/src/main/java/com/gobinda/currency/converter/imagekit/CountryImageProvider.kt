package com.gobinda.currency.converter.imagekit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object CountryImageProvider {
    fun getBitmap(context: Context, countryCode: String): Bitmap {
        try {
            val validFileName = countryCode.lowercase().substring(0, 2) + ".png"
            val inputStream = context.assets.open(validFileName)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val validFileName = "not_found.png"
        val inputStream = context.assets.open(validFileName)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        return bitmap
    }
}
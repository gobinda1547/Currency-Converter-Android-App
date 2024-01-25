package com.gobindacurrency.converter.countryimages

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream

object CountryImageProvider {
    fun getStream(context: Context, countryCode: String): InputStream? {
        return try {
            context.assets.open(getFileNameFromCountryCode(countryCode))
        } catch (ignore: Exception) {
            null
        }
    }

    fun getBitmap(context: Context, countryCode: String): Bitmap? {
        return try {
            val stream = getStream(context, countryCode)
            BitmapFactory.decodeStream(stream)
        } catch (ignore: Exception) {
            null
        }
    }

    fun doesExist(context: Context, countryCode: String): Boolean {
        return try {
            val fileName = getFileNameFromCountryCode(countryCode)
            context.assets.open(fileName).close()
            true
        } catch (ignore: Exception) {
            false
        }
    }

    fun getFileNameFromCountryCode(countryCode: String): String {
        return countryCode.lowercase().substring(0, 2) + ".png"
    }
}
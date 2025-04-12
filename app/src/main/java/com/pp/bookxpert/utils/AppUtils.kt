package com.pp.bookxpert.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

object AppUtils {
    suspend fun downloadPdfFile(context: Context, url: String): File? = withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val file = File(context.cacheDir, "report_temp.pdf")
                file.outputStream().use { output ->
                    response.body?.byteStream()?.copyTo(output)
                }
                return@withContext file
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }

}
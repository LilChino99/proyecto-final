package com.example.gamevault.ui.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Helper para gestionar la creación de archivos de imagen locales
 * y la obtención de URIs seguras mediante FileProvider.
 */
object PhotoFileProvider {

    /**
     * Crea un archivo temporal único para la foto en el directorio interno de la app
     * y retorna tanto el objeto File como la Uri generada por FileProvider.
     */
    fun createPhotoFile(context: Context): PhotoFileResult {
        val storageDir = File(context.filesDir, "review_photos").apply {
            if (!exists()) mkdirs()
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile = File(storageDir, "REVIEW_PHOTO_${timeStamp}.jpg")

        val photoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )

        return PhotoFileResult(
            file = photoFile,
            uri = photoUri,
            absolutePath = photoFile.absolutePath
        )
    }
}

data class PhotoFileResult(
    val file: File,
    val uri: Uri,
    val absolutePath: String
)

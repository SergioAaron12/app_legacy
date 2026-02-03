package com.example.legacyframeapp.domain
import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

object ImageStorageHelper {

    /**
        * @param context Application context.
        * @param uri Uri of the selected image (e.g., "content://...")
        * @return The unique file name saved (e.g., "product_1678886400.jpg")
        * @throws IOException If the copy fails.
     */
    suspend fun saveImageToInternalStorage(context: Context, uri: Uri): String {
        return withContext(Dispatchers.IO) {
            // 1. Generate a unique file name.
            val fileName = "product_${System.currentTimeMillis()}.jpg"

            // 2. Open the input stream from the Uri (gallery).
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw IOException("No se pudo abrir el stream desde el Uri: $uri")

            // 3. Define the output file in internal storage.
            // 'filesDir' is your app's private folder.
            val outputFile = File(context.filesDir, fileName)

            // 4. Open the output stream to the new file.
            val outputStream = outputFile.outputStream()

            // 5. Copy the bytes and close the streams.
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // 6. Return only the file name.
            fileName
        }
    }

    /**
     * Get the image file from internal storage.
     *
     * @param context Application context.
     * @param fileName File name (the one saved in the DB).
     * @return A File pointing to the saved image.
     */
    fun getImageFile(context: Context, fileName: String): File {
        return File(context.filesDir, fileName)
    }
}
package info.toughlife.mcdev.core.io

import com.google.api.client.http.FileContent
import info.toughlife.mcdev.Auxilia
import java.text.SimpleDateFormat
import java.util.*

internal object FileUploader {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    private fun createFolder(): String {
        val fileMetadata = com.google.api.services.drive.model.File()
        fileMetadata.name = dateFormat.format(System.currentTimeMillis())
        fileMetadata.mimeType = "application/vnd.google-apps.folder"
        val file = Auxilia.drive.files().create(fileMetadata)
            .setFields("id")
            .execute()
        return file.id
    }

    private fun getParentId(): String {
        val result = Auxilia.drive.files().list()
            .setPageSize(10)
            .setFields("nextPageToken, files(id, name)")
            .execute()
        val files = result.files
        if (files == null || files.isEmpty()) {
            return createFolder()
        } else {
            for (file in files) {
                if (file.name == dateFormat.format(System.currentTimeMillis())) {
                    return file.id
                }
            }
            return createFolder()
        }
    }

    fun upload(filePath: java.io.File) {
        val parentId = getParentId()
        val fileMetadata = com.google.api.services.drive.model.File()
        fileMetadata.name = filePath.name
        fileMetadata.parents = Collections.singletonList(parentId)
        val mediaContent = FileContent(FileCompressionManager.getMimeType(), filePath)
        val file = Auxilia.drive.files().create(fileMetadata, mediaContent)
            .setFields("id")
            .execute()
    }

}
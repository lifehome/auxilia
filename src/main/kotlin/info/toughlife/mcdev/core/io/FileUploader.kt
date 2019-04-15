package info.toughlife.mcdev.core.io

import com.google.api.client.http.FileContent
import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.core.io.config.configInfo
import java.text.SimpleDateFormat

internal object FileUploader {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    private val teamDriveId = configInfo().teamDriveId

    private fun createFolder(): String {
        val fileMetadata = com.google.api.services.drive.model.File()
        fileMetadata.name = dateFormat.format(System.currentTimeMillis())
        fileMetadata.teamDriveId = teamDriveId
        fileMetadata.parents = mutableListOf(teamDriveId)
        fileMetadata.mimeType = "application/vnd.google-apps.folder"
        val file = Auxilia.drive.files().create(fileMetadata)
            .setFields("id")
            .setSupportsTeamDrives(true)
            .execute()
        return file.id
    }

    private fun getParentId(): String {
        val result = Auxilia.drive.files().list()
            .setSupportsTeamDrives(true)
            .setCorpora("teamDrive")
            .setTeamDriveId(teamDriveId)
            .setIncludeTeamDriveItems(true)
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
        fileMetadata.teamDriveId = teamDriveId
        fileMetadata.parents = mutableListOf(parentId)
        val mediaContent = FileContent(FileCompressionManager.getMimeType(), filePath)
        Auxilia.drive.files().create(fileMetadata, mediaContent)
            .setFields("id")
            .setSupportsTeamDrives(true)
            .execute()
    }

}
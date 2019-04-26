package info.toughlife.mcdev.core.io.drive

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.drive.Drive
import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.core.io.FileCompressionManager
import info.toughlife.mcdev.core.io.config.configInfo
import java.io.FileInputStream
import java.text.SimpleDateFormat

class DriveTeamOptions : DriveOptions {
    override fun connect(): Drive {
        val httpTransport = NetHttpTransport()
        val credential = GoogleCredential
            .fromStream(FileInputStream(CREDENTIALS_FILE_PATH))
            .createScoped(SCOPES)
        return Drive.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME).build()
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    private val teamDriveId = configInfo().teamDriveId

    override fun createFolder(): String {
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

    override fun getParentId(): String {
        val result = Auxilia.drive.files().list()
            .setSupportsTeamDrives(true)
            .setQ("trashed = false")
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

    override fun listFiles(): List<String> {
        val list = mutableListOf<String>()
        val result = Auxilia.drive.files().list()
            .setSupportsTeamDrives(true)
            .setQ("trashed = false and not mimeType = 'application/vnd.google-apps.folder'")
            .setCorpora("teamDrive")
            .setTeamDriveId(teamDriveId)
            .setIncludeTeamDriveItems(true)
            .setPageSize(10)
            .setFields("nextPageToken, files(id, name)")
            .execute()
        val files = result.files
        for (file in files) {
            list.add(file.name)
        }
        return list
    }

    override fun upload(file: java.io.File) {
        val parentId = getParentId()
        val fileMetadata = com.google.api.services.drive.model.File()
        fileMetadata.name = file.name
        fileMetadata.teamDriveId = teamDriveId
        fileMetadata.parents = mutableListOf(parentId)
        val mediaContent = FileContent(FileCompressionManager.getMimeType(), file)
        Auxilia.drive.files().create(fileMetadata, mediaContent)
            .setFields("id")
            .setSupportsTeamDrives(true)
            .execute()
    }
}
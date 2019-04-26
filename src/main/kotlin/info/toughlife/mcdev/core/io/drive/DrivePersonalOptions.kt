package info.toughlife.mcdev.core.io.drive

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import info.toughlife.mcdev.Auxilia
import info.toughlife.mcdev.core.io.FileCompressionManager
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat

class DrivePersonalOptions : DriveOptions {

    private val TOKENS_DIRECTORY_PATH = Auxilia.instance.dataFolder.absolutePath + "/tokens"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    @Throws(IOException::class)
    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
        val input = DrivePersonalOptions::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
            ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(input))
        val flow = (GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(FileDataStoreFactory(java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline"))
            .build()
        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    override fun connect(): Drive {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return Drive.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
            .setApplicationName(APPLICATION_NAME)
            .build()
    }

    override fun createFolder(): String {
        val fileMetadata = com.google.api.services.drive.model.File()
        fileMetadata.name = dateFormat.format(System.currentTimeMillis())
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
            .setPageSize(10)
            .setFields("nextPageToken, files(id, name)")
            .execute()
        val files = result.files
        for (file in files) {
            list.add(file.name)
        }
        return list
    }

    override fun upload(file: File) {
        val parentId = getParentId()
        val fileMetadata = com.google.api.services.drive.model.File()
        fileMetadata.name = file.name
        fileMetadata.parents = mutableListOf(parentId)
        val mediaContent = FileContent(FileCompressionManager.getMimeType(), file)
        Auxilia.drive.files().create(fileMetadata, mediaContent)
            .setFields("id")
            .execute()
    }
}
package info.toughlife.mcdev

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import java.io.FileInputStream

object GoogleDriveAPI {
    private val APPLICATION_NAME = "Auxilia"
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    private val SCOPES = listOf(DriveScopes.DRIVE)
    private val CREDENTIALS_FILE_PATH = Auxilia.instance.dataFolder.absolutePath + "/credentials.json"

    fun createService(): Drive {
        val httpTransport = NetHttpTransport()
        val credential = GoogleCredential
            .fromStream(FileInputStream(CREDENTIALS_FILE_PATH))
            .createScoped(SCOPES)
        return Drive.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME).build()
    }

}

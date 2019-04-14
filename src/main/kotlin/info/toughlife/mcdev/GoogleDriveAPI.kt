package info.toughlife.mcdev

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import java.io.File
import java.io.FileInputStream

import java.io.InputStreamReader

object GoogleDriveAPI {
    private val APPLICATION_NAME = "Auxilia"
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    private val TOKENS_DIRECTORY_PATH = Auxilia.instance.dataFolder.absolutePath + "/tokens"

    private val SCOPES = listOf(DriveScopes.DRIVE)
    private val CREDENTIALS_FILE_PATH = Auxilia.instance.dataFolder.absolutePath + "/credentials.json"

    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
        val input = FileInputStream(File(CREDENTIALS_FILE_PATH))
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(input))
        val flow = GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(FileDataStoreFactory(java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("online")
                .build()
        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    fun createService(): Drive {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        return Drive.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
            .setApplicationName(APPLICATION_NAME)
            .build()
    }

}

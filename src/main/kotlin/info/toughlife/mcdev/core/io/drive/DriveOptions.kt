package info.toughlife.mcdev.core.io.drive

import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import info.toughlife.mcdev.Auxilia
import java.io.File

val APPLICATION_NAME = "Auxilia"
val JSON_FACTORY = JacksonFactory.getDefaultInstance()
val SCOPES = listOf(DriveScopes.DRIVE)
val CREDENTIALS_FILE_PATH = Auxilia.instance.dataFolder.absolutePath + "/credentials.json"

interface DriveOptions {
    fun connect(): Drive
    fun createFolder(): String
    fun getParentId(): String
    fun listFiles(): List<String>
    fun upload(file: File)
}
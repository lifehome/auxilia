package info.toughlife.mcdev.core

enum class AuxiliaQueueAction(val index: Int, val fancyText: String) {
    STARTING(1, "&4Starting..."),
    FETCH(2, "&cFetching files..."),
    COMPRESS(3, "&6Compressing files..."),
    UPLOAD(4, "&aUploading..."),
    CLEANUP(5, "&2Cleaning up...");
}
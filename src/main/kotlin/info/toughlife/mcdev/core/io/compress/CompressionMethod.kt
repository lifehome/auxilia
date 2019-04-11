package info.toughlife.mcdev.core.io.compress

/**
 * General compression method list
 * warning - GZIP does not work with 7zip compression methods and vice versa
 */
enum class CompressionMethod {

    // 7Zip compression methods
    COPY,
    LZMA,
    LZMA2,
    DEFLATE,
    BZIP2,
    AES256SHA256,
    BCJ_X86_FILTER,
    BCJ_PPC_FILTER,
    BCJ_IA64_FILTER,
    BCJ_ARM_FILTER,
    BCJ_ARM_THUMB_FILTER,
    BCJ_SPARC_FILTER,
    DELTA_FILTER,

    // Tar compression methods
    GZIP;

}
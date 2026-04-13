package com.GrandSphere.Topiks.ui.viewmodels

/**
 * Which payload type to share when selected messages contain mixed content.
 */
enum class ShareOption {
    TEXT_ONLY,
    IMAGES_ONLY,
    ATTACHMENTS_ONLY,
}

data class ShareDialogState(
    val hasText: Boolean,
    val imageCount: Int,
    val attachmentCount: Int,
)

sealed interface ShareRequest {
    data class Text(val text: String) : ShareRequest
    data class Files(
        val filePaths: List<String>,
        /** Hint for the outgoing intent type. */
        val mimeType: String,
    ) : ShareRequest
}


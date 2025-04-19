package com.GrandSphere.Topiks.model.dataClasses

import androidx.compose.ui.text.AnnotatedString
import com.GrandSphere.Topiks.db.entities.FileInfoWithIcon

data class MessageUiModel(
    val id: Int,
    val annotatedContent: AnnotatedString,
    val timestamp: String,
    val pictures: List<FileInfoWithIcon>,
    val attachments: List<String>,
    val hasPictures: Boolean,
    val hasAttachments: Boolean,
    val isSelected: Boolean,
    val onDelete: () -> Unit,
    val onSelect: () -> Unit,
    val onView: () -> Unit,
    val onEdit: () -> Unit,
    val onExport: () -> Unit
)
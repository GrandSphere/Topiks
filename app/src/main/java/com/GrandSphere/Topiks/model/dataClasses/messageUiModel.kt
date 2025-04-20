package com.GrandSphere.Topiks.model.dataClasses

import com.GrandSphere.Topiks.db.entities.FileInfoWithIcon

data class MessageUiModel(
    val id: Int,
    val messageContent: String,
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
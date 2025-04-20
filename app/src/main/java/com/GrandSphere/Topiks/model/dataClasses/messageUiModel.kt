/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
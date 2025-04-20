package com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos

/**
 * Handles exporting messages to PDF.
 */
interface ExportRepository {
    /** Export selected messages to a PDF file */
    suspend fun exportMessagesToPDF(messageIDs: Set<Int>)
}
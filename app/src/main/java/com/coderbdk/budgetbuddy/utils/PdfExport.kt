package com.coderbdk.budgetbuddy.utils

import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.net.toFile
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.util.Locale

object PdfExport {

    fun export(output: File, transactions: List<TransactionWithBothCategories>) {
        val document = PdfDocument()

        val pageWidth = 595
        val pageHeight = 842
        val margin = 30f

        val titlePaint = Paint().apply {
            textSize = 16f
            isFakeBoldText = true
        }
        val headerPaint = Paint().apply {
            textSize = 12f
            isFakeBoldText = true
        }
        val dataPaint = Paint().apply {
            textSize = 10f
        }

        val lineHeight = 18f
        var currentY = margin + titlePaint.textSize

        val dateFormat = DateFormat.getDateInstance()

        fun startNewPage(document: PdfDocument, pageNumber: Int): PdfDocument.Page {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            canvas.drawText("Transaction Report - Page $pageNumber", margin, margin, titlePaint)
            currentY = margin + titlePaint.textSize + margin

            val xStart = margin
            val xDate = xStart
            val xType = xStart + 80f
            val xAmount = xStart + 150f
            val xCategory = xStart + 220f
            val xNotes = xStart + 350f

            currentY += lineHeight
            canvas.drawText("Date", xDate, currentY, headerPaint)
            canvas.drawText("Type", xType, currentY, headerPaint)
            canvas.drawText("Amount", xAmount, currentY, headerPaint)
            canvas.drawText("Category", xCategory, currentY, headerPaint)
            canvas.drawText("Notes", xNotes, currentY, headerPaint)
            currentY += 5f
            canvas.drawLine(
                margin,
                currentY,
                pageWidth - margin,
                currentY,
                headerPaint
            )
            currentY += 5f

            return page
        }

        var pageNumber = 1
        var currentPage = startNewPage(document, pageNumber)
        var canvas = currentPage.canvas

        for (transactionWithCat in transactions) {
            if (currentY + lineHeight > pageHeight - margin) {
                document.finishPage(currentPage)

                pageNumber++
                currentPage = startNewPage(document, pageNumber)
                canvas = currentPage.canvas
            }

            val transaction = transactionWithCat.transaction
            val date = dateFormat.format(java.util.Date(transaction.transactionDate))
            val type = transaction.type.name
            val amount = String.format(Locale.getDefault(), "%.2f", transaction.amount)
            val categoryName =
                transactionWithCat.expenseCategory?.name ?: transactionWithCat.incomeCategory?.name
                ?: "N/A"
            val notes = transaction.notes ?: ""

            val xStart = margin
            val xDate = xStart
            val xType = xStart + 80f
            val xAmount = xStart + 150f
            val xCategory = xStart + 220f
            val xNotes = xStart + 350f

            currentY += lineHeight

            canvas.drawText(date, xDate, currentY, dataPaint)
            canvas.drawText(type, xType, currentY, dataPaint)
            canvas.drawText(amount, xAmount, currentY, dataPaint)
            canvas.drawText(categoryName, xCategory, currentY, dataPaint)

            val maxNoteWidth = (pageWidth - margin) - xNotes
            val clippedNotes = if (dataPaint.measureText(notes) > maxNoteWidth) {
                notes.substring(0, dataPaint.breakText(notes, true, maxNoteWidth, null)) + "..."
            } else {
                notes
            }
            canvas.drawText(clippedNotes, xNotes, currentY, dataPaint)
        }

        document.finishPage(currentPage)

        try {
            document.writeTo(output.outputStream())
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            document.close()
        }
    }
}
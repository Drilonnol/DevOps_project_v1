package com.devtiro.bookstore.services.observer

import com.devtiro.bookstore.domain.entities.AuthorEntity
import java.io.IOException
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter


class LoggerObserver : Observer {

    private val logger: Logger = Logger.getLogger(LoggerObserver::class.java.name)
    private lateinit var fileHandler: FileHandler

    init {
        try {
            fileHandler = FileHandler("author_changes.log", true)
            fileHandler.formatter = SimpleFormatter()
            logger.addHandler(fileHandler)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun update(author: AuthorEntity) {
        val logMessage = "📝 Author updated: ${author.name}, Age: ${author.age}, Description: ${author.description}"
        logger.info(logMessage)
    }

    fun close() {
        fileHandler.close()
    }
}
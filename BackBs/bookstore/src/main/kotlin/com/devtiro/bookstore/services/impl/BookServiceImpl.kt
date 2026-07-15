package com.devtiro.bookstore.services.impl

import com.devtiro.bookstore.domain.BookSummary
import com.devtiro.bookstore.domain.BookUpdateRequest
import com.devtiro.bookstore.domain.entities.BookEntity
import com.devtiro.bookstore.repositories.AuthorRepository
import com.devtiro.bookstore.repositories.BookRepository
import com.devtiro.bookstore.services.BookService
import com.devtiro.bookstore.services.strategy.AddBookStrategy
import com.devtiro.bookstore.services.strategy.BookContext
import com.devtiro.bookstore.services.strategy.UpdateBookStrategy
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookServiceImpl(
    val bookRepository: BookRepository,
    val authorRepository: AuthorRepository
) : BookService {

    private val addBookStrategy = AddBookStrategy(bookRepository, authorRepository)
    private val updateBookStrategy = UpdateBookStrategy(bookRepository)

    @Transactional
    override fun createUpdate(isbn: String, bookSummary: BookSummary): Pair<BookEntity, Boolean> {
        val context = BookContext(if (
            bookRepository.existsById(isbn))
            updateBookStrategy else addBookStrategy
        )
        return context.executeOperation(bookSummary, isbn)  // Correct: passing BookSummary
    }

    override fun list(authorId: Long?): List<BookEntity> {
        return authorId?.let {
            bookRepository.findByAuthorEntityId(it)
        } ?: bookRepository.findAll()
    }

    override fun get(isbn: String): BookEntity? {
        return bookRepository.findByIdOrNull(isbn)
    }

    override fun partialUpdate(isbn: String, bookUpdateRequest: BookUpdateRequest): BookEntity {
        val existingBook = bookRepository.findByIdOrNull(isbn)
        checkNotNull(existingBook)

        val updatedBook = existingBook.copy(
            title = bookUpdateRequest.title ?: existingBook.title,
            description = bookUpdateRequest.description ?: existingBook.description,
            image = bookUpdateRequest.image ?: existingBook.image
        )

        return bookRepository.save(updatedBook)
    }

    override fun delete(isbn: String) {
        bookRepository.deleteById(isbn)
    }
}
package com.devtiro.bookstore.services.strategy

import com.devtiro.bookstore.domain.BookSummary
import com.devtiro.bookstore.domain.entities.BookEntity
import com.devtiro.bookstore.repositories.AuthorRepository
import com.devtiro.bookstore.repositories.BookRepository
import com.devtiro.bookstore.toBookEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

// Strategy Interface
interface BookOperationStrategy {
    fun execute(bookSummary: BookSummary, isbn: String): Pair<BookEntity, Boolean>
}


class AddBookStrategy(private val bookRepository: BookRepository, private val authorRepository: AuthorRepository) : BookOperationStrategy {
    override fun execute(bookSummary: BookSummary, isbn: String): Pair<BookEntity, Boolean> {
        val normalisedBook = bookSummary.copy(isbn = isbn)
        val isExists = bookRepository.existsById(isbn)

        val author = authorRepository.findByIdOrNull(normalisedBook.author.id)
        checkNotNull(author)

        val savedBook = bookRepository.save(normalisedBook.toBookEntity(author))
        return Pair(savedBook, !isExists)
    }
}

// Përditësimi i librit
class UpdateBookStrategy(private val bookRepository: BookRepository) : BookOperationStrategy {
    override fun execute(bookSummary: BookSummary, isbn: String): Pair<BookEntity, Boolean> {
        val existingBook = bookRepository.findByIdOrNull(isbn)
        checkNotNull(existingBook)

        val updatedBook = existingBook.copy(
            title = bookSummary.title,
            description = bookSummary.description,
            image = bookSummary.image
        )

        return Pair(bookRepository.save(updatedBook), false)
    }
}

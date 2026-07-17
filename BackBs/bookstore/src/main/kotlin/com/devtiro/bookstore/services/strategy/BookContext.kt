package com.devtiro.bookstore.services.strategy

import com.devtiro.bookstore.domain.BookSummary
import com.devtiro.bookstore.domain.entities.BookEntity

class BookContext(private var strategy: BookOperationStrategy) {
    fun setStrategy(strategy: BookOperationStrategy) {
        this.strategy = strategy
    }

    fun executeOperation(
        bookSummary: BookSummary,
        isbn: String,
    ): Pair<BookEntity, Boolean> {
        return strategy.execute(bookSummary, isbn)
    }
}

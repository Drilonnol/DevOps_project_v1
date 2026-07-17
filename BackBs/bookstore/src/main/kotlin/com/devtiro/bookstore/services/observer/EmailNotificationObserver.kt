package com.devtiro.bookstore.services.observer

import com.devtiro.bookstore.domain.entities.AuthorEntity

class EmailNotificationObserver(private val email: String) : Observer {
    override fun update(author: AuthorEntity) {
        println("📧 Sending email to $email: Author '${author.name}' has been updated!")
    }
}

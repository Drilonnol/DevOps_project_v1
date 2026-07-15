package com.devtiro.bookstore.services.observer

import com.devtiro.bookstore.domain.entities.AuthorEntity

interface AuthorObserver : Observer {
    fun onAuthorCreated(author: AuthorEntity)
}
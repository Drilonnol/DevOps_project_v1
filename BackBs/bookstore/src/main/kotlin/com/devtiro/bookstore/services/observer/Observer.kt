package com.devtiro.bookstore.services.observer

import com.devtiro.bookstore.domain.entities.AuthorEntity

interface Observer {
    fun update(author: AuthorEntity)
}
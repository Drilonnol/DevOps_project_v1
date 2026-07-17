package com.devtiro.bookstore.services.observer

import com.devtiro.bookstore.domain.entities.AuthorEntity

interface Subject {
    fun registerObserver(observer: AuthorObserver)

    fun removeObserver(observer: Observer)

    fun notifyObservers(author: AuthorEntity)
}

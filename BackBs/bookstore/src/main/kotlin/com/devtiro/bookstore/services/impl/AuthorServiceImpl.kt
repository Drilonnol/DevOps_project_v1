package com.devtiro.bookstore.services.impl

import com.devtiro.bookstore.domain.AuthorUpdateRequest
import com.devtiro.bookstore.domain.entities.AuthorEntity
import com.devtiro.bookstore.repositories.AuthorRepository
import com.devtiro.bookstore.services.AuthorService
import com.devtiro.bookstore.services.observer.AuthorObserver
import com.devtiro.bookstore.services.observer.Observer
import com.devtiro.bookstore.services.observer.Subject
import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArrayList

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : AuthorService, Subject {
    private val observers: MutableList<AuthorObserver> = CopyOnWriteArrayList()

    override fun registerObserver(observer: AuthorObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers(author: AuthorEntity) {
        observers.forEach { it.update(author) }
    }

    override fun create(author: AuthorEntity): AuthorEntity {
        require(author.id == null) { "Author ID must be null for create." }
        require(author.age in 1..120) { "Author age must be between 1 and 120." }
        require(author.description.length <= 512) { "Description must be 512 characters or less." }

        val saved = authorRepository.save(author)
        notifyObservers(saved)
        return saved
    }

    override fun list(): List<AuthorEntity> = authorRepository.findAll()

    override fun get(id: Long): AuthorEntity? = if (id < 0) null else authorRepository.findById(id).orElse(null)

    override fun fullUpdate(
        id: Long,
        author: AuthorEntity,
    ): AuthorEntity {
        check(authorRepository.existsById(id)) { "Author not found" }
        require(author.age in 1..120) { "Author age must be between 1 and 120." }
        require(author.description.length <= 512) { "Description must be 512 characters or less." }

        val updated = author.copy(id = id)
        val saved = authorRepository.save(updated)
        notifyObservers(saved)
        return saved
    }

    override fun partialUpdate(
        id: Long,
        request: AuthorUpdateRequest,
    ): AuthorEntity {
        val existing =
            authorRepository.findById(id)
                .orElseThrow { IllegalStateException("Author not found") }

        val newAge = request.age ?: existing.age
        val newDescription = request.description ?: existing.description

        require(newAge in 1..120) { "Author age must be between 1 and 120." }
        require(newDescription.length <= 512) { "Description must be 512 characters or less." }

        val updated =
            existing.copy(
                name = request.name ?: existing.name,
                age = newAge,
                description = newDescription,
                image = request.image ?: existing.image,
            )
        val saved = authorRepository.save(updated)
        notifyObservers(saved)
        return saved
    }

    override fun delete(id: Long) {
        if (id < 0) throw IllegalStateException("Invalid author ID")
        val existing =
            authorRepository.findById(id)
                .orElseThrow { IllegalStateException("Author not found") }
        authorRepository.deleteById(id)
        notifyObservers(existing)
    }
}

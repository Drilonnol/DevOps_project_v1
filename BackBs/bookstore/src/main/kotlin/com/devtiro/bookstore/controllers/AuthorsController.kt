package com.devtiro.bookstore.controllers

import com.devtiro.bookstore.domain.dto.AuthorDto
import com.devtiro.bookstore.domain.dto.AuthorUpdateRequestDto
import com.devtiro.bookstore.services.AuthorService
import com.devtiro.bookstore.toAuthorDto
import com.devtiro.bookstore.toAuthorEntity
import com.devtiro.bookstore.toAuthorUpdateRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.slf4j.Logger

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping(path = ["/v1/authors"])
class AuthorsController(private val authorService: AuthorService) {

    // Krijoni një logger
    private val logger: Logger = LoggerFactory.getLogger(AuthorsController::class.java)

    @PostMapping
    fun createAuthor(@RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        // Logoni kërkesën para përpunimit
        logger.info("Processing create author request")

        return try {
            val createdAuthor = authorService.create(
                authorDto.toAuthorEntity()
            ).toAuthorDto()

            // Logoni pas krijimit
            logger.info("Successfully created author: ${createdAuthor.name}")
            ResponseEntity(createdAuthor, HttpStatus.CREATED)
        } catch (ex: IllegalArgumentException) {
            // Logoni nëse ka ndodhur një gabim
            logger.error("Error creating author: ${ex.message}")
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping
    fun readManyAuthor(): List<AuthorDto> {
        return authorService.list().map { it.toAuthorDto() }
    }

    @GetMapping(path = ["/{id}"])
    fun readOneAuthor(@PathVariable("id") id: Long): ResponseEntity<AuthorDto> {
        val foundAuthor = authorService.get(id)?.toAuthorDto()
        return foundAuthor?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping(path=["/{id}"])
    fun fullUpdateAuthor(@PathVariable("id") id: Long, @RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        return try {
            val updatedAuthor = authorService.fullUpdate(id, authorDto.toAuthorEntity())
            ResponseEntity(updatedAuthor.toAuthorDto(), HttpStatus.OK)

        } catch(ex: IllegalStateException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PatchMapping(path=["/{id}"])
    fun partialUpdateAuthor(@PathVariable("id") id: Long, @RequestBody authorUpdateRequest: AuthorUpdateRequestDto): ResponseEntity<AuthorDto> {
        return try {
            val updatedAuthor = authorService.partialUpdate(id, authorUpdateRequest.toAuthorUpdateRequest())
            ResponseEntity(updatedAuthor.toAuthorDto(), HttpStatus.OK)
        } catch (ex: IllegalStateException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping(path=["/{id}"])
    fun deleteAuthor(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        authorService.delete(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}
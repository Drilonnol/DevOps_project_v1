package com.devtiro.bookstore.controllers

import com.devtiro.bookstore.domain.entities.AuthorEntity
import com.devtiro.bookstore.services.AuthorService
import com.devtiro.bookstore.testAuthorDtoA
import com.devtiro.bookstore.testAuthorEntityA
import com.devtiro.bookstore.testAuthorUpdateRequestDtoA
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

private const val AUTHORS_BASE_URL = "/v1/authors"

@SpringBootTest
@AutoConfigureMockMvc
class AuthorsControllerTest
    @Autowired
    constructor(
        private val mockMvc: MockMvc,
        @MockkBean private val authorService: AuthorService,
    ) {
        private val objectMapper = ObjectMapper()

        @BeforeEach
        fun setupMocks() {
            // Default: authorService.create returns the input DTO as entity (simulating save)
            every { authorService.create(any()) } answers { firstArg() }
        }

        // ========== CREATE AUTHOR ==========

        @Test
        fun `create Author calls service with correct data`() {
            val authorDto = testAuthorDtoA()
            mockMvc.post(AUTHORS_BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(authorDto)
            }

            val expectedEntity =
                AuthorEntity(
                    id = null,
                    name = "John Doe",
                    age = 30,
                    image = "author-image.jpeg",
                    description = "Some description",
                )

            verify { authorService.create(expectedEntity) }
        }

        @Test
        fun `create Author returns HTTP 201 on success`() {
            mockMvc.post(AUTHORS_BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(testAuthorDtoA())
            }.andExpect {
                status { isCreated() }
            }
        }

        @Test
        fun `create Author returns HTTP 400 when IllegalArgumentException is thrown`() {
            every { authorService.create(any()) } throws IllegalArgumentException()

            mockMvc.post(AUTHORS_BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(testAuthorDtoA())
            }.andExpect {
                status { isBadRequest() }
            }
        }

        // ========== LIST AUTHORS ==========

        @Test
        fun `list Authors returns empty list and HTTP 200 when no authors exist`() {
            every { authorService.list() } returns emptyList()

            mockMvc.get(AUTHORS_BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
                content { json("[]") }
            }
        }

        @Test
        fun `list Authors returns authors and HTTP 200 when authors exist`() {
            val authorEntity = testAuthorEntityA(1)
            every { authorService.list() } returns listOf(authorEntity)

            mockMvc.get(AUTHORS_BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
                content { jsonPath("$[0].id", equalTo(1)) }
                content { jsonPath("$[0].name", equalTo("John Doe")) }
                content { jsonPath("$[0].age", equalTo(30)) }
                content { jsonPath("$[0].description", equalTo("Some description")) }
                content { jsonPath("$[0].image", equalTo("author-image.jpeg")) }
            }
        }

        // ========== GET AUTHOR BY ID ==========

        @Test
        fun `get Author returns HTTP 404 when author not found`() {
            every { authorService.get(any()) } returns null

            mockMvc.get("$AUTHORS_BASE_URL/999") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `get Author returns HTTP 200 and author when found`() {
            every { authorService.get(any()) } returns testAuthorEntityA(id = 999)

            mockMvc.get("$AUTHORS_BASE_URL/999") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
                content { jsonPath("$.id", equalTo(999)) }
                content { jsonPath("$.name", equalTo("John Doe")) }
                content { jsonPath("$.age", equalTo(30)) }
                content { jsonPath("$.description", equalTo("Some description")) }
                content { jsonPath("$.image", equalTo("author-image.jpeg")) }
            }
        }

        // ========== FULL UPDATE AUTHOR ==========

        @Test
        fun `full update Author returns HTTP 200 and updated author on success`() {
            every { authorService.fullUpdate(any(), any()) } answers { secondArg() }

            mockMvc.put("$AUTHORS_BASE_URL/999") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(testAuthorDtoA(id = 999))
            }.andExpect {
                status { isOk() }
                content { jsonPath("$.id", equalTo(999)) }
                content { jsonPath("$.name", equalTo("John Doe")) }
                content { jsonPath("$.age", equalTo(30)) }
                content { jsonPath("$.description", equalTo("Some description")) }
                content { jsonPath("$.image", equalTo("author-image.jpeg")) }
            }
        }

        @Test
        fun `full update Author returns HTTP 400 on IllegalStateException`() {
            every { authorService.fullUpdate(any(), any()) } throws IllegalStateException()

            mockMvc.put("$AUTHORS_BASE_URL/999") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(testAuthorDtoA(id = 999))
            }.andExpect {
                status { isBadRequest() }
            }
        }

        // ========== PARTIAL UPDATE AUTHOR ==========

        @Test
        fun `partial update Author returns HTTP 200 and updated author`() {
            every { authorService.partialUpdate(any(), any()) } returns testAuthorEntityA(id = 999)

            mockMvc.patch("$AUTHORS_BASE_URL/999") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(testAuthorUpdateRequestDtoA(999L))
            }.andExpect {
                status { isOk() }
                content { jsonPath("$.id", equalTo(999)) }
                content { jsonPath("$.name", equalTo("John Doe")) }
                content { jsonPath("$.age", equalTo(30)) }
                content { jsonPath("$.description", equalTo("Some description")) }
                content { jsonPath("$.image", equalTo("author-image.jpeg")) }
            }
        }

        @Test
        fun `partial update Author returns HTTP 400 on IllegalStateException`() {
            every { authorService.partialUpdate(any(), any()) } throws IllegalStateException()

            mockMvc.patch("$AUTHORS_BASE_URL/999") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(testAuthorUpdateRequestDtoA(999L))
            }.andExpect {
                status { isBadRequest() }
            }
        }

        // ========== DELETE AUTHOR ==========

        @Test
        fun `delete Author returns HTTP 204 even if author does not exist (idempotent)`() {
            every { authorService.delete(12345L) } answers { }

            mockMvc.delete("$AUTHORS_BASE_URL/12345") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isNoContent() }
            }
        }

        @Test
        fun `delete Author returns HTTP 400 on IllegalArgumentException`() {
            every { authorService.delete(any()) } throws IllegalArgumentException()

            mockMvc.delete("$AUTHORS_BASE_URL/invalid") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isBadRequest() }
            }
        }
    }

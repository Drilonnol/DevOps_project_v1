package com.devtiro.bookstore.services.impl

import com.devtiro.bookstore.domain.AuthorUpdateRequest
import com.devtiro.bookstore.domain.entities.AuthorEntity
import com.devtiro.bookstore.repositories.AuthorRepository
import io.mockk.junit5.MockKExtension
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class AuthorServiceBvaTest @Autowired constructor(
    private val underTest: AuthorServiceImpl,
    private val authorRepository: AuthorRepository
) {
    fun testAuthorEntityA(
        id: Long? = null,
        name: String = "John Doe",
        age: Int = 30,
        description: String = "Some description",
        image: String = "author-image.jpeg"
    ) = AuthorEntity(id, name, age, description, image)

    // CREATE
    @Test
    fun `BVA - create should throw when age is 0`() {
        val author = testAuthorEntityA(age = 0)
        assertThrows<IllegalArgumentException> { underTest.create(author.copy(id = null)) }
    }

    @Test
    fun `BVA - create should succeed when age is 1`() {
        val author = testAuthorEntityA(age = 1)
        val result = underTest.create(author.copy(id = null))
        assertThat(result.age).isEqualTo(1)
    }

    @Test
    fun `BVA - create should succeed when age is 100`() {
        val author = testAuthorEntityA(age = 100)
        val result = underTest.create(author.copy(id = null))
        assertThat(result.age).isEqualTo(100)
    }

    @Test
    fun `BVA - create should throw when age is 121`() {
        val author = testAuthorEntityA(age = 121)
        assertThrows<IllegalArgumentException> { underTest.create(author.copy(id = null)) }
    }

    @Test
    fun `BVA - create should throw when description exceeds 512 characters`() {
        val longDesc = "a".repeat(513)
        val author = testAuthorEntityA(description = longDesc)
        assertThrows<IllegalArgumentException> { underTest.create(author.copy(id = null)) }
    }

    @Test
    fun `BVA - create should succeed when description is exactly 512 characters`() {
        val validDesc = "a".repeat(512)
        val author = testAuthorEntityA(description = validDesc)
        val result = underTest.create(author.copy(id = null))
        assertThat(result.description.length).isEqualTo(512)
    }

    // FULL UPDATE
    @Test
    fun `BVA - fullUpdate should throw when age is 0`() {
        val existing = authorRepository.save(testAuthorEntityA())
        val updated = existing.copy(age = 0)
        assertThrows<IllegalArgumentException> {
            underTest.fullUpdate(existing.id!!, updated)
        }
    }

    @Test
    fun `BVA - fullUpdate should throw when description exceeds 512 characters`() {
        val existing = authorRepository.save(testAuthorEntityA())
        val updated = existing.copy(description = "a".repeat(513))
        assertThrows<IllegalArgumentException> {
            underTest.fullUpdate(existing.id!!, updated)
        }
    }

    // PARTIAL UPDATE
    @Test
    fun `BVA - partialUpdate should throw when age is 0`() {
        val existing = authorRepository.save(testAuthorEntityA())
        val update = AuthorUpdateRequest(age = 0)
        assertThrows<IllegalArgumentException> {
            underTest.partialUpdate(existing.id!!, update)
        }
    }

    @Test
    fun `BVA - partialUpdate should throw when description exceeds 512 characters`() {
        val existing = authorRepository.save(testAuthorEntityA())
        val update = AuthorUpdateRequest(description = "a".repeat(513))
        assertThrows<IllegalArgumentException> {
            underTest.partialUpdate(existing.id!!, update)
        }
    }

    @Test
    fun `BVA - partialUpdate should succeed when age is 1`() {
        val existing = authorRepository.save(testAuthorEntityA())
        val update = AuthorUpdateRequest(age = 1)
        val result = underTest.partialUpdate(existing.id!!, update)
        assertThat(result.age).isEqualTo(1)
    }

    @Test
    fun `BVA - partialUpdate should succeed when description is 512 characters`() {
        val existing = authorRepository.save(testAuthorEntityA())
        val update = AuthorUpdateRequest(description = "a".repeat(512))
        val result = underTest.partialUpdate(existing.id!!, update)
        assertThat(result.description.length).isEqualTo(512)
    }

    // GET / DELETE invalid ID
    @Test
    fun `BVA - get should return null for negative id`() {
        val result = underTest.get(-1)
        assertThat(result).isNull()
    }

    @Test
    fun `BVA - delete should throw for negative id`() {
        assertThrows<IllegalStateException> {
            underTest.delete(-1L)
        }
    }
}

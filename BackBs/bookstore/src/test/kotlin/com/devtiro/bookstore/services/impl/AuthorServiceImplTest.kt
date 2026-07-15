package com.devtiro.bookstore.services.impl

import com.devtiro.bookstore.domain.AuthorUpdateRequest
import com.devtiro.bookstore.domain.entities.AuthorEntity
import com.devtiro.bookstore.repositories.AuthorRepository
import com.devtiro.bookstore.services.observer.AuthorObserver
import com.devtiro.bookstore.services.observer.LoggerObserver
import com.devtiro.bookstore.services.observer.Subject
import com.devtiro.bookstore.testAuthorEntityA
import com.devtiro.bookstore.testAuthorEntityB
import com.devtiro.bookstore.testAuthorUpdateRequestA
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.util.logging.Logger

@SpringBootTest
@Transactional
class AuthorServiceImplTest @Autowired constructor(
    private val underTest: AuthorServiceImpl,
    private val authorRepository: AuthorRepository) {




    @Test
    fun `observedr is notified when an author is created`() {
        val newAuthor = AuthorEntity(id = null, name = "John Doe", age = 45, description = "A famous author", image = "image.jpg")
        val authorObserver: AuthorObserver = mockk(relaxed = true)
        val authorRepository: AuthorRepository = mockk()

        underTest.registerObserver(authorObserver)

        val savedAuthor = newAuthor.copy(id = 1L)
        every { authorRepository.save(newAuthor) } returns savedAuthor

        val createdAuthor = underTest.create(newAuthor)

        verify { authorObserver.update(any()) }
    }
    @Test
    fun `obsxerver is not notified after being removed`() {
        val newAuthor = AuthorEntity(id = null, name = "John Doe", age = 45, description = "A famous author", image = "image.jpg")
        val authorObserver: AuthorObserver = mockk(relaxed = true)
        val authorRepository: AuthorRepository = mockk()

        underTest.registerObserver(authorObserver)
        underTest.removeObserver(authorObserver)  // Heqim observer-in

        val savedAuthor = newAuthor.copy(id = 1L)
        every { authorRepository.save(newAuthor) } returns savedAuthor

        val createdAuthor = underTest.create(newAuthor)

        // Verifikoni që observer-i nuk është njoftuar
        verify(exactly = 0) { authorObserver.update(any()) }
    }
    @Test
    fun `multiple observers are notified when an author is created`() {
        val newAuthor = AuthorEntity(id = null, name = "John Doe", age = 45, description = "A famous author", image = "image.jpg")
        val authorObserver1: AuthorObserver = mockk(relaxed = true)
        val authorObserver2: AuthorObserver = mockk(relaxed = true)
        val authorRepository: AuthorRepository = mockk()

        underTest.registerObserver(authorObserver1)
        underTest.registerObserver(authorObserver2)

        val savedAuthor = newAuthor.copy(id = 1L)
        every { authorRepository.save(newAuthor) } returns savedAuthor

        val createdAuthor = underTest.create(newAuthor)

        // Verifikoni që të dy observer-ët janë njoftuar
        verify { authorObserver1.update(any()) }
        verify { authorObserver2.update(any()) }
    }
    @Test
    fun `observer not registered is not notified`() {
        val newAuthor = AuthorEntity(id = null, name = "John Doe", age = 45, description = "A famous author", image = "image.jpg")
        val authorObserver: AuthorObserver = mockk(relaxed = true)
        val authorRepository: AuthorRepository = mockk()

        // Nuk regjistrojmë observer-in
        val savedAuthor = newAuthor.copy(id = 1L)
        every { authorRepository.save(newAuthor) } returns savedAuthor

        val createdAuthor = underTest.create(newAuthor)

        // Verifikoni që observer-i nuk është njoftuar
        verify(exactly = 0) { authorObserver.update(any()) }
    }
    @Test
    fun `observer is notified when an author is created`() {

        val newAuthor = AuthorEntity(
            id = null, // ID do të caktohet pas ruajtjes
            name = "John Doe",
            age = 45,
            description = "A famous author",
            image = "image.jpg"
        )

        val authorObserver: AuthorObserver = mockk(relaxed = true)

        val authorRepository: AuthorRepository = mockk()

        underTest.registerObserver(authorObserver)

        val savedAuthor = newAuthor.copy(id = 1L)
        every { authorRepository.save(newAuthor) } returns savedAuthor

        val createdAuthor = underTest.create(newAuthor)

        verify { authorObserver.update(any()) }

        println("✅ Observer u njoftua: Autori i ri u krijua - ${createdAuthor.name}, Mosha: ${createdAuthor.age}")
    }

    @Test
    fun `observer is not notified after being removed`() {

        val newAuthor = AuthorEntity(
            id = null, // ID do të caktohet pas ruajtjes
            name = "John Doe",
            age = 45,
            description = "A famous author",
            image = "image.jpg"
        )

        val authorObserver: AuthorObserver = mockk(relaxed = true)

        val authorRepository: AuthorRepository = mockk()

        // Regjistrimi i observer-it
        underTest.registerObserver(authorObserver)

        val savedAuthor = newAuthor.copy(id = 1L)
        every { authorRepository.save(newAuthor) } returns savedAuthor

        // Heqja e observer-it
        underTest.removeObserver(authorObserver)

        // Krijimi i autorit
        val createdAuthor = underTest.create(newAuthor)

        // Verifikimi që observer-i nuk është njoftuar
        verify(exactly = 0) { authorObserver.update(any()) }

        println("✅ Observer nuk është njoftuar pasi është hequr.")
    }

    @Test
    fun `test that save persists the Author in the database`() {
        val savedAuthor = underTest.create(testAuthorEntityA())
        assertThat(savedAuthor.id).isNotNull()

        val recalledAuthor = authorRepository.findByIdOrNull(savedAuthor.id!!)
        assertThat(recalledAuthor).isNotNull()
        assertThat(recalledAuthor!!).isEqualTo(
            testAuthorEntityA(id=savedAuthor.id)
        )
    }

    @Test
    fun `test that an Author with an ID throws an IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            val existingAuthor = testAuthorEntityA(id=999)
            underTest.create(existingAuthor)
        }
    }

    @Test
    fun `test that list returns empty list when no authors in the database`() {
        val result = underTest.list()
        assertThat(result).isEmpty()
    }

    @Test
    fun `test that list returns authors when authors present in the database`() {
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        val expected = listOf(savedAuthor)
        val result = underTest.list()
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test that get returns null when author not present in the database`() {
        val result = underTest.get(999)
        assertThat(result).isNull()
    }

    @Test
    fun `test that get returns author when author is present in the database`() {
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        val result = underTest.get(savedAuthor.id!!)
        assertThat(result).isEqualTo(savedAuthor)
    }

    @Test
    fun `test that full update successfully updates the author in the database`() {
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val existingAuthorId = existingAuthor.id!!
        val updatedAuthor = testAuthorEntityB(id=existingAuthorId)
        val result = underTest.fullUpdate(existingAuthorId, updatedAuthor)
        assertThat(result).isEqualTo(updatedAuthor)

        val retrievedAuthor = authorRepository.findByIdOrNull(existingAuthorId)
        assertThat(retrievedAuthor).isNotNull()
        assertThat(retrievedAuthor).isEqualTo(updatedAuthor)
    }

    @Test
    fun `test that full update Author throws IllegalStateException when Author does not exist in the database`() {
        assertThrows<IllegalStateException> {
            val nonExistingAuthorId = 999L
            val updatedAuthor = testAuthorEntityB(id=nonExistingAuthorId)
            underTest.fullUpdate(nonExistingAuthorId, updatedAuthor)
        }
    }

    @Test
    fun `test that partial update Author throws IllegalStateException when Author does not exist in the database`() {
        assertThrows<IllegalStateException> {
            val nonExistingAuthorId = 999L
            val updateRequest = testAuthorUpdateRequestA(id=nonExistingAuthorId)
            underTest.partialUpdate(nonExistingAuthorId, updateRequest)
        }
    }

    @Test
    fun `test that partial update Author does not update Author when all values are null`() {
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val updatedAuthor = underTest.partialUpdate(existingAuthor.id!!, AuthorUpdateRequest())
        assertThat(updatedAuthor).isEqualTo(existingAuthor)
    }

    @Test
    fun `test that partial update Author updates author name`() {
        val newName = "New Name"
        val existingAuthor = testAuthorEntityA()
        val expectedAuthor = existingAuthor.copy(
            name = newName
        )
        val authorUpdateRequest = AuthorUpdateRequest(
            name = newName
        )
        assertThatAuthorPartialUpdateIsUpdated(
            existingAuthor = existingAuthor,
            expectedAuthor = expectedAuthor,
            authorUpdateRequest = authorUpdateRequest,
        )
    }

    @Test
    fun `test that partial update Author updates author age`() {
        val newAge = 50
        val existingAuthor = testAuthorEntityA()
        val expectedAuthor = existingAuthor.copy(
            age = newAge
        )
        val authorUpdateRequest = AuthorUpdateRequest(
            age = newAge
        )
        assertThatAuthorPartialUpdateIsUpdated(
            existingAuthor = existingAuthor,
            expectedAuthor = expectedAuthor,
            authorUpdateRequest = authorUpdateRequest,
        )
    }

    @Test
    fun `test that partial update Author updates author description`() {
        val newDescription = "A new description"
        val existingAuthor = testAuthorEntityA()
        val expectedAuthor = existingAuthor.copy(
            description = newDescription
        )
        val authorUpdateRequest = AuthorUpdateRequest(
            description = newDescription
        )
        assertThatAuthorPartialUpdateIsUpdated(
            existingAuthor = existingAuthor,
            expectedAuthor = expectedAuthor,
            authorUpdateRequest = authorUpdateRequest,
        )
    }

    @Test
    fun `test that partial update Author updates author image`() {
        val newImage = "new-image.jpeg"
        val existingAuthor = testAuthorEntityA()
        val expectedAuthor = existingAuthor.copy(
            image = newImage
        )
        val authorUpdateRequest = AuthorUpdateRequest(
            image = newImage
        )
        assertThatAuthorPartialUpdateIsUpdated(
            existingAuthor = existingAuthor,
            expectedAuthor = expectedAuthor,
            authorUpdateRequest = authorUpdateRequest,
        )
    }

    private fun assertThatAuthorPartialUpdateIsUpdated(
        existingAuthor: AuthorEntity,
        expectedAuthor: AuthorEntity,
        authorUpdateRequest: AuthorUpdateRequest
    ) {
        // Save an existing Author
        val savedExistingAuthor = authorRepository.save(existingAuthor)
        val existingAuthorsId = savedExistingAuthor.id!!

        // Update the Author
        val updatedAuthor = underTest.partialUpdate(
            existingAuthorsId, authorUpdateRequest)

        // Set up the expected Author
        val expected = expectedAuthor.copy(id=existingAuthorsId)
        assertThat(updatedAuthor).isEqualTo(expected)

        val retrievedAuthor = authorRepository.findByIdOrNull(existingAuthorsId)
        assertThat(retrievedAuthor).isNotNull()
        assertThat(retrievedAuthor).isEqualTo(expected)
    }

    @Test
    fun `test that delete deletes an existing Author in the database`() {
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val existingAuthorsId = existingAuthor.id!!

        underTest.delete(existingAuthorsId)

        assertThat(
            authorRepository.existsById(existingAuthorsId)
        ).isFalse()
    }

    @Test
    fun `test that delete deletes an non-existing Author in the database`() {
        val nonExistingId = 999L

        underTest.delete(nonExistingId)

        assertThat(
            authorRepository.existsById(nonExistingId)
        ).isFalse()
    }

}
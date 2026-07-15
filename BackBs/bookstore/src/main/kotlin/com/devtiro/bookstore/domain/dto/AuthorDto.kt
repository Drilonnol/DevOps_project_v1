package com.devtiro.bookstore.domain.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class AuthorDto(

    val id: Long? = null,

    @field:NotBlank(message = "Name must not be blank")
    @field:Size(min = 1, max = 512, message = "Name must be between 1 and 512 characters")
    val name: String,

    @field:Min(value = 1, message = "Age must be at least 1")
    @field:Max(value = 150, message = "Age must be less than or equal to 150")
    val age: Int,

    @field:NotBlank(message = "Description must not be blank")
    @field:Size(min = 2, max = 512, message = "Description must be between 2 and 512 characters")
    val description: String,

    @field:NotBlank(message = "Image must not be blank")
    @field:Size(min = 2, max = 512, message = "Image must be between 2 and 512 characters")
    val image: String
)

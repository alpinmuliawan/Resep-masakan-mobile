package com.alfin.resepmasakan

data class Recipe(
    val id: String ?= null,
    val name: String ?= null,
    val description: String ?= null,
    val ingredients: String ?= null,
    val steps: String ?= null,
)
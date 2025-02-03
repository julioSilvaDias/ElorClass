package com.example.elorrclass.socketIO.model

data class UpdateUser(
    private val user: String,
    private val name: String,
    private val surname: String,
    private val dni: String,
    private val email: String,
    private val telefono: String,
    private val telefono2: String
)
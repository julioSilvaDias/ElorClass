package com.example.elorrclass.logica.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.elorrclass.logica.entity.Usuario

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * FROM Usuario LIMIT 1")
    suspend fun getLastUser(): Usuario?

    @Query("SELECT * FROM usuario WHERE ultimoInicioSesion = 1 LIMIT 1")
    suspend fun getUltimoUsuarioLogeado(): Usuario?

    @Query("UPDATE usuario SET ultimoInicioSesion = 0")
    suspend fun resetUltimoInicioSesion()

    @Query("UPDATE usuario SET ultimoInicioSesion = 1 WHERE username = :username")
    suspend fun setUltimoInicioSesion(username: String)

}
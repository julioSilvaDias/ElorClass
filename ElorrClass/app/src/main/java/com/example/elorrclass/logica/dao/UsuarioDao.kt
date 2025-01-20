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

}
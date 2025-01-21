package com.example.elorrclass.logica.bbdd

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.elorrclass.logica.dao.UsuarioDao
import com.example.elorrclass.logica.entity.Usuario

@Database(entities = [Usuario::class], version = 1)
abstract class AppDataBase:RoomDatabase() {

    abstract fun UsuarioDao(): UsuarioDao

}
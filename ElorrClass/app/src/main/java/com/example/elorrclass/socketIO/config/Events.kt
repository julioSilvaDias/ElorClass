package com.example.elorrclass.socketIO.config

enum class Events (val value: String){
    ON_LOGIN ("onLogin"),
    ON_LOGIN_ANSWER ("onLoginAnswer"),
    ON_CHANGE_PASSWORD ("onChangePassword"),
    ON_CHANGE_PASSWORD_ANSWER ("onChangePassword"),
    ON_REGISTER ("onRegister"),
    ON_REGISTER_ANSWER ("onRegisterAnswer"),
    ON_GET_USER_ID("onGetUserId"),
    ON_GET_USER_ID_ANSWER("onGetUserIdAnswer"),
    ON_GET_HORARIO("onGetHorario"),
    ON_GET_HORARIO_ANSWER("onGetHorarioAnswer");

}
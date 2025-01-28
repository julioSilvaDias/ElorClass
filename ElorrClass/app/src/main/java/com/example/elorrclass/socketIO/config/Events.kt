package com.example.elorrclass.socketIO.config

enum class Events (val value: String){
    ON_LOGIN ("onLogin"),
    ON_LOGIN_ANSWER ("onLoginAnswer"),
    ON_GET_USER_ID("onGetUserId"),
    ON_GET_USER_ID_ANSWER("onGetUserIdAnswer"),
    ON_GET_HORARIO("onGetHorario"),
    ON_GET_HORARIO_ANSWER("onGetHorarioAnswer"),
    ON_RESET_PASSWORD("resetPassword"),
    ON_RESET_PASSWORD_RESPONSE("resetPasswordResponse")

}
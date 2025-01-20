package com.example.elorrclass.util

class ThemesUtils {

    //Cambio de idioma

   /* fun setLocale (context: Context, languageCode: String){
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)

        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

        val editor = context.getSharedPreferences("appPreferences", Context.MODE_PRIVATE).edit()
        editor.putString("LANGUAGE_KEY", languageCode)
        editor.apply()
    }

    fun getLocale(context: Context): String? {
        val prefs = context.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        return prefs.getString("LANGUAGE_KEY", "en")
    }*/

}
package com.example.mybasesemenovbaturov

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

val supabase = createSupabaseClient(
    supabaseUrl = "https://rnjnvfkfnhmsfligcopx.supabase.co", // Укажите свой URL
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJuam52Zmtmbmhtc2ZsaWdjb3B4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzY3NDc3NzIsImV4cCI6MjA1MjMyMzc3Mn0.lqiGWwojWi-WuzpR6SoSnR8lPRqY3baL2vdo2RIeXck"
) {
    install(Postgrest)
    install(io.github.jan.supabase.auth.Auth)
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginScreen(
                        context = LocalContext.current,
                        onNavigateToRegister = { navController.navigate("register") },
                        onLoginSuccess = { navController.navigate("main_screen") },
                        navController = navController
                    )
                }

                composable("main_screen") {
                    MainScreen(navController = navController) // Передаем navController в MainScreen
                }

                composable("second") {
                    SecondWindow(
                        context = LocalContext.current
                    ) // Переход на экран SecondWindow
                }

                composable("register") {
                    RegisterScreen(
                        context = LocalContext.current,
                        onNavigateToLogin = { navController.popBackStack() },
                        navController = navController
                    )
                }
            }
        }
    }
}

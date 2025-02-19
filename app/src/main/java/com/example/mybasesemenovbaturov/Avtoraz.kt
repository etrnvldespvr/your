package com.example.mybasesemenovbaturov

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    context: Context,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    navController: NavHostController // Добавляем navController
) {
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Инициализация клиента Supabase
    val client = remember {
        createSupabaseClient(
            supabaseUrl = "https://rnjnvfkfnhmsfligcopx.supabase.co", // Укажите свой URL
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJuam52Zmtmbmhtc2ZsaWdjb3B4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzY3NDc3NzIsImV4cCI6MjA1MjMyMzc3Mn0.lqiGWwojWi-WuzpR6SoSnR8lPRqY3baL2vdo2RIeXck"
        ) {
            install(io.github.jan.supabase.auth.Auth)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Электронная почта") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        // Попытка авторизации через Supabase
                        val response = client.auth.signInWith(Email) {
                            this.email = email.text
                            this.password = password.text
                        }

                        // Если все прошло успешно, переходим на экран успеха
                        Toast.makeText(context, "Вход успешен", Toast.LENGTH_SHORT).show()
                        onLoginSuccess() // Переход на экран после успешного входа
                        navController.navigate("main_screen") // Переход на основной экран
                    } catch (e: Exception) {
                        // Ловим исключения, если что-то пошло не так
                        Log.e("Login", "Error during login: ${e.localizedMessage}")
                        snackbarHostState.showSnackbar("Неверный логин или пароль") // Показываем ошибку
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Войти")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Не зарегистрированы? Зарегистрироваться",
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { onNavigateToRegister() } // Переход на экран регистрации
                .padding(8.dp)
        )

        // Показываем Snackbar с ошибкой
        SnackbarHost(hostState = snackbarHostState)
    }
}

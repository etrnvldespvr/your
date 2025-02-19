package com.example.mybasesemenovbaturov

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.launch
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.Auth
@Composable
fun RegisterScreen(
    context: Context,
    onNavigateToLogin: () -> Unit,
    navController: NavHostController
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
                        // Попытка регистрации через Supabase
                        val response = client.auth.signUpWith(Email) {
                            this.email = email.text
                            this.password = password.text
                        }

                        // Если регистрация успешна, выводим сообщение и переходим на экран входа
                        Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                        onNavigateToLogin() // Переход на экран входа
                    } catch (e: Exception) {
                        // Ловим исключения, если что-то пошло не так
                        Log.e("Register", "Error during registration: ${e.localizedMessage}")
                        snackbarHostState.showSnackbar("Ошибка регистрации: ${e.localizedMessage}")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Зарегистрироваться")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                onNavigateToLogin() // Переход на экран входа
            }
        ) {
            Text("Уже есть аккаунт? Войти")
        }

        // Показываем Snackbar с ошибкой
        SnackbarHost(hostState = snackbarHostState)
    }
}

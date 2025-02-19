package com.example.mybasesemenovbaturov

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun MainScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    var tableData by remember { mutableStateOf<List<TableEntry>>(emptyList()) }

    // Запрос данных из Supabase
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val response = supabase.from("products").select().decodeList<TableEntry>()
            tableData = response
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = "Главный экран",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            // Кнопка перехода на второй экран
            Button(
                onClick = { navController.navigate("second") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Добавить товар")
            }

            // Список данных из Supabase
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(tableData) { entry ->
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Название: ${entry.name}", fontWeight = FontWeight.Bold)
                        Text(text = "Описание: ${entry.description}")
                        Text(text = "Цена: ${entry.price}")
                        Divider()
                    }
                }
            }
        }
    }
}

@Serializable
data class TableEntry(
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0
)

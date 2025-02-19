package com.example.mybasesemenovbaturov

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Blue),
                title = {
                    Text(
                        text = "Uvi",
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
            )
        },
        floatingActionButton = {
            AddProductButton(onClick = { navController.navigate("second") })
        }
        ,modifier = Modifier.fillMaxSize()) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            // Кнопка перехода на второй экран
            Button(
                onClick = { navController.navigate("second") },
                modifier = Modifier.padding(16.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                Text("Добавить товар")
            }

            // Список данных из Supabase
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                items(tableData) { entry ->
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "${entry.name}", fontWeight = FontWeight.Bold)
                        Text(text = "${entry.description}")
                        Text(text = "${entry.price}")
                    }
                }
            }
        }
    }
}

@Composable
private fun AddProductButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
        )
    }
}

@Serializable
data class TableEntry(
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0
)

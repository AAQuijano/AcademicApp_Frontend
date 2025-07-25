package com.quijano.acdemicapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quijano.acdemicapp.network.Grade
import com.quijano.acdemicapp.network.ApiService
import com.quijano.acdemicapp.viewmodel.StudentViewModel
import com.quijano.acdemicapp.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val factory = remember {
        ViewModelFactory(
            context = context,
            apiService = ApiService,
            userId = "1",      // ← Extraer del JWT idealmente
            role = "student"
        )
    }
    val viewModel: StudentViewModel = viewModel(factory = factory)

    val grades by viewModel.grades.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Calificaciones") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Salir")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(grades) { grade ->
                GradeCard(grade)
                Divider()
            }
        }
    }
}

@Composable
fun GradeCard(grade: Grade) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Materia: ${grade.subject}", style = MaterialTheme.typography.bodyLarge)
            Text("Nota: ${grade.value}")
        }
    }
}
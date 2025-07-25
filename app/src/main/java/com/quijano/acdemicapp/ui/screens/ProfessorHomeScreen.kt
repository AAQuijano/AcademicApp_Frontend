package com.quijano.acdemicapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quijano.acdemicapp.network.Score
import com.quijano.acdemicapp.network.ApiService
import com.quijano.acdemicapp.viewmodel.ProfessorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessorHomeScreen(
    onCreateScore: () -> Unit,
    onCreateGrade: () -> Unit,
    onLogout: () -> Unit
) {
    val viewModel = remember {
        ProfessorViewModel(ApiService, professorId = "1")
    }

    val scores by viewModel.scores.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel del Profesor") },
                actions = {
                    TextButton(onClick = onLogout) { Text("Salir") }
                }
            )
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = onCreateScore, modifier = Modifier.padding(4.dp)) {
                    Text("+ Score")
                }
                FloatingActionButton(onClick = onCreateGrade, modifier = Modifier.padding(4.dp)) {
                    Text("+ Grade")
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(scores) { score ->
                ScoreCard(score)
                Divider()
            }
        }
    }
}

@Composable
fun ScoreCard(score: Score) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Materia: ${score.subject}", style = MaterialTheme.typography.bodyLarge)
            Text("ID: ${score.id}")
        }
    }
}
package com.quijano.acdemicapp.navigation

import android.util.Base64
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quijano.acdemicapp.data.remote.local.SessionManager
import com.quijano.acdemicapp.ui.screens.*
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val currentRole by sessionManager.userRole.collectAsState(initial = null)

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginSuccess = { token ->
                    coroutineScope.launch {
                        val role = decodeRoleFromToken(token) ?: "unknown"
                        sessionManager.saveUserSession(
                            token = token,
                            userId = "", // puedes extraerlo si el JWT lo tiene
                            role = role,
                            username = ""
                        )

                        when (role) {
                            "student" -> navController.navigate("student_home") {
                                popUpTo("login") { inclusive = true }
                            }
                            "professor" -> navController.navigate("professor_home") {
                                popUpTo("login") { inclusive = true }
                            }
                            else -> navController.navigate("login")
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login")
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                }
            )
        }

        composable("student_home") {
            StudentHomeScreen(
                onLogout = {
                    coroutineScope.launch {
                        sessionManager.clearUserSession()
                        navController.navigate("login") {
                            popUpTo("student_home") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("professor_home") {
            ProfessorHomeScreen(
                onCreateScore = { /* implementar */ },
                onCreateGrade = { /* implementar */ },
                onLogout = {
                    coroutineScope.launch {
                        sessionManager.clearUserSession()
                        navController.navigate("login") {
                            popUpTo("professor_home") { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

/**
 * Decodifica el token JWT y extrae el campo "role" del payload.
 */
fun decodeRoleFromToken(token: String): String? {
    return try {
        val parts = token.split(".")
        if (parts.size < 2) return null
        val payload = parts[1]
        val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
        val json = JSONObject(String(decodedBytes))
        json.getString("role")
    } catch (e: Exception) {
        null
    }
}
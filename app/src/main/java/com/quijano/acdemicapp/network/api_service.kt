package com.quijano.acdemicapp.network

import com.quijano.acdemicapp.data.remote.BASE_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.*

object ApiService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun login(request: LoginRequest): TokenResponse {
        return client.post("$BASE_URL/token") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun register(request: RegisterRequest) {
        client.post("$BASE_URL/usuarios/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun getStudentGrades(studentId: String, token: String): List<Grade> {
        return client.get("$BASE_URL/calificaciones/student/$studentId") {
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
        }.body()
    }

    suspend fun getProfessorScores(professorId: String, token: String): List<Score> {
        return client.get("$BASE_URL/scores/professor/$professorId") {
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
        }.body()
    }

    suspend fun createScore(scoreRequest: ScoreRequest, token: String) {
        client.post("$BASE_URL/scores") {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
            setBody(scoreRequest)
        }
    }

    suspend fun createGrade(gradeRequest: GradeRequest, token: String) {
        client.post("$BASE_URL/calificaciones") {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
            setBody(gradeRequest)
        }
    }
}

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val name_complete: String,
    val name_user: String,
    val email: String,
    val password: String
)

@Serializable
data class TokenResponse(
    val access_token: String,
    val token_type: String
)

@Serializable
data class Grade(
    val id: String,
    val subject: String,
    val type: String,
    val value: Double,
    val comment: String?,
    val studentId: String,
    val professorId: String
)

@Serializable
data class Score(
    val id: String,
    val subject: String,
    val description: String,
    val professorId: String
)

@Serializable
data class ScoreRequest(
    val subject: String,
    val description: String,
    val professorId: String
)

@Serializable
data class GradeRequest(
    val subject: String,
    val type: String,
    val value: Double,
    val comment: String?,
    val studentId: String,
    val professorId: String
)
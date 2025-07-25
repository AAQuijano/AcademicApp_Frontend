package com.quijano.acdemicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quijano.acdemicapp.data.remote.local.JwtDecoder
import com.quijano.acdemicapp.data.remote.local.SessionManager
import com.quijano.acdemicapp.network.ApiService
import com.quijano.acdemicapp.network.LoginRequest
import com.quijano.acdemicapp.network.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthViewModel(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(username, password))
                val decodedToken = JwtDecoder.decode(response.access_token)
                sessionManager.saveSession(
                    token = response.access_token,
                    userId = decodedToken["user_id"].toString(),
                    role = decodedToken["role"].toString()
                )
                _loginState.value = LoginState.Success(response.access_token)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun register(registerRequest: RegisterRequest) {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            try {
                apiService.register(registerRequest)
                _registerState.value = RegisterState.Success
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
        }
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val token: String) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}
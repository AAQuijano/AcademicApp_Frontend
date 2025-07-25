package com.quijano.acdemicapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quijano.acdemicapp.data.remote.local.SessionManager
import com.quijano.acdemicapp.network.ApiService

class ViewModelFactory(
    private val context: Context,
    private val apiService: ApiService,
    private val userId: String,
    private val role: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val sessionManager = SessionManager(context)

        return when {
            modelClass.isAssignableFrom(StudentViewModel::class.java) -> {
                StudentViewModel(apiService, sessionManager, userId) as T
            }
            modelClass.isAssignableFrom(ProfessorViewModel::class.java) -> {
                ProfessorViewModel(apiService, sessionManager, userId) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(apiService, sessionManager) as T
            }
            else -> throw IllegalArgumentException("ViewModel no soportado: " + modelClass.name)
        }
    }
}
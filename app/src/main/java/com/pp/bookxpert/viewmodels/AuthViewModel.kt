package com.pp.bookxpert.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.pp.bookxpert.models.UserEntity
import com.pp.bookxpert.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    var user by mutableStateOf<UserEntity?>(null)
        private set

    fun saveUser(user: FirebaseUser) {
        viewModelScope.launch {
            repo.saveUserToDb(user)
            this@AuthViewModel.user = repo.getUser()
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            user = repo.getUser()
        }
    }
}

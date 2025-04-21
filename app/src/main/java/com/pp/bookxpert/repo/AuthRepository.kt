package com.pp.bookxpert.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pp.bookxpert.daos.UserDao
import com.pp.bookxpert.models.UserEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userDao: UserDao,
) {





    suspend fun saveUserToDb(user: FirebaseUser) {
        val userEntity = UserEntity(
            uid = user.uid,
            name = user.displayName,
            email = user.email,
            photoUrl = user.photoUrl?.toString()
        )


        userDao.insertUser(userEntity)



    }

    suspend fun getUser(): UserEntity? = userDao.getUser()
}

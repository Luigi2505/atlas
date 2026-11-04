package com.equipe.treinoapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessaoManager @Inject constructor() {

    private val _usuarioId = MutableStateFlow<Long?>(null)
    val usuarioId: StateFlow<Long?> = _usuarioId.asStateFlow()

    fun entrar(id: Long) {
        _usuarioId.value = id
    }

    fun sair() {
        _usuarioId.value = null
    }
}

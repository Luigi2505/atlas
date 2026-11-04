package com.equipe.treinoapp.util

import java.security.MessageDigest

fun hashSenha(senha: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(senha.toByteArray(Charsets.UTF_8))
    return bytes.joinToString("") { "%02x".format(it) }
}

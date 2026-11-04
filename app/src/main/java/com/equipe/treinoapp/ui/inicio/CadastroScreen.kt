package com.equipe.treinoapp.ui.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.SeletorDropdown
import com.equipe.treinoapp.util.OBJETIVOS
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(
    onCadastroOk: () -> Unit,
    onIrLogin: () -> Unit,
    onVoltar: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var telefone by rememberSaveable { mutableStateOf("") }
    var objetivo by rememberSaveable { mutableStateOf(OBJETIVOS.first()) }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmarSenha by rememberSaveable { mutableStateOf("") }

    var erroNome by remember { mutableStateOf(false) }
    var erroEmail by remember { mutableStateOf<String?>(null) }
    var erroSenha by remember { mutableStateOf<String?>(null) }
    var erroConfirmacao by remember { mutableStateOf(false) }
    var salvando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { BarraTopo("Criar conta", onVoltar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = {
                    nome = it
                    erroNome = false
                },
                label = { Text("Nome completo") },
                isError = erroNome,
                supportingText = { if (erroNome) Text("Informe o nome") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    erroEmail = null
                },
                label = { Text("E-mail") },
                isError = erroEmail != null,
                supportingText = { erroEmail?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                label = { Text("Telefone (opcional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            SeletorDropdown(
                rotulo = "Objetivo",
                opcoes = OBJETIVOS,
                selecionado = objetivo,
                textoDe = { it },
                onSelecionar = { objetivo = it }
            )

            OutlinedTextField(
                value = senha,
                onValueChange = {
                    senha = it
                    erroSenha = null
                },
                label = { Text("Senha (mínimo 6 caracteres)") },
                isError = erroSenha != null,
                supportingText = { erroSenha?.let { Text(it) } },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = {
                    confirmarSenha = it
                    erroConfirmacao = false
                },
                label = { Text("Confirmar senha") },
                isError = erroConfirmacao,
                supportingText = { if (erroConfirmacao) Text("As senhas não são iguais") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    erroNome = nome.isBlank()
                    erroEmail = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches())
                        "Informe um e-mail válido" else null
                    erroSenha = if (senha.length < 6) "A senha precisa ter pelo menos 6 caracteres" else null
                    erroConfirmacao = senha != confirmarSenha

                    if (!erroNome && erroEmail == null && erroSenha == null && !erroConfirmacao) {
                        salvando = true
                        scope.launch {
                            val criada = viewModel.cadastrar(nome, email, telefone, objetivo, senha)
                            salvando = false
                            if (criada) onCadastroOk() else erroEmail = "Este e-mail já está cadastrado"
                        }
                    }
                },
                enabled = !salvando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Criar conta")
            }

            TextButton(onClick = onIrLogin) {
                Text("Já tem conta? Entrar")
            }
        }
    }
}

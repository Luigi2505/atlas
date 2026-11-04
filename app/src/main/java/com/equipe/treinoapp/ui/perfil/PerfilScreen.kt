package com.equipe.treinoapp.ui.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.SeletorDropdown
import com.equipe.treinoapp.util.OBJETIVOS
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    onVoltar: () -> Unit,
    onSair: () -> Unit,
    viewModel: PerfilViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var telefone by rememberSaveable { mutableStateOf("") }
    var objetivo by rememberSaveable { mutableStateOf(OBJETIVOS.first()) }
    var novaSenha by rememberSaveable { mutableStateOf("") }
    var carregado by rememberSaveable { mutableStateOf(false) }

    var erroNome by remember { mutableStateOf(false) }
    var erroEmail by remember { mutableStateOf<String?>(null) }
    var erroSenha by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.usuario) {
        val usuario = uiState.usuario
        if (!carregado && usuario != null) {
            nome = usuario.nome
            email = usuario.email
            telefone = usuario.telefone
            objetivo = usuario.objetivo
            carregado = true
        }
    }

    Scaffold(
        topBar = { BarraTopo("Meu perfil", onVoltar) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                label = { Text("Telefone") },
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
                value = novaSenha,
                onValueChange = {
                    novaSenha = it
                    erroSenha = null
                },
                label = { Text("Nova senha (deixe em branco para manter)") },
                isError = erroSenha != null,
                supportingText = { erroSenha?.let { Text(it) } },
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
                    erroSenha = if (novaSenha.isNotEmpty() && novaSenha.length < 6)
                        "A senha precisa ter pelo menos 6 caracteres" else null

                    if (!erroNome && erroEmail == null && erroSenha == null) {
                        scope.launch {
                            val salvou = viewModel.salvar(nome, email, telefone, objetivo, novaSenha)
                            if (salvou) {
                                novaSenha = ""
                                snackbarHostState.showSnackbar("Perfil atualizado!")
                            } else {
                                erroEmail = "Este e-mail já pertence a outra conta"
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar alterações")
            }

            OutlinedButton(
                onClick = {
                    viewModel.sair()
                    onSair()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sair da conta", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

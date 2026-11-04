package com.equipe.treinoapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipe.treinoapp.data.model.Usuario
import com.equipe.treinoapp.ui.componentes.BarraTopo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onVoltar: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { BarraTopo("Administração de Perfis", onVoltar) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.usuarios) { usuario ->
                val isLogado = usuario.id == uiState.idUsuarioLogado
                UsuarioItem(
                    usuario = usuario,
                    isLogado = isLogado,
                    onRoleChange = { novaRole ->
                        viewModel.mudarRole(usuario.id, novaRole)
                    }
                )
            }
        }
    }
}

@Composable
fun UsuarioItem(
    usuario: Usuario,
    isLogado: Boolean,
    onRoleChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = usuario.nome, style = MaterialTheme.typography.titleMedium)
                Text(text = usuario.email, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Role: ${usuario.role}", style = MaterialTheme.typography.bodySmall)
            }
            if (!isLogado) {
                var expandido by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expandido = true }) {
                        Text("Mudar Role")
                    }
                    DropdownMenu(
                        expanded = expandido,
                        onDismissRequest = { expandido = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("admin") },
                            onClick = {
                                onRoleChange("admin")
                                expandido = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("usuario") },
                            onClick = {
                                onRoleChange("usuario")
                                expandido = false
                            }
                        )
                    }
                }
            } else {
                Text(text = "(Você)", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

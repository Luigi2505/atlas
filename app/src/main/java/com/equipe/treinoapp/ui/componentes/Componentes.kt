package com.equipe.treinoapp.ui.componentes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.equipe.treinoapp.util.formatarData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraTopo(
    titulo: String,
    onVoltar: (() -> Unit)? = null,
    acoes: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(titulo) },
        navigationIcon = {
            if (onVoltar != null) {
                IconButton(onClick = onVoltar) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                }
            }
        },
        actions = acoes,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun EstadoVazio(texto: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ConfirmarExclusaoDialog(
    mensagem: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Excluir") },
        text = { Text(mensagem) },
        confirmButton = { TextButton(onClick = onConfirmar) { Text("Excluir") } },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    )
}

@Composable
fun <T> SeletorDropdown(
    rotulo: String,
    opcoes: List<T>,
    selecionado: T?,
    textoDe: (T) -> String,
    onSelecionar: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expandido = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (selecionado != null) "$rotulo: ${textoDe(selecionado)}" else "Selecionar $rotulo",
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            opcoes.forEach { opcao ->
                DropdownMenuItem(
                    text = { Text(textoDe(opcao)) },
                    onClick = {
                        onSelecionar(opcao)
                        expandido = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeletorData(
    dataMillis: Long,
    onDataSelecionada: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { mostrarDialogo = true },
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.DateRange, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Data: ${formatarData(dataMillis)}")
    }

    if (mostrarDialogo) {
        val estado = rememberDatePickerState(initialSelectedDateMillis = dataMillis)
        DatePickerDialog(
            onDismissRequest = { mostrarDialogo = false },
            confirmButton = {
                TextButton(onClick = {
                    estado.selectedDateMillis?.let(onDataSelecionada)
                    mostrarDialogo = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = estado)
        }
    }
}

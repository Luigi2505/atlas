package com.equipe.treinoapp.ui.serie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipe.treinoapp.data.model.Serie
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.SeletorData
import com.equipe.treinoapp.ui.componentes.SeletorDropdown
import com.equipe.treinoapp.util.formatarPeso
import com.equipe.treinoapp.util.hojeMillis
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerieFormScreen(
    id: Long,
    onVoltar: () -> Unit,
    viewModel: SerieViewModel = hiltViewModel()
) {
    val nova = id == -1L
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var exercicioId by rememberSaveable { mutableStateOf<Long?>(null) }
    var peso by rememberSaveable { mutableStateOf("") }
    var repeticoes by rememberSaveable { mutableStateOf("") }
    var dataMillis by rememberSaveable { mutableStateOf(hojeMillis()) }
    var carregado by rememberSaveable { mutableStateOf(nova) }
    var erroExercicio by remember { mutableStateOf(false) }
    var erroPeso by remember { mutableStateOf(false) }
    var erroRepeticoes by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        if (!carregado) {
            viewModel.buscarPorId(id)?.let {
                exercicioId = it.exercicioId
                peso = formatarPeso(it.peso)
                repeticoes = it.repeticoes.toString()
                dataMillis = it.dataMillis
            }
            carregado = true
        }
    }

    Scaffold(
        topBar = { BarraTopo(if (nova) "Nova série" else "Editar série", onVoltar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SeletorDropdown(
                rotulo = "Exercício",
                opcoes = uiState.exercicios,
                selecionado = uiState.exercicios.firstOrNull { it.id == exercicioId },
                textoDe = { it.nome },
                onSelecionar = {
                    exercicioId = it.id
                    erroExercicio = false
                }
            )
            if (erroExercicio) {
                Text(
                    "Selecione um exercício",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = peso,
                onValueChange = {
                    peso = it
                    erroPeso = false
                },
                label = { Text("Peso (kg)") },
                isError = erroPeso,
                supportingText = { if (erroPeso) Text("Informe um peso válido") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = repeticoes,
                onValueChange = {
                    repeticoes = it.filter { c -> c.isDigit() }
                    erroRepeticoes = false
                },
                label = { Text("Repetições") },
                isError = erroRepeticoes,
                supportingText = { if (erroRepeticoes) Text("Informe as repetições") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            SeletorData(
                dataMillis = dataMillis,
                onDataSelecionada = { dataMillis = it }
            )

            Button(
                onClick = {
                    val pesoNumero = peso.replace(',', '.').toDoubleOrNull()
                    val repsNumero = repeticoes.toIntOrNull()

                    erroExercicio = exercicioId == null
                    erroPeso = pesoNumero == null || pesoNumero < 0
                    erroRepeticoes = repsNumero == null || repsNumero <= 0

                    if (!erroExercicio && !erroPeso && !erroRepeticoes) {
                        scope.launch {
                            viewModel.salvar(
                                Serie(
                                    id = if (nova) 0 else id,
                                    exercicioId = exercicioId!!,
                                    peso = pesoNumero!!,
                                    repeticoes = repsNumero!!,
                                    dataMillis = dataMillis
                                )
                            )
                            onVoltar()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }
}

package com.equipe.treinoapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipe.treinoapp.data.SessaoManager
import com.equipe.treinoapp.data.repository.ExercicioRepository
import com.equipe.treinoapp.data.repository.MetaRepository
import com.equipe.treinoapp.data.repository.RotinaRepository
import com.equipe.treinoapp.data.repository.SerieRepository
import com.equipe.treinoapp.data.repository.TreinoRepository
import com.equipe.treinoapp.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeUiState(
    val nomeUsuario: String = "",
    val exercicios: Int = 0,
    val rotinas: Int = 0,
    val treinos: Int = 0,
    val series: Int = 0,
    val metasPendentes: Int = 0
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    exercicioRepository: ExercicioRepository,
    rotinaRepository: RotinaRepository,
    treinoRepository: TreinoRepository,
    serieRepository: SerieRepository,
    metaRepository: MetaRepository,
    usuarioRepository: UsuarioRepository,
    sessao: SessaoManager
) : ViewModel() {

    private val contagens = sessao.usuarioId.flatMapLatest { id ->
        if (id == null) flowOf(HomeUiState())
        else combine(
            exercicioRepository.contar(id),
            rotinaRepository.contar(),
            treinoRepository.contar(),
            serieRepository.contar(),
            metaRepository.contarPendentes()
        ) { exercicios, rotinas, treinos, series, metas ->
            HomeUiState(
                exercicios = exercicios,
                rotinas = rotinas,
                treinos = treinos,
                series = series,
                metasPendentes = metas
            )
        }
    }

    private val nomeUsuario = sessao.usuarioId.flatMapLatest { id ->
        if (id == null) flowOf("")
        else usuarioRepository.observarPorId(id).map { it?.nome ?: "" }
    }

    val uiState: StateFlow<HomeUiState> = combine(contagens, nomeUsuario) { estado, nome ->
        estado.copy(nomeUsuario = nome)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )
}

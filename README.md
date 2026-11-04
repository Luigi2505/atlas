# Atlas: aplicativo de treinos (estilo Hevy)

Aplicativo Android feito com **Jetpack Compose + Material 3**, **Navigation Compose**, **Room**, **MVVM (ViewModel + Repository)** e **Hilt**, seguindo o que foi visto nas Aulas 02 a 06.

> **Importante:** o projeto foi escrito e revisado à mão, mas **não foi compilado** (o ambiente onde foi gerado não acessa o Maven/Google/Gradle). Abra no Android Studio (seção 2). Se aparecer algum erro de compilação, ele deve ser pequeno e localizado; copie a mensagem da aba **Build** e peça a correção.

---

## 1. Um CRUD por aluno

Cada integrante é dono de um módulo completo (entidade → DAO → Repository → ViewModel → tela de lista → tela de formulário).

| Aluno | Módulo (CRUD) | Entidade Room | Pasta da interface |
|---|---|---|---|
| **Andrey** | Exercícios (com busca) | `Exercicio` | `ui/exercicio/` |
| **Luigi** | Rotinas | `Rotina` | `ui/rotina/` |
| **Eric** | Treinos realizados | `Treino` | `ui/treino/` |
| **Joao** | Histórico de séries (peso × repetições, volume total) | `Serie` | `ui/serie/` |
| **Guilherme** | Metas + **Home** (resumo e menu do app) | `Meta` | `ui/meta/` e `ui/home/` |

Cada CRUD tem as 4 operações:

| Operação | Onde acontece |
|---|---|
| **Create** | botão **+** na lista → formulário vazio → Salvar |
| **Read** | lista (atualiza sozinha, pois o DAO retorna `Flow`) |
| **Update** | tocar em um item da lista → formulário preenchido → Salvar (nas Metas, também pelo checkbox) |
| **Delete** | ícone de lixeira no item → diálogo de confirmação |

### Relacionamentos (para mostrar o uso do Room além do básico)
- `Treino.rotinaId` → `Rotina` (se a rotina for excluída, o treino fica, sem rotina: `SET_NULL`).
- `Serie.exercicioId` → `Exercicio` (se o exercício for excluído, as séries dele também: `CASCADE`).
- As listas de Treino e Série usam `JOIN` para mostrar o nome da rotina/exercício.

### Fluxo de navegação
```
Home ──► Lista de Exercícios ──► Formulário de Exercício (novo/editar)
     ├─► Lista de Rotinas    ──► Formulário de Rotina
     ├─► Lista de Treinos    ──► Formulário de Treino
     ├─► Histórico de séries ──► Formulário de Série
     └─► Lista de Metas      ──► Formulário de Meta
```
Total: **15 telas**: Boas-vindas, Login, Cadastro, Perfil, Home, 5 listas e 5 formulários, todas ligadas pelo `NavHost` em `ui/navigation/AppNavigation.kt`. Os formulários recebem o `id` pela rota (`-1` = novo).

### Acesso do aluno (início, login, cadastro e perfil)
```
Boas-vindas (nome da academia) ──► Entrar ──► Home
                               └─► Criar conta ──► Home
Home ──► ícone de perfil ──► Meu perfil (editar dados, trocar senha, sair)
```
- O nome da academia fica em **uma constante** (`NOME_ACADEMIA` em `util/Constantes.kt`): troque ali e muda em todas as telas.
- Entidade `Usuario` no Room (e-mail único). A senha é guardada como **hash SHA-256**, nunca em texto puro.
- `SessaoManager` (@Singleton do Hilt) guarda quem está logado, na memória: ao fechar o app, é preciso entrar de novo.
- **Conta de teste:** `aluno@academia.com` / `123456`.

---

## 2. Como abrir e rodar (3 passos)

1. **Descompacte** o `TreinoApp.zip`.
2. No Android Studio: **File → Open** (ou "Open" na tela inicial) e escolha a pasta **`TreinoApp`** (a que tem o arquivo `settings.gradle.kts`). Clique em **Trust Project** se perguntar.
3. Espere o **Sync** do Gradle terminar (barra embaixo; na primeira vez baixa o Gradle e as bibliotecas, então precisa de internet e pode levar alguns minutos). Escolha um emulador (ou celular) no topo e clique em **Run ▶**.

Não precisa criar projeto, copiar arquivo nem editar Gradle: já está tudo configurado.

### Se o Android Studio pedir algo
- **"Install missing SDK Platform 37"** (ou parecido): clique no link azul para instalar e depois **Sync** de novo. O projeto usa `compileSdk = 37`.
- **"Incompatible version of the Android Gradle plugin"**: o seu Android Studio é mais antigo que o projeto. O projeto usa o AGP 9.2, que funciona do Android Studio Quail 1 (2026.1.1) em diante. Atualize em **Help → Check for Updates** ou me diga qual versão ele aceita.
- **Emulador:** **Tools → Device Manager → Create Device** (ex.: Pixel 7, imagem API 34 ou maior).

---

## 3. Versões usadas (`gradle/libs.versions.toml`)

Consultadas em fontes oficiais em **03/11/2026**. Foram escolhidas para funcionar juntas segundo as notas de cada biblioteca, mas **não foram compiladas neste ambiente**.

| Item | Versão |
|---|---|
| Android Gradle Plugin | 9.2.0 (Gradle 9.4.1, JDK 17) |
| Kotlin | 2.4.21 |
| KSP | 2.3.11 |
| Compose BOM | 2026.09.00 |
| Room | 2.8.5 |
| Navigation Compose | 2.10.2 |
| Hilt (Dagger) | 2.60.1 |
| androidx.hilt (`hiltViewModel`) | 1.4.0 |
| Lifecycle / Activity | 2.11.0 / 1.13.0 |

Observações:
- Com o AGP 9 o Kotlin já vem embutido, por isso **não existe** o plugin `org.jetbrains.kotlin.android` no Gradle.
- **`hiltViewModel()`** vem de `androidx.hilt:hilt-lifecycle-viewmodel-compose` (pacote `androidx.hilt.lifecycle.viewmodel.compose`).
- Os slides da Aula 06 mostram Hilt `2.47`, que é antigo. A estrutura é a mesma (`ksp "...hilt-compiler"`), só mudam os números.

---

## 4. Onde cada aula aparece no código

| Aula | Conteúdo do slide | Onde está no projeto |
|---|---|---|
| **02** Jetpack Compose | `@Composable`, `setContent`, `Column`, `Row`, `Modifier.padding`, `Spacer`, cores | Todas as telas em `ui/**`; `MainActivity.kt` (`setContent`) |
| **03** Kotlin | `val`/`var`, `data class`, templates de string (`"$x"`), `if/else`, `when`, listas e `forEach`, null safety (`?.`, `?:`), funções e lambdas | `data/model/*.kt` (data classes), `util/*.kt`, ViewModels (`filter`, `map`), formulários (`?.let`, `toIntOrNull()`) |
| **04** Layouts com Material | Template Empty Activity, `Scaffold`, `TopAppBar`, `Card`, botões, `FloatingActionButton`, seletor de data, diálogos, navegação | `ui/componentes/Componentes.kt` (barra, dropdown, DatePicker, diálogo); `ui/home/HomeScreen.kt`; telas de lista/formulário |
| **05** Arquitetura MVVM | View → ViewModel → Repository → banco; `Flow`; fluxo de dados unidirecional (UDF); fonte única da verdade (SSOT); lab MyTracker L03→L04 | `data/repository/*`, `ui/*/XxxViewModel.kt` (`uiState: StateFlow`), telas com `collectAsStateWithLifecycle()` |
| **06** Injeção de dependências | `@HiltAndroidApp`, `@AndroidEntryPoint`, `@Module` + `@Provides` + `@Singleton`, `@HiltViewModel`, `@Inject constructor` (Repository) | `TreinoApplication.kt`, `MainActivity.kt`, `di/AppModule.kt`, ViewModels, Repositories |

**Diferença em relação ao slide da Aula 06:** o slide põe o `@Module` dentro da classe `Application` e usa `AppCompatActivity`/`Fragment`. Aqui usamos Compose (uma `ComponentActivity` e `hiltViewModel()`), e o módulo fica numa classe separada (`AppModule`), que é a prática recomendada.

---

## 5. Como o app atende aos critérios de avaliação

| Critério | Peso | Como é atendido | Arquivos para mostrar |
|---|---|---|---|
| **R1** Interface com Compose e navegação funcional | 4,0 | 11 telas 100% Compose + Material 3 (`Scaffold`, `TopAppBar`, `Card`, `LazyColumn`, FAB, `DatePicker`, diálogos). Navegação com `NavHost`, rotas com argumento e botão voltar | `ui/navigation/AppNavigation.kt`, `ui/**` |
| **R2** CRUD persistente com Room | 3,0 | 5 `@Entity`, 5 `@Dao`, `@Database`; Create/Read/Update/Delete em cada módulo; dados continuam após fechar o app | `data/model`, `data/dao`, `data/AppDatabase.kt` |
| **R3** Arquitetura MVVM e Hilt | 3,0 | A tela nunca acessa o DAO: `Tela → ViewModel → Repository → DAO`. Estado em `StateFlow`. Hilt injeta DAOs, Repositories e ViewModels | `ui/*/XxxViewModel.kt`, `data/repository`, `di/AppModule.kt`, `TreinoApplication.kt` |

---

## 6. Roteiro para explicar o código (cada aluno)

Cada um deve conseguir explicar, abrindo o código do **seu** módulo:

1. **Hilt:** quais anotações aparecem (`@HiltViewModel`, `@Inject constructor` no ViewModel e no Repository; `@Provides` do DAO em `AppModule`; `@HiltAndroidApp` e `@AndroidEntryPoint`) e o que cada uma faz.
2. **Fluxo dos dados:** do banco até a tela. `@Query` retorna `Flow` → Repository repassa → ViewModel transforma em `uiState: StateFlow` → a tela lê com `collectAsStateWithLifecycle()` → quando o banco muda, a lista é redesenhada sozinha.
3. **Recomposição e estado:** a tela é função `@Composable` que desenha o `uiState`; o que o usuário digita fica em `rememberSaveable` (sobrevive à rotação); eventos (salvar, excluir) vão para o ViewModel.

Perguntas que a banca pode fazer:
- *Por que a tela não chama o DAO direto?* Separação de responsabilidades (slide da Aula 05): a tela cuida só da interface; o Repository é a única porta de acesso aos dados.
- *Por que `Flow` e não `List` simples?* Para a lista se atualizar sozinha quando o banco muda (fluxo unidirecional).
- *O que o `@Singleton` faz em `provideBanco`?* Garante uma única instância do banco no app inteiro.
- *Por que `salvar` é `suspend` e é chamado em `scope.launch`?* Gravar no banco não pode travar a interface; a tela só volta depois que terminou de salvar.
- *Por que as datas ficam em UTC?* O `DatePicker` do Material 3 devolve a meia-noite em UTC; guardando e mostrando em UTC, o dia não "volta" por causa do fuso.

---

## 7. Teste rápido (checklist para rodar no emulador)

- [ ] O app abre na Home mostrando o resumo (já vêm 10 exercícios, 1 rotina e 1 meta de exemplo).
- [ ] **Exercícios:** criar, buscar pelo nome, editar, excluir.
- [ ] **Rotinas:** criar "Pull Day / Quarta", editar o dia, excluir.
- [ ] **Treinos:** registrar um treino escolhendo a rotina, a data no calendário e a duração; editar; excluir. Excluir a rotina e conferir que o treino aparece como "Treino sem rotina".
- [ ] **Série:** registrar "Supino reto, 80 kg × 10"; conferir o volume total (800 kg); editar; excluir.
- [ ] **Excluir um exercício** que tem séries: as séries somem junto (o diálogo avisa).
- [ ] **Metas:** marcar o checkbox na lista; abrir e editar; excluir.
- [ ] **Girar a tela** no meio de um formulário: o texto digitado continua.
- [ ] **Fechar e reabrir o app:** os dados continuam.
- [ ] A Home atualiza os números depois de cada cadastro.

> Os dados de exemplo são criados só na primeira vez. Para recriar, desinstale o app do emulador.

---

## 8. Observações

- **Escopo simplificado de propósito.** Em relação ao plano anterior da equipe (cronômetro de descanso, calendário mensal próprio, execução de treino ao vivo), esta versão é mais simples: cada aluno tem um CRUD completo e direto, como pede o enunciado. O documento antigo (`Plano_de_Desenvolvimento...md`) e a `Especificacao_Tecnica_para_IA.md` descrevem uma versão maior e **não correspondem** a este projeto; use este README como referência.
- **Labs do devlabs:** os 4 links (L04 ViewModel/Repository, L05 Hilt, L06 Firebase/Firestore e MyTracker Navigation + Room) não abriram na hora de gerar o projeto. A estrutura seguiu os slides das Aulas 05 e 06, que mostram a mesma arquitetura (MyTracker L03 → L04). Se os labs tiverem convenções específicas (nomes, pacotes), vale conferir.
- **Room, não Firebase:** o enunciado permite os dois. Escolhemos Room, que é o que as Aulas 05 e 06 usam.
- **Limites propositais:** sem login, sem sincronização, sem gráficos, sem testes automatizados.

---

## 9. Estrutura de pastas

```
app/src/main/java/com/equipe/treinoapp/
├── TreinoApplication.kt          (@HiltAndroidApp)
├── MainActivity.kt               (@AndroidEntryPoint + setContent)
├── di/
│   └── AppModule.kt              (@Module: banco, DAOs, dados de exemplo)
├── data/
│   ├── AppDatabase.kt            (@Database)
│   ├── model/                    (Exercicio, Rotina, Treino, Serie, Meta)
│   ├── dao/                      (um DAO por entidade)
│   └── repository/               (um Repository por entidade)
├── util/                         (Constantes.kt, DataUtil.kt)
└── ui/
    ├── navigation/AppNavigation.kt
    ├── theme/Theme.kt
    ├── componentes/Componentes.kt   (barra, dropdown, DatePicker, diálogo)
    ├── home/        HomeScreen, HomeViewModel                       (Guilherme)
    ├── meta/        MetaViewModel, MetaListScreen, MetaFormScreen   (Guilherme)
    ├── rotina/      RotinaViewModel, RotinaListScreen, RotinaFormScreen   (Luigi)
    ├── treino/      TreinoViewModel, TreinoListScreen, TreinoFormScreen   (Eric)
    ├── serie/       SerieViewModel, SerieListScreen, SerieFormScreen      (Joao)
    └── exercicio/   ExercicioViewModel, ExercicioListScreen, ExercicioFormScreen (Andrey)
```

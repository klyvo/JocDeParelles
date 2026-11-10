# Joc de les Parelles - Arquitectura MVVM amb Jetpack Compose

Aquest projecte consisteix en la implementació del tradicional **Joc de les Parelles** (Memory Game) per a Android utilitzant **Kotlin**, **Jetpack Compose** i l'arquitectura **MVVM (Model-View-ViewModel)**.

---

## Secció 1: Anàlisi i documentació de l'arquitectura MVVM

### 1.1 Components de l'arquitectura MVVM
L'arquitectura **MVVM (Model-View-ViewModel)** organitza el codi separat en tres capes principals amb responsabilitats ben definides:

*   **Model**: Encapsula les dades de l'aplicació i la lògica de negoci pura. En el nostre projecte, representa l'estat de les cartes (`CardState`) i les regles del joc de les parelles (`ModelGame`). No té cap dependència de la interfície d'usuari (UI) ni del framework d'Android.
*   **View (Vista)**: És la interfície visual desenvolupada amb **Jetpack Compose** (`GameScreen.kt` i `MainActivity.kt`). La seva única funció és dibuixar l'estat actual que li proporciona el ViewModel i capturar les accions de l'usuari (clics a les cartes o botons).
*   **ViewModel**: Actua com a intermediari entre el Model i la Vista (`GameViewModel.kt`). Manté l'estat reactiu observable per la Vista i exposa funcions per gestionar els esdeveniments de l'usuari, delegant les actualitzacions al Model.

---

### 1.2 Comparativa: MVVMSimple vs. MVVM-separatModel

| Característica | MVVMSimple (Model fusionat al ViewModel) | MVVM-separatModel (Model separat) |
| :--- | :--- | :--- |
| **Ubicació de la Lògica** | La lògica del joc i l'estat resideixen directament dins de la classe `ViewModel`. | La lògica i les dades estan completament encapsulades en classes `Model` independents (`ModelGame`). |
| **Principi SRP (Single Responsibility)** | Vulnerat: El ViewModel gestiona tant la UI com les regles del joc. | Complert: El Model gestiona la lògica de negoci i el ViewModel gestiona l'estat de la UI. |
| **Testabilitat (Unit Testing)** | Complicada: Cal instanciar el ViewModel i dependre de components del cicle de vida. | Excel·lent: Es poden fer proves unitàries del `ModelGame` de manera 100% aïllada i ràpida. |
| **Mantenibilitat** | Baixa a mesura que el projecte creix (fitxers `ViewModel` molt grans). | Alta: Codi net, modular i fàcilment ampliable. |
| **Complexitat Inicial** | Menys fitxers inicials, ràpid per a prototips petits. | Requereix crear l'estructura de classes per al Model des de l'inici. |

> **Conclusió**: En aquest projecte hem optat per **MVVM-separatModel** per garantir una clara separació de responsabilitats, alta reusabilitat i facilitat de manteniment.

---

## Secció 2: Explicació de l'arquitectura utilitzada en el Joc de les Parelles

### 2.1 Funcionament del Joc
1. Es genera un tauler de **6 cartes** (3 parelles d'imatges de personatges barrejades aleatòriament).
2. L'usuari pot seleccionar fins a un màxim de **2 cartes** fent-hi clic.
3. Quan hi ha 2 cartes destapades, s'habilita el botó **Validar**.
4. En prémer **Validar**:
   * Si les dues cartes són iguals, es queden trobades i visibles permanentment (`isMatched = true`).
   * Si són diferents, es tornen a tapar (`isFaceUp = false`).
5. Quan totes 6 cartes estan trobades, es mostra el missatge de victòria **"¡Has guanyat! 🎉"**.
6. En qualsevol moment es pot prémer el botó **Reiniciar** per començar una partida nova amb les cartes barrejades de nou.

---

### 2.2 Estructura del Codi i Arquitectura

```
org.proven.jocparelles
├── ModelGame.kt        <-- [MODEL] CardState i ModelGame (Lògica del joc i estat de les cartes)
├── GameViewModel.kt    <-- [VIEWMODEL] Manteniment de l'estat mutableStateOf i gestió d'esdeveniments
├── GameScreen.kt       <-- [VIEW] Interfície d'usuari en Jetpack Compose (Grid, Cartes, Botons)
└── MainActivity.kt     <-- [VIEW CONTENIDOR] Puntuació d'entrada i inicialització amb remember
```

#### Diagrama de Flux d'Arquitectura:
```
┌──────────────────────────────────────────────┐
│                  GameScreen                  │
│       (Jetpack Compose UI Layout)            │
└──────────────────────┬───────────────────────┘
                       │
             Clic en Carta / Validar
                       │
                       ▼
┌──────────────────────────────────────────────┐
│                 GameViewModel                │
│    model by mutableStateOf(ModelGame())      │
└──────────────────────┬───────────────────────┘
                       │
              Crida a selectCard() / validate()
                       │
                       ▼
┌──────────────────────────────────────────────┐
│                  ModelGame                   │
│ (Llista de CardState, validació i resultat)  │
└──────────────────────────────────────────────┘
```

---

## Secció 3: Conceptes i Mètodes Apresos

Durant el desenvolupament d'aquesta aplicació s'han utilitzat i après els següents mètodes, estructures i conceptes clau:

1. **`mutableStateOf()`**:
   Mecanisme de Jetpack Compose per crear un estat observable. Quan la propietat `model` canvia al `GameViewModel`, Compose recomposa (redibuixa) automàticament els elements de la UI afectats.

2. **`remember { ... }`**:
   Utilitzat a `MainActivity.kt` per preservar la mateixa instància de `GameViewModel` a través de totes les recomposicions de Compose, evitant reiniciar el joc en fer qualsevol acció.

3. **`LazyVerticalGrid` & `GridCells.Fixed(3)`**:
   Component de layout de Jetpack Compose per distribuir les 6 cartes en una estructura de graella de 3 columnes i 2 files de manera eficient.

4. **`.copy()`**:
   Mètode natriu de les `data class` de Kotlin utilitzat per modificar immutablement l'estat de les cartes i del joc (`card.copy(isFaceUp = true)`), afavorint la programació funcional.

5. **`.shuffled()`**:
   Funció de les col·leccions de Kotlin utilitzada per barrejar aleatòriament l'ordre de les imatges al `ModelGame` cada cop que s'inicia o es reinicia una partida.

6. **`itemsIndexed`**:
   Funció utilitzada dins de `LazyVerticalGrid` per iterar sobre la llista de cartes tenint accés tan al seu índex posicional com a la informació de l'objecte `CardState`.

7. **Modifier `.aspectRatio(0.75f)` & `.clickable(enabled = ...)`**:
   Modificadors visual de Compose utilitzats per mantenir una proporció d'aspecte de carta vertical a la graella i per desactivar els clics sobre cartes que ja han estat trobades.

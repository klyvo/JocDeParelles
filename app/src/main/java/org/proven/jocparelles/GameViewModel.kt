package org.proven.jocparelles

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Capa de VIEWMODEL: Intermediari entre el Model (lògica/dades) i la Vista (Jetpack Compose).
 * Manté l'estat de la interfície d'usuari i gestiona els esdeveniments (accions de l'usuari).
 */
class GameViewModel : ViewModel() {

    /**
     * Estat del joc mantingut amb mutableStateOf per notificar automàticament a la Vista quan canvia.
     */
    var model by mutableStateOf(ModelGame())
        private set

    /**
     * Esdeveniment: L'usuari fa clic sobre una carta.
     */
    fun onCardClick(index: Int) {
        model = model.selectCard(index)
    }

    /**
     * Esdeveniment: L'usuari prem el botó "Validar".
     */
    fun onValidateClick() {
        model = model.validate()
    }

    /**
     * Esdeveniment: L'usuari prem el botó "Reiniciar".
     */
    fun onRestartClick() {
        model = ModelGame()
    }
}

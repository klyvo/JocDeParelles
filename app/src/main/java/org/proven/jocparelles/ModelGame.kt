package org.proven.jocparelles

/**
 * Capa de MODEL: Representa l'estat d'una carta individual en el joc.
 *
 * @property id Identificador únic de la carta.
 * @property imageResId Identificador del recurs d'imatge (R.drawable).
 * @property isFaceUp Indica si la carta està destapada (boca amunt).
 * @property isMatched Indica si la carta ja forma part d'una parella trobada.
 */
data class CardState(
    val id: Int,
    val imageResId: Int,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false
)

/**
 * Capa de MODEL: Classe immutable que conté la lògica de negoci i l'estat del joc de les parelles.
 *
 * @property cards Llista amb les 6 cartes del joc (3 parelles).
 * @property selectedIndices Índexs de les cartes seleccionades actualment (màxim 2).
 * @property isGameOver Indica si s'han trobat totes les parelles i el joc ha finalitzat.
 */
data class ModelGame(
    val cards: List<CardState> = createInitialCards(),
    val selectedIndices: List<Int> = emptyList(),
    val isGameOver: Boolean = false
) {
    companion object {
        /**
         * Inicialitza les 6 cartes (3 imatges duplicades) de manera aleatòria (shuffled).
         */
        private fun createInitialCards(): List<CardState> {
            val images = listOf(
                R.drawable.chiquito,
                R.drawable.eugenio,
                R.drawable.faemino
            )
            // Dupliquem les imatges per fer 3 parelles (6 cartes) i les barregem
            return (images + images).shuffled().mapIndexed { index, resId ->
                CardState(id = index, imageResId = resId)
            }
        }
    }

    /**
     * Selecciona una carta si hi ha menys de 2 cartes seleccionades i la carta no està destapada/encertada.
     */
    fun selectCard(index: Int): ModelGame {
        if (selectedIndices.size >= 2 || cards[index].isMatched || cards[index].isFaceUp) {
            return this
        }
        val newCards = cards.toMutableList()
        newCards[index] = newCards[index].copy(isFaceUp = true)
        return copy(
            cards = newCards,
            selectedIndices = selectedIndices + index
        )
    }

    /**
     * Valida les 2 cartes seleccionades. Si són iguals, es queden trobades (isMatched).
     * Si són diferents, es tornen a tapar (isFaceUp = false).
     */
    fun validate(): ModelGame {
        if (selectedIndices.size != 2) return this

        val idx1 = selectedIndices[0]
        val idx2 = selectedIndices[1]
        val newCards = cards.toMutableList()

        if (cards[idx1].imageResId == cards[idx2].imageResId) {
            // Parella encertada: es queden visibles per sempre
            newCards[idx1] = newCards[idx1].copy(isMatched = true)
            newCards[idx2] = newCards[idx2].copy(isMatched = true)
        } else {
            // Parella incorrecta: es tornen a tapar
            newCards[idx1] = newCards[idx1].copy(isFaceUp = false)
            newCards[idx2] = newCards[idx2].copy(isFaceUp = false)
        }

        val gameOver = newCards.all { it.isMatched }

        return copy(
            cards = newCards,
            selectedIndices = emptyList(),
            isGameOver = gameOver
        )
    }
}

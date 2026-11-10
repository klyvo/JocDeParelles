package org.proven.jocparelles

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * [VIEW / VISTA]
 * Interfície d'usuari en Jetpack Compose.
 * Mostra l'estat actual i reacciona a les accions de l'usuari cridant el ViewModel.
 */
@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier,
) {
    // Obtenim l'estat del model
    val model = gameViewModel.model

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Joc de les Parelles",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Indicador d'estat
        if (model.isGameOver) {
            Text(
                text = "¡Has guanyat! 🎉",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(text = "Cartes seleccionades: ${model.selectedIndices.size} / 2")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Graella de 6 cartes (3 columnes x 2 files)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(model.cards) { index, card ->
                val imageRes = if (card.isFaceUp || card.isMatched) {
                    card.imageResId
                } else {
                    R.drawable.back
                }

                Card(
                    modifier = Modifier
                        .aspectRatio(0.75f)
                        .clickable(enabled = !card.isMatched) {
                            gameViewModel.onCardClick(index)
                        }
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botons d'acció
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { gameViewModel.onValidateClick() },
                enabled = model.selectedIndices.size == 2,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Validar")
            }

            Button(
                onClick = { gameViewModel.onRestartClick() },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Reiniciar")
            }
        }
    }
}

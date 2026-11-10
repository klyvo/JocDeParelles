package org.proven.jocparelles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.proven.jocparelles.ui.theme.JocParellesTheme

/**
 * [VIEW / CONTENIDOR PRINCIPAL]
 * Activity principal d'Android. Instancia el ViewModel i mostra la Vista (GameScreen).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JocParellesTheme {
                // Instanciem el ViewModel preservant-lo amb remember
                val gameViewModel = remember { GameViewModel() }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(
                        gameViewModel = gameViewModel,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

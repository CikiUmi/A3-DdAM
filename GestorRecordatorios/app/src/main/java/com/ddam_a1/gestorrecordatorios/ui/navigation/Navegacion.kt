package com.ddam_a1.gestorrecordatorios.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ddam_a1.gestorrecordatorios.ui.screens.Editar
import com.ddam_a1.gestorrecordatorios.ui.screens.Main
import com.ddam_a1.gestorrecordatorios.ui.screens.Papelera

@Composable
fun RecordatoriosNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            Main()
        }

        composable("editar") {
            Editar()
        }

        composable("papelera") {
            Papelera()
        }
    }
}



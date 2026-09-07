package com.ddam_a1.gestorrecordatorios.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ddam_a1.gestorrecordatorios.ui.screens.Editar
import com.ddam_a1.gestorrecordatorios.ui.screens.Main
import com.ddam_a1.gestorrecordatorios.ui.screens.Papelera
import com.ddam_a1.gestorrecordatorios.viewmodel.RecordatoriosViewModel

@Composable
fun RecordatoriosNavHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val vm: RecordatoriosViewModel = hiltViewModel()

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



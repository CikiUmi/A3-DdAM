package com.ddam_a1.gestorrecordatorios.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ddam_a1.gestorrecordatorios.modelClasses.Recordatorio
import com.ddam_a1.gestorrecordatorios.ui.components.AccionSwipe
import com.ddam_a1.gestorrecordatorios.ui.components.EstadoCard
import com.ddam_a1.gestorrecordatorios.ui.theme.GestorRecordatoriosTheme

/**
 * La bandeja de entrada.
 *
 * Fijate en lo que NO recibe: el ViewModel. Solo pide una lista, unos margenes y
 * unas funciones. Por eso se puede previsualizar con datos falsos y probar sin
 * emulador. Quien conoce al ViewModel es el NavHost.
 */
@Composable
fun Main(
    recordatorios: List<Recordatorio>,
    margenes: Margenes,
    onMoverAPapelera: (String) -> Unit,
    onEditar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ListaDeRecordatorios(
        titulo = "Mis Recordatorios",
        recordatorios = recordatorios,
        margenes = margenes,
        accion = AccionSwipe.BORRAR,
        estadoCard = EstadoCard.NORMAL,
        onAccion = onMoverAPapelera,
        onEditar = onEditar,
        mensajeVacio = "No tienes recordatorios.\nToca + para crear el primero.",
        modifier = modifier
    )
}

private const val FONDO = 0xFFEAE2D4

@Preview(name = "Bandeja", showBackground = true, backgroundColor = FONDO, widthDp = 412, heightDp = 720)
@Composable
private fun MainPreview() {
    GestorRecordatoriosTheme {
        Main(recordatoriosDeEjemplo(), margenesPara(412.dp), onMoverAPapelera = {}, onEditar = {})
    }
}

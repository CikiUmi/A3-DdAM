package com.ddam_a1.gestorrecordatorios.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ddam_a1.gestorrecordatorios.R
import com.ddam_a1.gestorrecordatorios.ui.theme.GestorRecordatoriosTheme

/**
 * Los DESTINOS de la app: los lugares donde el usuario puede estar.
 *
 * "Nuevo" NO está aquí porque no es un destino, es una ACCIÓN. En tu Figma se
 * dibuja aparte: hasta abajo del rail, y dentro de la muesca en la barra de teléfono.
 */
enum class DestinoNav { BANDEJA, PAPELERA }

// ============================================================
//  MEDIDAS — medidas sobre tu Figma
//
//  Referencia (rail de 124 x 1024 en el archivo):
//    centro del hamburguesa ....  65
//    centro de bandeja .........  161
//    centro de papelera ........  267
//  Los valores de abajo reproducen esos centros en 68 / 164 / 260.
// ============================================================

private val RAIL_ANCHO = 124.dp
private val RAIL_PADDING_ARRIBA = 32.dp
private val RAIL_PADDING_ABAJO = 32.dp

internal val ITEM_TAM = 72.dp            // la caja completa de cada icono
internal val ITEM_RADIO = 18.dp
internal val ICONO_TAM = 32.dp

private val SEPARACION_TRAS_MENU = 24.dp
private val SEPARACION_ENTRE_DESTINOS = 24.dp

/**
 * Sombras de las barras. Las barras de navegación flotan por ENCIMA del contenido,
 * así que llevan más elevación que una tarjeta: es lo que le dice al ojo
 * "esto no se va con el scroll, está en otra capa".
 */
private val ELEVACION_RAIL = 8.dp
private val ELEVACION_BARRA = 12.dp

private val BARRA_ALTO = 88.dp
private val FAB_TAM = 64.dp
private val MUESCA_TAM = 88.dp

// ============================================================
//  COLORES — sacados de tu Figma, midiendo los pixeles
//
//  Y aquí está lo bonito: los cuatro colores de tu diseño SÍ salen de tu tema.
//  Ninguno está escrito a mano.
//
//    fondo del rail ........... #354D2E = onTertiaryContainer
//    icono normal ............. #FFFFFF = onTertiary
//    indicador seleccionado ... #C2B283 = primaryContainer AL 70%
//    icono seleccionado ....... #54452A = onSecondaryContainer
//    botón "nuevo" ............ #FFF8F3 = surface
//
//  Lo del 70% lo verifiqué con la fórmula de mezcla: primaryContainer (#FFDEA7)
//  al 70% sobre el verde da exactamente #C2B283, con los tres canales de acuerdo.
//  Por eso el indicador NO es un color plano: es transparencia, como me dijiste.
// ============================================================

/** Opacidad del indicador de seleccionado. Tu Figma usa 70%. */
private const val ALPHA_SELECCIONADO = 0.70f

@Composable
private fun colorFondoRail() = MaterialTheme.colorScheme.onTertiaryContainer

@Composable
private fun colorIndicador() =
    MaterialTheme.colorScheme.primaryContainer.copy(alpha = ALPHA_SELECCIONADO)

// ============================================================
//  RAIL LATERAL  — pantallas medianas y tablet
// ============================================================

/**
 * Barra lateral. Corresponde a tu variante "Barra Lateral / default".
 *
 * Construida a mano con un `Column`, no con el `NavigationRail` de Material:
 * ese trae sus propias medidas y píldoras ovaladas que no coinciden con tu diseño.
 */
@Composable
fun RecordatoriosNavRail(
    destinoActual: DestinoNav,
    onDestino: (DestinoNav) -> Unit,
    onMenu: () -> Unit,
    onNuevo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
            .width(RAIL_ANCHO)
            .shadow(ELEVACION_RAIL)
            .background(colorFondoRail())
            .padding(top = RAIL_PADDING_ARRIBA, bottom = RAIL_PADDING_ABAJO)
    ) {
        // ---- El control ----
        ItemRail(
            icono = R.drawable.ic_menu,
            descripcion = "Abrir menú",
            seleccionado = false,
            onClick = onMenu
        )

        Spacer(Modifier.height(SEPARACION_TRAS_MENU))

        // ---- Los destinos ----
        DestinoNav.entries.forEachIndexed { indice, destino ->
            if (indice > 0) Spacer(Modifier.height(SEPARACION_ENTRE_DESTINOS))
            ItemRail(
                icono = iconoDe(destino),
                descripcion = etiquetaDe(destino),
                seleccionado = destino == destinoActual,
                onClick = { onDestino(destino) }
            )
        }

        // `weight(1f)` se come el espacio sobrante y empuja lo de abajo hasta el
        // fondo. Es "pegado abajo" sin medidas fijas: funciona igual en teléfono
        // que en tablet.
        Spacer(Modifier.weight(1f))

        // ---- La acción, hasta abajo (como en tu Figma) ----
        BotonAccion(onClick = onNuevo)
    }
}

/**
 * Un icono del rail.
 *
 * Es un `Box` pelón con `clickable`, no un `Surface`: el Surface dibuja su propio
 * contenedor y aparecía un cuadrito fantasma detrás de los iconos NO seleccionados.
 * Así, sin selección, no se dibuja absolutamente nada.
 *
 * El `contentDescription` NO puede ir en null aquí: en el rail cerrado no hay texto
 * visible, así que es lo único que TalkBack tiene para nombrar el botón.
 */
@Composable
internal fun ItemRail(
    @DrawableRes icono: Int,
    descripcion: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val indicador = colorIndicador()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(ITEM_TAM)                      // 72dp: muy por encima del mínimo táctil de 48
            .clip(RoundedCornerShape(ITEM_RADIO))
            .background(if (seleccionado) indicador else Color.Transparent)
            .clickable(role = Role.Tab, onClick = onClick)
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = descripcion,
            tint = if (seleccionado) MaterialTheme.colorScheme.onSecondaryContainer
            else MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier.size(ICONO_TAM)
        )
    }
}

/** El cuadrito claro de "nuevo recordatorio" que va hasta abajo del rail. */
@Composable
private fun BotonAccion(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(ITEM_TAM)
            .clip(RoundedCornerShape(ITEM_RADIO))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = "Nuevo recordatorio",
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.size(ICONO_TAM)
        )
    }
}

// ============================================================
//  BARRA INFERIOR  — teléfono
// ============================================================

/**
 * Barra inferior con muesca. Corresponde a tu variante "Celular".
 *
 * CÓMO SE HACE LA MUESCA
 *
 * La barra es un rectángulo normal. La "mordida" del centro no está recortada:
 * es un círculo del color del FONDO de la pantalla dibujado encima de la barra.
 * Como coincide con el fondo, el ojo lo lee como un hueco.
 *
 * Calcular la curva a mano es frágil y dificilísimo de ajustar. El único requisito
 * de este truco es que el círculo use el color real del fondo de la pantalla —
 * si tu pantalla usa otro color de fondo, cámbialo en `colorFondoPantalla`.
 */
@Composable
fun RecordatoriosBottomBar(
    destinoActual: DestinoNav,
    onDestino: (DestinoNav) -> Unit,
    onNuevo: () -> Unit,
    modifier: Modifier = Modifier,
    colorFondoPantalla: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    // El hueco de la barra del sistema. Por defecto se LEE del dispositivo;
    // los previews le pasan un número a mano para simular cada modo.
    insetInferior: Dp = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxWidth()
            // la barra + el hueco del sistema + lo que sobresale el botón
            .height(BARRA_ALTO + insetInferior + FAB_TAM / 2)
    ) {
        // ---- La barra verde, pegada abajo ----
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(BARRA_ALTO + insetInferior)
                .shadow(ELEVACION_BARRA)
                .background(colorFondoRail())
                // El verde se pinta ANTES del padding, así llega hasta el borde
                // físico de la pantalla y solo el CONTENIDO sube. Si lo pusieras
                // al revés, quedaría una franja del color del fondo abajo.
                .padding(bottom = insetInferior)
                .padding(horizontal = 16.dp)
        ) {
            // Cada icono va centrado en su mitad, con el hueco de la muesca
            // en medio. Con `weight` todo se reparte solo: la barra funciona
            // igual en un teléfono angosto que en uno ancho.
            DestinoNav.entries.forEachIndexed { indice, destino ->
                if (indice > 0) Spacer(Modifier.width(MUESCA_TAM))
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    ItemRail(
                        icono = iconoDe(destino),
                        descripcion = etiquetaDe(destino),
                        seleccionado = destino == destinoActual,
                        onClick = { onDestino(destino) }
                    )
                }
            }
        }

        // ---- El círculo de fondo que finge ser la muesca ----
        Box(
            modifier = Modifier
                .size(MUESCA_TAM)
                .clip(CircleShape)
                .background(colorFondoPantalla)
        )

        // ---- El botón de nuevo, centrado dentro de la muesca ----
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(y = (MUESCA_TAM - FAB_TAM) / 2)
                .size(FAB_TAM)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable(onClick = onNuevo)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = "Nuevo recordatorio",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(ICONO_TAM)
            )
        }
    }
}

// ============================================================
//  Piezas compartidas
//  Los iconos y nombres se definen UNA vez y los usan las tres barras.
// ============================================================

@DrawableRes
internal fun iconoDe(destino: DestinoNav): Int = when (destino) {
    DestinoNav.BANDEJA -> R.drawable.ic_inbox
    DestinoNav.PAPELERA -> R.drawable.ic_trash
}

internal fun etiquetaDe(destino: DestinoNav): String = when (destino) {
    DestinoNav.BANDEJA -> "Bandeja"
    DestinoNav.PAPELERA -> "Papelera"
}

// ============================================================
//  Previews — el fondo es el crema real de tu app (#EAE2D4),
//  no el blanco del preview, para que los colores se lean igual.
// ============================================================

private const val FONDO_APP = 0xFFEAE2D4

@Preview(name = "Rail · sin selección", showBackground = true, backgroundColor = FONDO_APP, heightDp = 640)
@Composable
private fun RailDefaultPreview() {
    GestorRecordatoriosTheme {
        RecordatoriosNavRail(DestinoNav.entries[0], {}, {}, {})
    }
}

@Preview(name = "Rail · bandeja", showBackground = true, backgroundColor = FONDO_APP, heightDp = 640)
@Composable
private fun RailBandejaPreview() {
    GestorRecordatoriosTheme {
        RecordatoriosNavRail(DestinoNav.BANDEJA, {}, {}, {})
    }
}

@Preview(name = "Rail · papelera", showBackground = true, backgroundColor = FONDO_APP, heightDp = 640)
@Composable
private fun RailPapeleraPreview() {
    GestorRecordatoriosTheme {
        RecordatoriosNavRail(DestinoNav.PAPELERA, {}, {}, {})
    }
}

@Preview(name = "Barra · gestos (24dp)", showBackground = true, backgroundColor = FONDO_APP, widthDp = 412)
@Composable
private fun BottomBarGestosPreview() {
    GestorRecordatoriosTheme {
        RecordatoriosBottomBar(DestinoNav.BANDEJA, {}, {}, insetInferior = 24.dp)
    }
}

@Preview(name = "Barra · tres botones (48dp)", showBackground = true, backgroundColor = FONDO_APP, widthDp = 412)
@Composable
private fun BottomBarTresBotonesPreview() {
    GestorRecordatoriosTheme {
        RecordatoriosBottomBar(DestinoNav.PAPELERA, {}, {}, insetInferior = 48.dp)
    }
}

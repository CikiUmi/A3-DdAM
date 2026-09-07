package com.ddam_a1.gestorrecordatorios.modelClasses

import java.time.LocalDateTime
import java.util.UUID

// Definir los enums
enum class nivelPrioridad {
    NULA,
    BAJA,
    MEDIA,
    ALTA
}
data class Recordatorio(
    val id: String = UUID.randomUUID().toString(),
    val titulo : String = "",
    val descripcion : String = "",
    val enPapelera: Boolean = false,
    val fechaRecordatorio: LocalDateTime = LocalDateTime.now(),
    val fechaEliminado: LocalDateTime? = null,
    val prioridad: nivelPrioridad = nivelPrioridad.NULA
)

// El ?: (Elvis Operator /gen) significa que revisa si hay un valor, si no, asigna uno.
// Sirve para asignar cosas. Ej. val nombre ?: = "user anónimo".


// Parece basura pero el val es por una razón ok?
// Se supone que si actualizas un objeto nada más, los datos cambian
// Pero creo que compose como que no le sabe y no actualiza la UI
// Entonces va de borrar y crear objetos
// Son ligeros, no pasa mucho creo :D
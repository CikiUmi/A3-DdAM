package com.ddam_a1.gestorrecordatorios.modelClasses

import java.time.LocalDateTime
import java.util.UUID

data class Recordatorio(
    val id: String = UUID.randomUUID().toString(),
    val titulo : String = "",
    val descripcion : String = "",
    val enPapelera: Boolean = false,
    val fechaRecordatorio: LocalDateTime,
    val fechaEliminado: LocalDateTime? = null,
    val fechaEliminarPermantente : LocalDateTime
)

// El ?: (Elvis Operator /gen) significa que revisa si hay un valor, si no, asigna uno.
// Sirve para asignar cosas. Ej. val nombre ?: = "user anónimo".

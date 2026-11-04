package com.equipe.treinoapp.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val formatoData: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun hojeMillis(): Long =
    LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

fun formatarData(millis: Long): String =
    Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate().format(formatoData)

fun formatarPeso(peso: Double): String {
    val arredondado = Math.round(peso * 10) / 10.0
    return if (arredondado % 1.0 == 0.0) arredondado.toLong().toString() else arredondado.toString()
}

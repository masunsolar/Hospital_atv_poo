package com.agendamento.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidadorHorario {

    private static final String TIME_PATTERN = "HH:mm";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    public static boolean isHorarioValido(String horario) {
        if (horario == null || horario.trim().isEmpty()) {
            return false;
        }
        try {
            // Tenta parsear a string para um LocalTime, o que valida o formato e os valores
            // (00-23 para hora, 00-59 para minuto) de forma rigorosa.
            LocalTime.parse(horario.trim(), TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            // Se houver um erro de parsing, o formato ou os valores são inválidos.
            return false;
        }
    }
}
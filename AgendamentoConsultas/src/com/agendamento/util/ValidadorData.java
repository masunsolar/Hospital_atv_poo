package com.agendamento.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.Month;

public class ValidadorData {

    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static boolean isDataValida(String data) {
        if (data == null || data.trim().isEmpty()) {
            return false;
        }
        try {
            // Tenta parsear a string para um LocalDate.
            // O parser do java.time é rigoroso e valida automaticamente
            // a consistência de dias em meses (ex: 31 de fevereiro) e anos bissextos.
            LocalDate.parse(data.trim(), DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            // Se houver um erro de parsing, a data é inválida ou não está no formato esperado.
            return false;
        }
    }

    public static boolean isDiaMesAnoValido(int dia, int mes, int ano) {
        if (dia < 1 || mes < 1 || mes > 12) {
            return false;
        }

        try {
            Month month = Month.of(mes);
            // Verifica o número máximo de dias para o mês, considerando ano bissexto para Fevereiro
            if (dia > month.maxLength()) {
                return false;
            }
            if (month == Month.FEBRUARY && dia == 29 && !LocalDate.of(ano, 1, 1).isLeapYear()) {
                return false; // 29 de fevereiro em ano não bissexto
            }
        } catch (java.time.DateTimeException e) {
            // Mês inválido (já coberto por mes < 1 || mes > 12) ou outro erro de data.
            return false;
        }
        return true;
    }
}
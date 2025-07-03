// src/com/agendamento/util/FormatadorTelefone.java
package com.agendamento.util;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class FormatadorTelefone {

    /**
     * Retorna uma MaskFormatter para números de telefone no formato (##) #####-####.
     * Define '_' como o caractere placeholder.
     *
     * @return MaskFormatter para telefone.
     */
    public static MaskFormatter criarMaskTelefone() {
        try {
            MaskFormatter telefoneMask = new MaskFormatter("(##) #####-####");
            telefoneMask.setPlaceholderCharacter('_');
            return telefoneMask;
        } catch (ParseException e) {
            // Em uma aplicação real, você logaria este erro ou o relançaria como uma RuntimeException
            // para que a aplicação falhe de forma controlada se a máscara não puder ser criada.
            throw new RuntimeException("Erro ao criar máscara de telefone: " + e.getMessage(), e);
        }
    }

    /**
     * Formata uma string de telefone bruta (apenas números) para o formato (##) #####-####.
     * Remove todos os caracteres não numéricos antes de tentar formatar.
     *
     * @param telefoneBruto O número de telefone sem formatação.
     * @return O telefone formatado ou o telefone bruto se a formatação falhar.
     */
    public static String formatarTelefone(String telefoneBruto) {
        if (telefoneBruto == null || telefoneBruto.trim().isEmpty()) {
            return "";
        }
        // Remove todos os caracteres não numéricos
        String apenasNumeros = telefoneBruto.replaceAll("[^0-9]", "");

        // Aplica a máscara se o número tiver o comprimento correto
        try {
            MaskFormatter formatter = criarMaskTelefone();
            // O formatter.valueToString requer que o input tenha o tamanho exato dos caracteres não-placeholders da máscara
            // Para (##) #####-####, são 11 dígitos.
            if (apenasNumeros.length() == 11) {
                return formatter.valueToString(apenasNumeros);
            } else {
                // Se o número não tiver o comprimento esperado, retorna o número limpo (ou o original)
                return apenasNumeros;
            }
        } catch (ParseException e) {
            System.err.println("Erro ao formatar telefone: " + e.getMessage());
            return telefoneBruto; // Retorna o original em caso de erro na formatação
        }
    }

    /**
     * Remove a formatação de um número de telefone, retornando apenas os dígitos.
     *
     * @param telefoneFormatado O número de telefone formatado.
     * @return O número de telefone com apenas dígitos.
     */
    public static String desformatarTelefone(String telefoneFormatado) {
        if (telefoneFormatado == null || telefoneFormatado.trim().isEmpty()) {
            return "";
        }
        return telefoneFormatado.replaceAll("[^0-9]", "");
    }
}
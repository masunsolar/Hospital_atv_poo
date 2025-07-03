package com.agendamento.core;

import com.agendamento.ui.mainmenu.MenuPrincipalFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Inicia a nova tela de menu principal
        SwingUtilities.invokeLater(MenuPrincipalFrame::new);
    }
}
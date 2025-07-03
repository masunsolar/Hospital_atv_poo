package com.agendamento.ui.medico;

import com.agendamento.core.Main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TelaAgendaMedico extends JFrame {
    private JComboBox<String> comboMedicos = new JComboBox<>();
    private JTextArea areaAgenda = new JTextArea();

    public TelaAgendaMedico() {
        setTitle("Agenda do Médico");
        setSize(600, 600);
        setLayout(new BorderLayout());

        carregarMedicos();

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(new JLabel("Selecione o Médico:"), BorderLayout.WEST);
        topo.add(comboMedicos, BorderLayout.CENTER);
        add(topo, BorderLayout.NORTH);

        areaAgenda.setEditable(false);
        add(new JScrollPane(areaAgenda), BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(evt -> {
            dispose();
            new Main();
        });
        add(btnVoltar, BorderLayout.SOUTH);

        comboMedicos.addActionListener(e -> carregarAgenda());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void carregarMedicos() {
        try (Scanner sc = new Scanner(new File("data/medicos.txt"))) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" \\|");
                if (partes.length >= 2) {
                    comboMedicos.addItem(partes[0].trim() + " - " + partes[1].trim());
                }
            }
        } catch (FileNotFoundException e) {
            areaAgenda.setText("Erro ao carregar médicos.");
        }
    }

    private void carregarAgenda() {
        areaAgenda.setText("");
        String item = (String) comboMedicos.getSelectedItem();
        if (item == null) return;

        String id = item.split(" - ")[0].trim();

        try (Scanner sc = new Scanner(new File("data/consultas.txt"))) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                if (linha.startsWith(id + "|") || linha.contains("Dr(a). " + item.split(" - ")[1].trim())) {
                    areaAgenda.append(linha + "\n");
                }
            }
        } catch (FileNotFoundException e) {
            areaAgenda.setText("Nenhuma consulta encontrada.");
        }
    }

    public static void main(String[] args) {
        new TelaAgendaMedico();
    }
}
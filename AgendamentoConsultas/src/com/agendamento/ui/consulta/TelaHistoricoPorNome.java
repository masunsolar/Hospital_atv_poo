package com.agendamento.ui.consulta;

import com.agendamento.core.Main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class TelaHistoricoPorNome extends JFrame {
    private JTextField campoNome = new JTextField();
    private JTextArea areaResultados = new JTextArea();

    public TelaHistoricoPorNome() {
        setTitle("Histórico de Consultas por Nome");
        setSize(500, 500);
        setLayout(new BorderLayout());

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(new JLabel("Nome do Paciente (como foi agendado):"), BorderLayout.WEST);
        topo.add(campoNome, BorderLayout.CENTER);
        add(topo, BorderLayout.NORTH);

        areaResultados.setEditable(false);
        add(new JScrollPane(areaResultados), BorderLayout.CENTER);

        JButton buscarBtn = new JButton("Buscar");
        JButton voltarBtn = new JButton("Voltar ao Menu");

        JPanel botoes = new JPanel();
        botoes.add(buscarBtn);
        botoes.add(voltarBtn);
        add(botoes, BorderLayout.SOUTH);

        buscarBtn.addActionListener(e -> buscarConsultas());
        voltarBtn.addActionListener(e -> {
            dispose();
            Main.main(new String[]{});
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void buscarConsultas() {
        String nomeBusca = campoNome.getText().trim();
        areaResultados.setText("");

        if (nomeBusca.isEmpty()) {
            areaResultados.setText("Digite o nome completo do paciente.");
            return;
        }

        File file = new File("data/consultas/consultas.txt");
        if (!file.exists()) {
            areaResultados.setText("Arquivo de consultas não encontrado.");
            return;
        }

        boolean encontrou = false;

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" \\|");
                if (partes.length >= 6) {
                    String nomePaciente = partes[2].trim();
                    if (nomePaciente.equalsIgnoreCase(nomeBusca)) {
                        String dataHora = partes[4].trim();
                        String nomeMedico = partes[1].trim();
                        String status = partes[5].trim();
                        areaResultados.append("Data: " + dataHora + " | Médico: " + nomeMedico + " | Status: " + status + "\n");
                        encontrou = true;
                    }
                }
            }
        } catch (Exception e) {
            areaResultados.setText("Erro ao ler o histórico.");
            e.printStackTrace();
        }

        if (!encontrou) {
            areaResultados.setText("Nenhuma consulta encontrada para: " + nomeBusca);
        }
    }
}
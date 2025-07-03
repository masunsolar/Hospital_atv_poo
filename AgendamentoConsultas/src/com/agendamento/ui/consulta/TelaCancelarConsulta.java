package com.agendamento.ui.consulta;

import com.agendamento.core.Main;
import com.agendamento.model.entidades.Consulta;
import com.agendamento.model.entidades.StatusConsulta;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class TelaCancelarConsulta extends JFrame {
    private JTextField campoBusca = new JTextField();
    private JTextArea areaResultados = new JTextArea();

    public TelaCancelarConsulta() {
        setTitle("Cancelar Consulta");
        setSize(600, 600);
        setLayout(new BorderLayout());

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(new JLabel("Nome do Paciente e Horário para Cancelar:"), BorderLayout.WEST);
        topo.add(campoBusca, BorderLayout.CENTER);
        add(topo, BorderLayout.NORTH);

        areaResultados.setEditable(false);
        add(new JScrollPane(areaResultados), BorderLayout.CENTER);

        JButton buscarBtn = new JButton("Buscar");
        JButton cancelarBtn = new JButton("Cancelar Consulta");
        JButton voltarBtn = new JButton("Voltar ao Menu");

        JPanel botoes = new JPanel();
        botoes.add(buscarBtn);
        botoes.add(cancelarBtn);
        botoes.add(voltarBtn);
        add(botoes, BorderLayout.SOUTH);

        buscarBtn.addActionListener(e -> buscarConsultas());
        cancelarBtn.addActionListener(e -> cancelarConsulta());
        voltarBtn.addActionListener(e -> {
            dispose();
            Main.main(new String[]{});
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void buscarConsultas() {
        String termo = campoBusca.getText().trim();
        areaResultados.setText("");

        if (termo.isEmpty()) {
            areaResultados.setText("Digite o nome completo e horário da consulta.");
            return;
        }

        File arquivo = new File("data/consultas/consultas.txt");
        if (!arquivo.exists()) {
            areaResultados.setText("Nenhuma consulta encontrada.");
            return;
        }

        try (Scanner sc = new Scanner(arquivo)) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" \\|");
                if (partes.length >= 6) {
                    String nomePaciente = partes[2].trim();
                    String horario = partes[4].trim();
                    if (nomePaciente.contains(termo) || horario.contains(termo)) {
                        String dataHora = partes[4].trim();
                        String medico = partes[1].trim();
                        String status = partes[5].trim();
                        areaResultados.append("Data: " + dataHora + " | Médico: " + medico + " | Status: " + status + "\n");
                    }
                }
            }
        } catch (Exception e) {
            areaResultados.setText("Erro ao ler o arquivo de consultas.");
            e.printStackTrace();
        }
    }

    private void cancelarConsulta() {
        String selecionado = areaResultados.getSelectedText();
        if (selecionado == null || selecionado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma consulta para cancelar.");
            return;
        }

        // Alterar status para CANCELADA
        File inputFile = new File("data/consultas/consultas.txt");
        File tempFile = new File("data/consultas/consultas_temp.txt");

        try (
                Scanner scanner = new Scanner(inputFile);
                FileWriter writer = new FileWriter(tempFile)
        ) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                if (linha.contains(selecionado)) {
                    Consulta consulta = Consulta.fromString(linha);
                    if (consulta != null) {
                        consulta.setStatus(StatusConsulta.CANCELADA);
                        writer.write(consulta.toString() + "\n");
                    }
                } else {
                    writer.write(linha + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        JOptionPane.showMessageDialog(this, "Consulta cancelada com sucesso!");
        buscarConsultas();
    }
}

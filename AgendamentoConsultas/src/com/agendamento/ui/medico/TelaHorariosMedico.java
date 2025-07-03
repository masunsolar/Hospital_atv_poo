package com.agendamento.ui.medico;

import com.agendamento.core.Main;
import com.agendamento.util.ValidadorData;     // Importa a classe ValidadorData
import com.agendamento.util.ValidadorHorario;  // Importa a classe ValidadorHorario

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.Scanner;

public class TelaHorariosMedico extends JFrame {

    private DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private JList<String> listaHorarios = new JList<>(modeloLista);
    private JFormattedTextField campoData;
    private JFormattedTextField campoHora;
    private JComboBox<String> comboMedicos;

    public TelaHorariosMedico() {
        setTitle("Horários do Médico");
        setSize(600, 600);
        setLayout(new BorderLayout());

        try {
            MaskFormatter dataMask = new MaskFormatter("##/##/####");
            MaskFormatter horaMask = new MaskFormatter("##:##");
            campoData = new JFormattedTextField(dataMask);
            campoHora = new JFormattedTextField(horaMask);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        comboMedicos = new JComboBox<>();
        carregarMedicos(comboMedicos);

        JButton btnAdicionar = new JButton("Adicionar Horário");
        JButton btnRemover = new JButton("Remover Selecionado");
        JButton btnVoltar = new JButton("Voltar ao Menu Principal"); // New button

        JPanel painelTopo = new JPanel(new GridLayout(3, 2));
        painelTopo.add(new JLabel("Médico:"));
        painelTopo.add(comboMedicos);
        painelTopo.add(new JLabel("Data (dd/MM/yyyy):"));
        painelTopo.add(campoData);
        painelTopo.add(new JLabel("Hora (HH:mm):"));
        painelTopo.add(campoHora);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnVoltar); // Add the new button

        add(painelTopo, BorderLayout.NORTH);
        add(new JScrollPane(listaHorarios), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Listener para carregar horários do médico selecionado
        comboMedicos.addActionListener(e -> carregarHorariosMedico((String) comboMedicos.getSelectedItem()));

        // Listener para adicionar horário
        btnAdicionar.addActionListener(e -> {
            String medicoSelecionado = (String) comboMedicos.getSelectedItem();
            String data = campoData.getText().trim();
            String hora = campoHora.getText().trim();

            if (medicoSelecionado == null || data.isEmpty() || hora.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }

            // --- ADICIONA A VALIDAÇÃO DE DATA E HORÁRIO AQUI ---
            if (!ValidadorData.isDataValida(data)) {
                JOptionPane.showMessageDialog(this, "Data inválida!");
                return;
            }

            if (!ValidadorHorario.isHorarioValido(hora)) {
                JOptionPane.showMessageDialog(this, "Hora inválida! (00:00 a 23:59).");
                return;
            }
            // --- FIM DA VALIDAÇÃO ---

            int idMedico = obterIdMedico(medicoSelecionado);
            String horarioCompleto = data + " " + hora;

            // Verifica se o horário já existe para evitar duplicatas (opcional, mas boa prática)
            if (modeloLista.contains(horarioCompleto)) {
                JOptionPane.showMessageDialog(this, "Este horário já foi adicionado para este médico.");
                return;
            }

            salvarHorario(idMedico, horarioCompleto);
            modeloLista.addElement(horarioCompleto);
            campoData.setText("");
            campoHora.setText("");
            JOptionPane.showMessageDialog(this, "Horário adicionado!");
        });

        // Listener para remover horário
        btnRemover.addActionListener(e -> {
            String horarioSelecionado = listaHorarios.getSelectedValue();
            String medicoSelecionado = (String) comboMedicos.getSelectedItem();

            if (horarioSelecionado == null || medicoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um médico e um horário para remover.");
                return;
            }

            int idMedico = obterIdMedico(medicoSelecionado);
            // O formato em horarios.txt é "data hora"
            String[] partesHorario = horarioSelecionado.split(" ");
            String horarioCompletoRemover = partesHorario[0] + " " + partesHorario[1];


            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja remover o horário: " + horarioSelecionado + "?",
                    "Confirmar Remoção",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                removerHorario(idMedico, horarioCompletoRemover);
                modeloLista.removeElement(horarioSelecionado);
                JOptionPane.showMessageDialog(this, "Horário removido!");
            }
        });

        btnVoltar.addActionListener(evt -> {
            dispose(); // Fecha a tela atual
            Main.main(new String[]{}); // Volta para o menu principal
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void carregarMedicos(JComboBox<String> combo) {
        File file = new File("data/medicos.txt");
        File dataDir = new File("data");
        if (!dataDir.exists()) { dataDir.mkdirs(); }
        if (!file.exists()) {
            try { file.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] partes = sc.nextLine().split(" \\|");
                if (partes.length >= 2) {
                    combo.addItem(partes[0].trim() + " - " + partes[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarHorariosMedico(String medico) {
        modeloLista.clear();
        if (medico == null) return;

        int idMedico = obterIdMedico(medico);
        File file = new File("data/horarios.txt");
        File dataDir = new File("data");
        if (!dataDir.exists()) { dataDir.mkdirs(); }
        if (!file.exists()) {
            try { file.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" \\|");
                if (partes.length >= 2 && Integer.parseInt(partes[0].trim()) == idMedico) {
                    // O item adicionado à lista deve ser no formato "data hora"
                    modeloLista.addElement(partes[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int obterIdMedico(String itemCombo) {
        try {
            return Integer.parseInt(itemCombo.split(" - ")[0].trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private void salvarHorario(int idMedico, String horario) {
        try {
            File dir = new File("data");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            FileWriter fw = new FileWriter("data/horarios.txt", true);
            fw.write(idMedico + " | " + horario + "\n"); // Salva no formato "id;data hora"
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removerHorario(int idMedico, String horario) {
        File inputFile = new File("data/horarios.txt");
        File tempFile = new File("data/horarios_temp.txt");

        try (
                Scanner scanner = new Scanner(inputFile);
                FileWriter writer = new FileWriter(tempFile)
        ) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                // A linha no arquivo é "idMedico;data hora"
                if (!linha.equals(idMedico + " | " + horario)) {
                    writer.write(linha + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
}
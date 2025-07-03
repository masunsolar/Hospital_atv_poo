package com.agendamento.ui.consulta;

import com.agendamento.core.Main;
import com.agendamento.model.entidades.Consulta;
import com.agendamento.model.entidades.Paciente;
import com.agendamento.model.entidades.StatusConsulta;
import com.agendamento.util.FormatadorTelefone;
import com.agendamento.util.ValidadorData;
import com.agendamento.util.ValidadorHorario;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.Scanner;

public class TelaAgendamento extends JFrame {

    public TelaAgendamento() {
        setTitle("Agendar Consulta");
        setSize(400, 500);
        setLayout(new BorderLayout());

        // Campos de entrada
        JTextField nomePaciente = new JTextField();
        JTextField idadeField = new JTextField();
        JComboBox<String> comboSexo = new JComboBox<>(new String[]{"Masculino", "Feminino"});
        JFormattedTextField telefonePaciente = new JFormattedTextField(FormatadorTelefone.criarMaskTelefone());
        JTextField enderecoField = new JTextField();
        JComboBox<String> especialidadeCombo = new JComboBox<>();
        JComboBox<String> medicosCombo = new JComboBox<>();
        JComboBox<String> datasCombo = new JComboBox<>();
        JComboBox<String> horariosCombo = new JComboBox<>();

        // Carregamento inicial de dados
        carregarEspecialidades(especialidadeCombo);

        // Listeners para os ComboBoxes
        especialidadeCombo.addActionListener(e -> {
            medicosCombo.removeAllItems();
            datasCombo.removeAllItems();
            horariosCombo.removeAllItems();
            carregarMedicosPorEspecialidade((String) especialidadeCombo.getSelectedItem(), medicosCombo);
        });

        medicosCombo.addActionListener(e -> {
            datasCombo.removeAllItems();
            horariosCombo.removeAllItems();
            carregarDatas((String) medicosCombo.getSelectedItem(), datasCombo);
        });

        datasCombo.addActionListener(e -> {
            horariosCombo.removeAllItems();
            carregarHorarios((String) medicosCombo.getSelectedItem(), (String) datasCombo.getSelectedItem(), horariosCombo);
        });

        // Painel para os campos de entrada e seus rótulos (usando GridLayout)
        JPanel painelCampos = new JPanel(new GridLayout(9, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelCampos.add(new JLabel("Nome do Paciente:"));
        painelCampos.add(nomePaciente);
        painelCampos.add(new JLabel("Idade:"));
        painelCampos.add(idadeField);
        painelCampos.add(new JLabel("Sexo:"));
        painelCampos.add(comboSexo);
        painelCampos.add(new JLabel("Telefone:"));
        painelCampos.add(telefonePaciente);
        painelCampos.add(new JLabel("Endereço:"));
        painelCampos.add(enderecoField);
        painelCampos.add(new JLabel("Especialidade:"));
        painelCampos.add(especialidadeCombo);
        painelCampos.add(new JLabel("Médico:"));
        painelCampos.add(medicosCombo);
        painelCampos.add(new JLabel("Data:"));
        painelCampos.add(datasCombo);
        painelCampos.add(new JLabel("Horário:"));
        painelCampos.add(horariosCombo);

        // Botões
        JButton agendarBtn = new JButton("Agendar");
        JButton btnVoltar = new JButton("Voltar ao Menu");

        // Painel para os botões (usando FlowLayout para ficarem lado a lado)
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(agendarBtn);
        painelBotoes.add(btnVoltar);

        // Adiciona os painéis à JFrame principal
        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        agendarBtn.addActionListener(e -> {
            String nome = capitalize(nomePaciente.getText().trim());
            String idade = idadeField.getText().trim();
            String sexo = (String) comboSexo.getSelectedItem();
            String telefoneFormatado = telefonePaciente.getText().trim();
            String telefoneDesformatado = FormatadorTelefone.desformatarTelefone(telefoneFormatado);

            String endereco = capitalize(enderecoField.getText().trim());
            String medicoSelecionado = (String) medicosCombo.getSelectedItem();
            String data = (String) datasCombo.getSelectedItem();
            String hora = (String) horariosCombo.getSelectedItem();

            if (nome.isEmpty() || idade.isEmpty() || sexo == null || telefoneDesformatado.isEmpty() || endereco.isEmpty() ||
                    medicoSelecionado == null || data == null || hora == null) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!");
                return;
            }

            if (telefoneDesformatado.length() != 11 || !telefoneDesformatado.matches("^\\d{11}$")) {
                JOptionPane.showMessageDialog(this, "Telefone inválido! Deve conter 11 dígitos numéricos (incluindo DDD).");
                return;
            }

            if (!ValidadorData.isDataValida(data)) {
                JOptionPane.showMessageDialog(this, "Data selecionada é inválida ou está no formato incorreto (esperado dd/MM/yyyy).");
                return;
            }

            if (!ValidadorHorario.isHorarioValido(hora)) {
                JOptionPane.showMessageDialog(this, "Hora selecionada é inválida ou está no formato incorreto (esperado HH:mm).");
                return;
            }

            int idMedico = obterIdMedico(medicoSelecionado);
            String nomeMedico = medicoSelecionado.split(" - ")[1].trim();
            String horarioCompleto = data + " " + hora; // This is correct to form "DD/MM/YYYY HH:MM"

            Paciente paciente = new Paciente(nome, idade, sexo, telefoneFormatado, endereco);
            salvarPaciente(paciente);

            // Using the correct field name for the constructor, which is 'horarioCompleto'
            Consulta c = new Consulta(idMedico, nomeMedico, nome, telefoneFormatado, horarioCompleto, StatusConsulta.PENDENTE);
            salvarConsulta(c);
            removerHorarioDoArquivo(idMedico, horarioCompleto);

            JOptionPane.showMessageDialog(this, "Consulta agendada!");
            horariosCombo.removeItem(hora);

            // Limpar campos após o agendamento
            nomePaciente.setText("");
            idadeField.setText("");
            comboSexo.setSelectedIndex(0);
            telefonePaciente.setText("");
            enderecoField.setText("");
        });

        btnVoltar.addActionListener(evt -> {
            dispose();
            Main.main(new String[]{});
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void carregarEspecialidades(JComboBox<String> combo) {
        Set<String> especialidades = new TreeSet<>();
        File file = new File("data/medicos.txt");
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] partes = sc.nextLine().split(" \\|");
                if (partes.length >= 3) {
                    especialidades.add(partes[2].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String esp : especialidades) {
            combo.addItem(esp);
        }
    }

    private void carregarMedicosPorEspecialidade(String especialidade, JComboBox<String> combo) {
        File file = new File("data/medicos.txt");
        File dataDir = new File("data");
        if (!dataDir.exists()) { dataDir.mkdirs(); }
        if (!file.exists()) {
            try { file.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] partes = sc.nextLine().split(" \\|");
                if (partes.length >= 3 && partes[2].trim().equalsIgnoreCase(especialidade)) {
                    combo.addItem(partes[0].trim() + " - " + partes[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarDatas(String medico, JComboBox<String> combo) {
        if (medico == null) return;
        int idMedico = obterIdMedico(medico);
        Set<String> datas = new TreeSet<>();

        File file = new File("data/horarios.txt");
        File dataDir = new File("data");
        if (!dataDir.exists()) { dataDir.mkdirs(); }
        if (!file.exists()) {
            try { file.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] partes = sc.nextLine().split(" \\|");
                if (partes.length >= 2 && Integer.parseInt(partes[0].trim()) == idMedico) {
                    String[] horarioCompleto = partes[1].trim().split(" ");
                    if (horarioCompleto.length >= 1) {
                        datas.add(horarioCompleto[0]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String d : datas) {
            combo.addItem(d);
        }
    }

    private void carregarHorarios(String medico, String data, JComboBox<String> combo) {
        if (medico == null || data == null) return;
        int idMedico = obterIdMedico(medico);

        File file = new File("data/horarios.txt");
        File dataDir = new File("data");
        if (!dataDir.exists()) { dataDir.mkdirs(); }
        if (!file.exists()) {
            try { file.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] partes = sc.nextLine().split(" \\|");
                if (partes.length >= 2 && Integer.parseInt(partes[0].trim()) == idMedico) {
                    String[] horarioCompleto = partes[1].trim().split(" ");
                    if (horarioCompleto.length >= 2 && horarioCompleto[0].equals(data)) {
                        combo.addItem(horarioCompleto[1]);
                    }
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

    private void salvarConsulta(Consulta c) {
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();
            FileWriter fw = new FileWriter("data/consultas/consultas.txt", true);
            // Using the new getters from the updated Consulta class
            fw.write(c.getIdMedico() + " | " + c.getNomeMedico() + " | " +
                    c.getNomePaciente() + " | " + c.getTelefonePaciente() + " | " +
                    c.getHorario() + " | " + c.getStatus() + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarPaciente(Paciente p) {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
        try (FileWriter fw = new FileWriter("data/pacientes.txt", true)) {
            // Os campos são separados por "; " (ponto e vírgula seguido de espaço) para consistência
            fw.write(p.getNome() + " | " + p.getIdade() + " | " + p.getSexo() + " | " + p.getTelefone() + " | " + p.getEndereco() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removerHorarioDoArquivo(int idMedico, String horarioCompleto) {
        File inputFile = new File("data/horarios.txt");
        File tempFile = new File("data/horarios_temp.txt");

        try (
                Scanner scanner = new Scanner(inputFile);
                FileWriter writer = new FileWriter(tempFile)
        ) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                // Note: The line in "horarios.txt" is expected to be "idMedico;data hora" (no space after ;)
                // Ensure consistency here. If it was "idMedico; data hora", adjust the split or comparison.
                // Based on previous code, the file contains "idMedico;horarioCompleto" where horarioCompleto is "data hora"
                if (!linha.equals(idMedico + "|" + horarioCompleto)) {
                    writer.write(linha + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }

    private String capitalize(String texto) {
        String[] palavras = texto.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                sb.append(Character.toUpperCase(palavra.charAt(0)))
                        .append(palavra.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
package com.agendamento.ui.medico;

import com.agendamento.core.Main;
import com.agendamento.model.entidades.Medico;
import com.agendamento.util.FormatadorTelefone; // Importa a nova classe FormatadorTelefone

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class CadastroSimplesView extends JFrame {
    public CadastroSimplesView() {
        setTitle("Sistema Clínica - Cadastro Médico");
        setSize(400, 250);
        setLayout(new BorderLayout());

        JTextField nomeField = new JTextField();
        // Usando a classe FormatadorTelefone para criar a máscara
        JFormattedTextField telefoneField = new JFormattedTextField(FormatadorTelefone.criarMaskTelefone());
        JTextField enderecoField = new JTextField();
        JTextField especialidadeField = new JTextField();

        JButton salvarBtn = new JButton("Salvar Médico");
        JButton btnVoltar = new JButton("Voltar ao Menu Principal");

        JPanel painelCampos = new JPanel(new GridLayout(4, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelCampos.add(new JLabel("Nome:"));
        painelCampos.add(nomeField);
        painelCampos.add(new JLabel("Telefone:"));
        painelCampos.add(telefoneField);
        painelCampos.add(new JLabel("Endereço:"));
        painelCampos.add(enderecoField);
        painelCampos.add(new JLabel("Especialidade:"));
        painelCampos.add(especialidadeField);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(salvarBtn);
        painelBotoes.add(btnVoltar);

        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        salvarBtn.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            // Pega o texto formatado do campo, mas verifica o desformatado para validação
            String telefoneFormatado = telefoneField.getText().trim();
            String telefoneDesformatado = FormatadorTelefone.desformatarTelefone(telefoneFormatado);

            String endereco = enderecoField.getText().trim();
            String especialidade = especialidadeField.getText().trim();

            if (nome.isEmpty() || telefoneDesformatado.isEmpty() || endereco.isEmpty() || especialidade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!");
                return;
            }

            // Valida o telefone desformatado
            if (telefoneDesformatado.length() != 11 || !telefoneDesformatado.matches("^\\d{11}$")) {
                JOptionPane.showMessageDialog(this, "Telefone inválido! Deve conter 11 dígitos numéricos (incluindo DDD).");
                return;
            }

            nome = capitalize(nome);
            endereco = capitalize(endereco);
            especialidade = capitalize(especialidade);

            if (medicoJaExiste(nome)) {
                JOptionPane.showMessageDialog(this, "Já existe um médico com esse nome!");
                return;
            }

            int novoId = gerarProximoId();
            // Agora, passa o telefone formatado para o objeto Medico
            Medico m = new Medico(novoId, nome, telefoneFormatado, endereco, especialidade);
            // O método salvarEmArquivo agora usará o telefone já formatado do objeto Medico
            salvarEmArquivo(m);
            JOptionPane.showMessageDialog(this, "Médico cadastrado com sucesso!");

            // Limpa os campos após o cadastro
            nomeField.setText("");
            telefoneField.setText("");
            enderecoField.setText("");
            especialidadeField.setText("");
        });

        btnVoltar.addActionListener(e -> {
            dispose();
            Main.main(new String[]{});
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private String capitalize(String texto) {
        String[] palavras = texto.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                sb.append(Character.toUpperCase(palavra.charAt(0))).append(palavra.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }

    private boolean medicoJaExiste(String nome) {
        File file = new File("data/medicos.txt");
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            try {
                new File(dataDir, "medicos.txt").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        if (!file.exists()) return false;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(" \\|");
                // Com base em como você salva: id; nome; especialidade; telefone; endereco
                // O nome é partes[1]
                if (partes.length >= 2 && partes[1].trim().equalsIgnoreCase(nome)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void salvarEmArquivo(Medico m) {
        try {
            File dir = new File("data");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            FileWriter fw = new FileWriter("data/medicos.txt", true);
            // Salva o telefone diretamente do objeto Medico, que agora conterá o formato
            fw.write(m.getId() + " | " + m.getNome() + " | " + m.getEspecialidade() + " | " + m.getTelefone() + " | " + m.getEndereco() + "\n");
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int gerarProximoId() {
        int maxId = 0;
        File file = new File("data/medicos.txt");

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            try {
                new File(dataDir, "medicos.txt").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String linha = scanner.nextLine();
                    String[] partes = linha.split(" \\|");
                    if (partes.length > 0) {
                        int idAtual = Integer.parseInt(partes[0]);
                        if (idAtual > maxId) {
                            maxId = idAtual;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return maxId + 1;
    }
}
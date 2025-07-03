package com.agendamento.ui.consulta;

import com.agendamento.core.Main;
import com.agendamento.model.entidades.Consulta;
import com.agendamento.model.entidades.StatusConsulta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

public class TelaAlterarStatusConsulta extends JFrame {
    private JTextField campoBusca = new JTextField();
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private List<String> linhasOriginais = new ArrayList<>(); // Armazena linhas do arquivo

    public TelaAlterarStatusConsulta() {
        setTitle("Alterar Status de Consulta");
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Campo de busca
        JPanel topo = new JPanel(new BorderLayout());
        topo.add(new JLabel("Nome do Paciente ou Horário:"), BorderLayout.WEST);
        topo.add(campoBusca, BorderLayout.CENTER);
        add(topo, BorderLayout.NORTH);

        // Tabela com checkbox
        modeloTabela = new DefaultTableModel(new Object[]{"Selecionar", "Consulta"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(600);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botões
        JButton buscarBtn = new JButton("Buscar");
        JButton cancelarBtn = new JButton("Cancelar Consulta");
        JButton concluirBtn = new JButton("Concluir Consulta");
        JButton voltarBtn = new JButton("Voltar ao Menu");

        JPanel botoes = new JPanel();
        botoes.add(buscarBtn);
        botoes.add(cancelarBtn);
        botoes.add(concluirBtn);
        botoes.add(voltarBtn);
        add(botoes, BorderLayout.SOUTH);

        // Ações
        buscarBtn.addActionListener(e -> buscarConsultas());
        cancelarBtn.addActionListener(e -> alterarStatus(StatusConsulta.CANCELADA));
        concluirBtn.addActionListener(e -> alterarStatus(StatusConsulta.CONCLUIDA));
        voltarBtn.addActionListener(e -> {
            dispose();
            Main.main(new String[]{});
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void buscarConsultas() {
        String termo = campoBusca.getText().trim().toLowerCase();
        modeloTabela.setRowCount(0);
        linhasOriginais.clear();

        File arquivo = new File("data/consultas/consultas.txt");
        if (!arquivo.exists()) {
            JOptionPane.showMessageDialog(this, "Nenhuma consulta encontrada.");
            return;
        }

        try (Scanner sc = new Scanner(arquivo)) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                if (linha.toLowerCase().contains(termo)) {
                    modeloTabela.addRow(new Object[]{false, linha});
                    linhasOriginais.add(linha);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo.");
            e.printStackTrace();
        }
    }

    private void alterarStatus(StatusConsulta novoStatus) {
        if (modeloTabela.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Nenhuma consulta listada.");
            return;
        }

        File inputFile = new File("data/consultas/consultas.txt");
        File tempFile = new File("data/consultas/consultas_temp.txt");
        File statusFile = new File("data/consultas/" +
                (novoStatus == StatusConsulta.CONCLUIDA ? "concluidas.txt" : "canceladas.txt"));

        Set<String> selecionadas = new HashSet<>();
        for (int i = 0; i < modeloTabela.getRowCount(); i++) {
            if ((boolean) modeloTabela.getValueAt(i, 0)) {
                selecionadas.add((String) modeloTabela.getValueAt(i, 1));
            }
        }

        if (selecionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos uma consulta.");
            return;
        }

        try (
                Scanner scanner = new Scanner(inputFile);
                PrintWriter writer = new PrintWriter(tempFile);
                FileWriter statusWriter = new FileWriter(statusFile, true)
        ) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                if (selecionadas.contains(linha)) {
                    // Substituir apenas o último campo (status)
                    String novaLinha = substituirStatus(linha, novoStatus.name());
                    writer.println(novaLinha);
                    statusWriter.write(novaLinha + "\n");
                } else {
                    writer.println(linha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao alterar status.");
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        JOptionPane.showMessageDialog(this, "Status atualizado com sucesso!");
        buscarConsultas();
    }

    private String substituirStatus(String linha, String novoStatus) {
        String[] partes = linha.split(" \\| ");
        if (partes.length < 6) return linha;
        partes[partes.length - 1] = novoStatus;
        return String.join(" | ", partes);
    }
}

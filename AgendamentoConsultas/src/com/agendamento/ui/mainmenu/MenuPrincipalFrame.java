package com.agendamento.ui.mainmenu;

import com.agendamento.ui.consulta.TelaAgendamento;
import com.agendamento.ui.consulta.TelaAlterarStatusConsulta;
import com.agendamento.ui.consulta.TelaCancelarConsulta;
import com.agendamento.ui.consulta.TelaHistoricoPorNome;
import com.agendamento.ui.medico.CadastroSimplesView;
import com.agendamento.ui.medico.TelaHorariosMedico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipalFrame extends JFrame implements ActionListener {

    private JButton btnCadastrarMedico;
    private JButton btnAgendarConsulta;
    private JButton btnGerenciarHorarios;
    private JButton btnAlterarStatus;
    private JButton btnCancelarConsulta;
    private JButton btnHistoricoPorNome;

    public MenuPrincipalFrame() {
        setTitle("Sistema de Consultas - Menu Principal");
        setSize(550, 250); // Ajuste este tamanho para influenciar a altura dos botões
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Use um BorderLayout para organizar o JLabel e o painel de botões
        setLayout(new BorderLayout());

        // Adiciona o texto "Escolha uma opção:" no topo
        JLabel instrucaoLabel = new JLabel("Escolha uma opção:", SwingConstants.CENTER);
        instrucaoLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Opcional: ajusta a fonte
        instrucaoLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); // Espaçamento acima e abaixo
        add(instrucaoLabel, BorderLayout.NORTH);

        // Painel principal usando GridLayout de 3 linhas e 2 colunas
        // Com espaçamento de 15 pixels entre as células (horizontal e vertical)
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20)); // Margem interna (top ajustado)

        // Inicialização dos botões com o texto original
        btnCadastrarMedico = new JButton("Cadastrar Médico");
        btnAgendarConsulta = new JButton("Agendar Consulta");
        btnGerenciarHorarios = new JButton("Gerenciar Horários");
        btnAlterarStatus = new JButton("Alterar Status de Consulta");
        btnCancelarConsulta = new JButton("Cancelar Consulta");
        btnHistoricoPorNome = new JButton("Histórico de Consultas por Nome");

        // Adiciona ActionListener para cada botão
        btnCadastrarMedico.addActionListener(this);
        btnAgendarConsulta.addActionListener(this);
        btnGerenciarHorarios.addActionListener(this);
        btnAlterarStatus.addActionListener(this);
        btnCancelarConsulta.addActionListener(this);
        btnHistoricoPorNome.addActionListener(this);

        // Adiciona os botões ao painel da grade na ordem desejada
        mainPanel.add(btnCadastrarMedico);
        mainPanel.add(btnAgendarConsulta);
        mainPanel.add(btnGerenciarHorarios);
        mainPanel.add(btnAlterarStatus);
        mainPanel.add(btnCancelarConsulta);
        mainPanel.add(btnHistoricoPorNome);

        // Adiciona o painel da grade à janela na posição central
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose(); // Fecha a janela atual (Menu Principal)

        if (e.getSource() == btnCadastrarMedico) {
            System.out.println("Abrindo Cadastrar Médico...");
            new CadastroSimplesView();
        } else if (e.getSource() == btnAgendarConsulta) {
            System.out.println("Abrindo Agendar Consulta...");
            new TelaAgendamento();
        } else if (e.getSource() == btnGerenciarHorarios) {
            System.out.println("Abrindo Gerenciar Horários...");
            new TelaHorariosMedico();
        } else if (e.getSource() == btnAlterarStatus) {
            System.out.println("Abrindo Alterar Status de Consulta...");
            new TelaAlterarStatusConsulta();
        } else if (e.getSource() == btnCancelarConsulta) {
            System.out.println("Abrindo Cancelar Consulta...");
            new TelaCancelarConsulta();
        } else if (e.getSource() == btnHistoricoPorNome) {
            System.out.println("Abrindo Histórico de Consultas por Nome...");
            new TelaHistoricoPorNome();
        }
    }
}
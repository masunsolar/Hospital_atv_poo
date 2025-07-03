package com.agendamento.model.entidades;

import com.agendamento.model.base.Pessoa;

public class Medico extends Pessoa {
    private String especialidade;

    public Medico(int id, String nome, String telefone, String endereco, String especialidade) {
        super(id, nome, telefone, endereco);
        this.especialidade = especialidade;
    }

    @Override
    public void verificarAgenda() {
        System.out.println("Verificando agenda do médico " + nome);
    }

    @Override
    public void atualizarHorario() {
        System.out.println("Atualizando horário do médico " + nome);
    }

    @Override
    public void agendarConsulta() {
        System.out.println("Consulta agendada com o médico " + nome);
    }

    @Override
    public void cancelarConsulta() {
        System.out.println("Consulta cancelada com o médico " + nome);
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }
}
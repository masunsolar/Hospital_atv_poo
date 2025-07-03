package com.agendamento.model.base;

public abstract class Pessoa {
    protected int id;
    protected String nome;
    protected String telefone;
    protected String endereco;

    public Pessoa(int id, String nome, String telefone, String endereco) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public abstract void verificarAgenda();
    public abstract void atualizarHorario();
    public abstract void agendarConsulta();
    public abstract void cancelarConsulta();
}
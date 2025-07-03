package com.agendamento.model.entidades;

public class Paciente {
    private String nome;
    private String idade;
    private String sexo; // This field exists
    private String telefone;
    private String endereco;

    public Paciente(String nome, String idade, String sexo, String telefone, String endereco) {
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return nome + " | " + idade + " | " + sexo + " | " + telefone + " | " + endereco;
    }

    // Getters for all fields
    public String getNome() {
        return nome;
    }

    public String getIdade() {
        return idade;
    }

    // --- You need to add this getter method for 'sexo' ---
    public String getSexo() {
        return sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }
}
package com.agendamento.model.entidades;

public class Consulta {
    private int idMedico;
    private String nomeMedico;
    private String nomePaciente;
    private String telefonePaciente;
    private String horario;
    private StatusConsulta status;  // Adicionando o campo status

    // Construtor atualizado para receber o status
    public Consulta(int idMedico, String nomeMedico, String nomePaciente, String telefonePaciente, String horario, StatusConsulta statusConsulta) {
        this.idMedico = idMedico;
        this.nomeMedico = nomeMedico;
        this.nomePaciente = nomePaciente;
        this.telefonePaciente = telefonePaciente;
        this.horario = horario;
        this.status = statusConsulta;  // Inicializando o status
    }

    // Método setStatus para alterar o status
    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    // Método toString atualizado para incluir o status
    public String toString() {
        return horario + " - " + nomePaciente + " (" + telefonePaciente + ") | com Dr(a). " + nomeMedico + " | Status: " + status;
    }

    // Método fromString atualizado para lidar com status
    public static Consulta fromString(String linha) {
        String[] partes = linha.split(" \\|");
        if (partes.length >= 6) {
            return new Consulta(
                    Integer.parseInt(partes[0].trim()),
                    partes[1].trim(),
                    partes[2].trim(),
                    partes[3].trim(),
                    partes[4].trim(),
                    StatusConsulta.valueOf(partes[5].trim())  // Agora recebendo o status
            );
        }
        return null;
    }

    // Métodos getters
    public int getIdMedico() {
        return idMedico;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public String getTelefonePaciente() {
        return telefonePaciente;
    }

    public String getHorario() {
        return horario;
    }

    public StatusConsulta getStatus() {
        return status;
    }
}

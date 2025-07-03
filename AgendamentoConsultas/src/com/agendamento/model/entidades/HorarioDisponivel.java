package com.agendamento.model.entidades;

import java.time.LocalDateTime;

public class HorarioDisponivel {
    private int idMedico;
    private LocalDateTime horario;

    public HorarioDisponivel(int idMedico, LocalDateTime horario) {
        this.idMedico = idMedico;
        this.horario = horario;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public String toString() {
        return horario.toString();
    }
}
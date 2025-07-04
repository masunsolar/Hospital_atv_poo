# Sistema de Agendamento de Consultas Médicas

Este projeto é um sistema simples de **agendamento de consultas médicas**, desenvolvido como atividade da disciplina de **Programação Orientada a Objetos (POO)**.

O sistema permite o cadastro de médicos e pacientes, o agendamento de consultas com escolha de horário, além da visualização da agenda do médico e a possibilidade de alterar o status das consultas (como agendada, realizada ou cancelada). Também é possível modificar os horários disponíveis de atendimento dos médicos.

A interface é feita com **Java Swing** e simula o funcionamento de um sistema básico de clínicas ou consultórios médicos. Os dados são salvos em arquivos de texto locais, substituindo um banco de dados tradicional.

---

### 🧱 Estrutura do Projeto

- `com.agendamento.core.Main`: Classe principal que inicia a aplicação.

- `com.agendamento.model.entidades`: Contém as entidades principais do sistema:
  - `Pessoa`, `Paciente`, `Medico`, `Consulta`, `Horario`, `Pagamento`, etc.

- `com.agendamento.model.enums`: Enumerações como `StatusConsulta`.

- `com.agendamento.repository`: Classes responsáveis pelo armazenamento em memória ou arquivos.

- `com.agendamento.service`: Lógica de negócios (serviços).

- `com.agendamento.ui`: Interface gráfica usando Swing com telas como:
  - Cadastro de Médicos/Pacientes
  - Agendamento de Consultas
  - Visualização e alteração da agenda

---

### 🖥️ Funcionalidades Implementadas

- ✅ Cadastro de médicos e pacientes  
- ✅ Agendamento de consultas com escolha de horário  
- ✅ Visualização da agenda do médico  
- ✅ Alteração do status da consulta (ex: Agendada, Realizada, Cancelada)  
- ✅ Edição dos horários disponíveis do médico  
- ✅ Interface gráfica simples e funcional  

---

### 🧪 Tecnologias Utilizadas

- ☕ Java SE 8+  
- 🖼️ Java Swing (para a interface gráfica)  
- 💾 Salvamento em arquivos `.txt` (simulando banco de dados)  

---

### ▶️ Como Executar

1. Importe o projeto em sua IDE Java (ex: IntelliJ ou Eclipse)
2. Execute a classe `Main.java` localizada em `com.agendamento.core`
3. A interface gráfica será aberta com as opções de uso

---

### ✍️ Autor

- Nome: Natan e Giovanni 
- Disciplina: Programação Orientada a Objetos (POO)  
- Professor: Anderson Iwazaki 
- Instituição: UNIFIL 
- Semestre: 2° ano B2 Ciência da computação

---

### 📌 Observações

- Este sistema é simplificado e tem fins didáticos.  
- Não utiliza banco de dados real, apenas arquivos locais.  
- O foco principal está na aplicação de conceitos de POO, como:
  - Herança (`Pessoa` → `Paciente` / `Medico`)
  - Encapsulamento
  - Polimorfismo
  - Abstração

package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ValidadorHorarioFuncionamentoClinicaTest {

    private ValidadorHorarioFuncionamentoClinica validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorHorarioFuncionamentoClinica();
    }

    @Test
    @DisplayName("Nao Deve Lancar Excecao Para Consulta Em Horario Valido")
    void ValidadorHorarioFuncionamentoClinicaCenario1() {
        // Segunda-feira às 10h
        LocalDateTime data = LocalDateTime.of(2025, 8, 4, 10, 0);
        var dados = new DadosAgendamentoConsulta(1L, 1L, data, null);

        assertDoesNotThrow(() -> validador.validar(dados));
    }

    @Test
    @DisplayName("Deve Lancar Excecao Para Consulta Antes Das 7 Horas")
    void ValidadorHorarioFuncionamentoClinicaCenario2() {
        // Segunda-feira às 6h
        LocalDateTime data = LocalDateTime.of(2025, 8, 4, 6, 0);
        var dados = new DadosAgendamentoConsulta(1L, 1L, data, null);

        var ex = assertThrows(ValidacaoException.class, () -> validador.validar(dados));
        assertEquals("Consulta fora do horário de funcionamento da clínica", ex.getMessage());
    }

    @Test
    @DisplayName("Deve Lancar Excecao Para Consulta Apos As 18 Horas")
    void ValidadorHorarioFuncionamentoClinicaCenario3() {
        // Segunda-feira às 19h
        LocalDateTime data = LocalDateTime.of(2025, 8, 4, 19, 0);
        var dados = new DadosAgendamentoConsulta(1L, 1L, data, null);

        var ex = assertThrows(ValidacaoException.class, () -> validador.validar(dados));
        assertEquals("Consulta fora do horário de funcionamento da clínica", ex.getMessage());
    }

    @Test
    @DisplayName("Deve Lancar Excecao Para Consulta No Domingo")
    void ValidadorHorarioFuncionamentoClinicaCenario4() {
        // Domingo às 10h
        LocalDateTime data = LocalDateTime.of(2025, 8, 3, 10, 0);
        var dados = new DadosAgendamentoConsulta(1L, 1L, data, null);

        var ex = assertThrows(ValidacaoException.class, () -> validador.validar(dados));
        assertEquals("Consulta fora do horário de funcionamento da clínica", ex.getMessage());
    }
}

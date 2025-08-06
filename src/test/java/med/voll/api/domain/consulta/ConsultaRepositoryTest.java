package med.voll.api.domain.consulta;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.medico.*;
import med.voll.api.domain.paciente.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ConsultaRepositoryTest {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Valida se o relatório retornado contém os dados esperados de 1 consulta")
    void gerarRelatorioConsultaMensalCenario1() {
        // Arrange
        var medico = cadastrarMedico("Medico", "medico@voll.med", "123456", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Paciente", "paciente@email.com", "00000000000");
        var dataConsulta = LocalDateTime.of(2025, 8, 10, 10, 0);
        cadastrarConsulta(medico, paciente, dataConsulta);

        var inicioMes = LocalDateTime.of(2025, 8, 1, 0, 0);
        var fimMes = LocalDateTime.of(2025, 8, 31, 23, 59);

        // Act
        List<DadosRelatorioConsultaMensal> relatorio = consultaRepository.gerarRelatorioConsultaMensal(inicioMes, fimMes);

        // Assert
        assertEquals(1, relatorio.size());

        var resultado = relatorio.get(0);
        assertEquals("Medico", resultado.nome());
        assertEquals("123456", resultado.crm());
        assertEquals(1L, resultado.quantidadeConsultasNoMes());
    }

    @Test
    @DisplayName("Valida se o relatório retornado contém a quantidade de 3 consultas para o mesmo medico")
    void gerarRelatorioConsultaMensalCenario2() {
        // Arrange
        var medico1 = cadastrarMedico("Medico1", "medico1@voll.med", "123456", Especialidade.CARDIOLOGIA);
        var paciente1 = cadastrarPaciente("Paciente1", "paciente1@email.com", "00000000000");
        var dataConsulta1 = LocalDateTime.of(2025, 8, 6, 10, 0);
        cadastrarConsulta(medico1, paciente1, dataConsulta1);

        var paciente2 = cadastrarPaciente("Paciente2", "paciente2@email.com", "00000000022");
        var dataConsulta2 = LocalDateTime.of(2025, 8, 7, 10, 0);
        cadastrarConsulta(medico1, paciente2, dataConsulta2);

        var paciente3 = cadastrarPaciente("Paciente3", "paciente3@email.com", "12300000000");
        var dataConsulta3 = LocalDateTime.of(2025, 8, 8, 10, 0);
        cadastrarConsulta(medico1, paciente3, dataConsulta3);

        var inicioMes = LocalDateTime.of(2025, 8, 1, 0, 0);
        var fimMes = LocalDateTime.of(2025, 8, 31, 23, 59);

        // Act
        List<DadosRelatorioConsultaMensal> relatorio = consultaRepository.gerarRelatorioConsultaMensal(inicioMes, fimMes);

        // Assert
        assertEquals(1, relatorio.size());

        var resultado = relatorio.get(0);
        assertEquals("Medico1", resultado.nome());
        assertEquals("123456", resultado.crm());

        assertEquals(3L, resultado.quantidadeConsultasNoMes());
    }

    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
        em.persist(new Consulta(null, medico, paciente, data, null));
    }

    private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
        var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
        em.persist(medico);
        return medico;
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf) {
        var paciente = new Paciente(dadosPaciente(nome, email, cpf));
        em.persist(paciente);
        return paciente;
    }

    private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
        return new DadosCadastroMedico(
                nome,
                email,
                "61999999999",
                crm,
                especialidade,
                dadosEndereco()
        );
    }

    private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
        return new DadosCadastroPaciente(
                nome,
                email,
                "61999999999",
                cpf,
                dadosEndereco()
        );
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}

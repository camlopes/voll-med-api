package med.voll.api.domain.consulta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByPacienteIdAndDataBetween(Long idPaciente, LocalDateTime primeiroHorario, LocalDateTime ultimoHorario);

    boolean existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(Long idMedico, LocalDateTime data);

    @Query("""
         SELECT new med.voll.api.domain.consulta.DadosRelatorioConsultaMensal(m.nome, m.crm, COUNT(c))
         FROM Consulta c JOIN c.medico m
         WHERE c.data >= :inicioMes AND c.data <= :fimMes
         GROUP BY m.nome, m.crm
         """)
    List<DadosRelatorioConsultaMensal> gerarRelatorioConsultaMensal(LocalDateTime inicioMes, LocalDateTime fimMes);
}

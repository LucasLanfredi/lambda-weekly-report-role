package fiap.lambda;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fiap.dto.WeeklyReport;
import fiap.model.FeedbackEntity;
import fiap.presenters.ReportPresenter;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

import java.util.List;
import java.util.Map;

public class EmailServiceTest {

        @Test
        void deveGerarRelatorioCorretamente() {

                FeedbackEntity f1 = new FeedbackEntity();
                f1.setNota(8);
                f1.setTimestamp("2024-06-01T10:15:30.000Z");

                FeedbackEntity f2 = new FeedbackEntity();
                f2.setNota(6);
                f2.setTimestamp("2024-06-01T14:20:00.000Z");

                FeedbackEntity f3 = new FeedbackEntity();
                f3.setNota(10);
                f3.setTimestamp("2024-06-02T09:00:00.000Z");

                List<FeedbackEntity> feedbacks = List.of(f1, f2, f3);

                WeeklyReport report = ReportPresenter.gerarRelatorio(feedbacks);

                // Média: (8 + 6 + 10) / 3 = 8.0
                assertEquals(8.0, report.getMediaAvaliacoes());

                assertEquals(
                        Map.of(
                                "2024-06-01", 2L,
                                "2024-06-02", 1L
                        ),
                        report.getQuantidadePorDia()
                );

                assertEquals(
                        Map.of(
                                Boolean.FALSE, 2L,
                                Boolean.TRUE, 1L
                        ),
                        report.getQuantidadePorUrgencia()
                );
        }

        @Test
        void deveRetornarRelatorioVazioQuandoListaVazia() {

                WeeklyReport report = ReportPresenter.gerarRelatorio(List.of());

                assertEquals(0.0, report.getMediaAvaliacoes());
                assertTrue(report.getQuantidadePorDia().isEmpty());
                assertTrue(report.getQuantidadePorUrgencia().isEmpty());
        }
}

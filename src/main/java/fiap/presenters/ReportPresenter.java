package fiap.presenters;

import fiap.dto.WeeklyReport;
import fiap.model.FeedbackEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReportPresenter {

    public static WeeklyReport gerarRelatorio(List<FeedbackEntity> feedbacks) {

        WeeklyReport report = new WeeklyReport();

        if (feedbacks == null || feedbacks.isEmpty()) {
            report.setMediaAvaliacoes(0.0);
            report.setQuantidadePorDia(Map.of());
            report.setQuantidadePorUrgencia(Map.of());
            return report;
        }

        // Média das avaliações
        report.setMediaAvaliacoes(
                feedbacks.stream()
                        .map(FeedbackEntity::getNota)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0)
        );

        // Quantidade de avaliações por dia
        report.setQuantidadePorDia(
                feedbacks.stream()
                        .filter(f -> f.getTimestamp() != null)
                        .collect(Collectors.groupingBy(
                                f -> LocalDate.parse(f.getTimestamp()).toString(),
                                Collectors.counting()
                        ))
        );

        // Quantidade de avaliações por urgência
        report.setQuantidadePorUrgencia(
                feedbacks.stream()
                        .filter(f -> f.getUrgencia() != null)
                        .collect(Collectors.groupingBy(
                                FeedbackEntity::getUrgencia,
                                Collectors.counting()
                        ))
        );

        return report;
    }

    public static String toText(WeeklyReport report) {
        return """
                RELATÓRIO SEMANAL DE FEEDBACKS
                
                Média das avaliações: %.2f
                
                Avaliações por dia:
                %s
                
                Avaliações por urgência:
                %s
                """.formatted(
                report.getMediaAvaliacoes(),
                formatMapString(report.getQuantidadePorDia()),
                formatMapBoolean(report.getQuantidadePorUrgencia())
        );
    }

    public static String toHtml(WeeklyReport report) {
        return """
            <h2>Relatório Semanal de Feedbacks</h2>

            <p><b>Média das avaliações:</b> %.2f</p>

            <h3>Avaliações por dia</h3>
            <ul>
                %s
            </ul>

            <h3>Avaliações por urgência</h3>
            <ul>
                %s
            </ul>
            """.formatted(
                report.getMediaAvaliacoes(),
                formatMapHtmlString(report.getQuantidadePorDia()),
                formatMapHtmlBoolean(report.getQuantidadePorUrgencia())
        );
    }

    private static String formatMapString(Map<String, Long> map) {
        if (map == null || map.isEmpty()) {
            return "Nenhum registro encontrado";
        }

        return map.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\n"));
    }

    private static String formatMapBoolean(Map<Boolean, Long> map) {
        if (map == null || map.isEmpty()) {
            return "Nenhum registro encontrado";
        }

        return map.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\n"));
    }

    private static String formatMapHtmlString(Map<String, Long> map) {
        if (map == null || map.isEmpty()) {
            return "<li>Nenhum registro encontrado</li>";
        }

        return map.entrySet().stream()
                .map(e -> "<li>" + e.getKey() + ": " + e.getValue() + "</li>")
                .collect(Collectors.joining());
    }

    private static String formatMapHtmlBoolean(Map<Boolean, Long> map) {
        if (map == null || map.isEmpty()) {
            return "<li>Nenhum registro encontrado</li>";
        }

        return map.entrySet().stream()
                .map(e -> "<li>" + e.getKey() + ": " + e.getValue() + "</li>")
                .collect(Collectors.joining());
    }
}

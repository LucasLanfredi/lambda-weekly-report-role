package fiap.repository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import fiap.model.FeedbackEntity;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import util.DateUtils;

@ApplicationScoped
public class WeeklyReportRepository {

    private static final Logger LOG = Logger.getLogger(WeeklyReportRepository.class);

    private static final String TABLE_NAME = "feedback";

    private final DynamoDbTable<FeedbackEntity> table;

    public WeeklyReportRepository() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder().build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        this.table = enhancedClient.table(
                TABLE_NAME,
                TableSchema.fromBean(FeedbackEntity.class));
    }

    /**
     * Busca feedbacks enviados nos últimos 7 dias
     */
    public List<FeedbackEntity> buscarFeedbacksUltimaSemana() {
        LocalDate hoje = LocalDate.now(ZoneOffset.UTC);
        LocalDate inicioPeriodo = hoje.minusDays(6); // 7 dias no total

        List<FeedbackEntity> resultados = new ArrayList<>();

        // usar exatamente o nome do índice conforme console: "createdAt"
        DynamoDbIndex<FeedbackEntity> index = table.index("createdAt");

        for (int i = 0; i < 7; i++) {
            LocalDate date = inicioPeriodo.plusDays(i);
            String dateStr = date.toString(); // yyyy-MM-dd conforme GSI partition key

            long inicioDia = date.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
            long fimDia = date.plusDays(1)
                    .atStartOfDay(ZoneOffset.UTC)
                    .minusSeconds(1)
                    .toEpochSecond();

            SdkIterable<Page<FeedbackEntity>> pages = index.query(q -> q.queryConditional(
                    QueryConditional.sortBetween(
                            Key.builder()
                                    .partitionValue(dateStr)
                                    .sortValue(inicioDia)
                                    .build(),
                            Key.builder()
                                    .partitionValue(dateStr)
                                    .sortValue(fimDia)
                                    .build()
                    )
            ));

            for (Page<FeedbackEntity> page : pages) {
                resultados.addAll(page.items());
            }
        }

        LOG.infof("Total de feedbacks encontrados na última semana: %d", resultados.size());
        return resultados;
    }
}


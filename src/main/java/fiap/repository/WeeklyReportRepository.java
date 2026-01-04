package fiap.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fiap.model.FeedbackEntity;
import org.jboss.logging.Logger;


import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
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
        try {
            LocalDate hoje = LocalDate.now();
            LocalDate inicioPeriodo = hoje.minusDays(7);

            List<FeedbackEntity> resultados = new ArrayList<>();

            table.scan().items().forEach(feedback -> {
                if (feedback.getTimestamp() != null) {
                    LocalDate dataEnvio = DateUtils.toLocalDate(feedback.getTimestamp());

                    if (!dataEnvio.isBefore(inicioPeriodo)) {
                        resultados.add(feedback);
                    }
                }
            });

            LOG.infof("Total de feedbacks encontrados na última semana: %d", resultados.size());
            return resultados;

        } catch (DynamoDbException e) {
            Log.error("Erro ao consultar feedbacks no DynamoDB", e);
            throw e;
        }
    }
}


package fiap.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import fiap.dto.WeeklyReport;
import fiap.model.FeedbackEntity;
import fiap.presenters.ReportPresenter;
import fiap.repository.WeeklyReportRepository;

import fiap.service.ReportStorageService;

import java.util.List;

public class WeeklyReportHandler implements RequestHandler<ScheduledEvent, Void> {

    private final WeeklyReportRepository repository;
    private final ReportStorageService storageService;

    public WeeklyReportHandler() {
        this.repository = new WeeklyReportRepository();
        this.storageService = new ReportStorageService();
    }

    @Override
    public Void handleRequest(ScheduledEvent input, Context context) {

        List<FeedbackEntity> feedbacks =
                repository.buscarFeedbacksUltimaSemana();

        WeeklyReport report = ReportPresenter.gerarRelatorio(feedbacks);

        storageService.storeReport(
                ReportPresenter.toText(report),
                ReportPresenter.toHtml(report)
        );

        context.getLogger().log("Relatório semanal enviado com sucesso");
        return null;
    }
}

package fiap.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import fiap.dto.WeeklyReport;
import fiap.model.FeedbackEntity;
import fiap.presenters.ReportPresenter;
import fiap.repository.WeeklyReportRepository;
import fiap.service.EmailService;

import java.util.List;

public class WeeklyReportHandler implements RequestHandler<Object, Void> {

    private final WeeklyReportRepository repository;
    private final EmailService emailService;

    public WeeklyReportHandler() {
        this.repository = new WeeklyReportRepository();
        this.emailService = new EmailService();
    }

    @Override
    public Void handleRequest(Object input, Context context) {

        List<FeedbackEntity> feedbacks =
                repository.buscarFeedbacksUltimaSemana();

        WeeklyReport report = ReportPresenter.gerarRelatorio(feedbacks);

        emailService.sendEmail(
                System.getenv("RECIPIENT_EMAIL"),
                "Relatório Semanal de Feedbacks",
                ReportPresenter.toText(report),
                ReportPresenter.toHtml(report)
        );

        context.getLogger().log("Relatório semanal enviado com sucesso");
        return null;
    }
}

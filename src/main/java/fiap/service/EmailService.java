package fiap.service;

import org.jboss.logging.Logger;

import io.quarkus.logging.Log;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;

public class EmailService {

        private static final Logger LOG = Logger.getLogger(EmailService.class);

        private static final String SOURCE = System.getenv("SOURCE_EMAIL");

        private final SesClient sesClient;

        public EmailService() {
                this.sesClient = SesClient.builder().build();
        }

        public EmailService(SesClient sesClient) {
                this.sesClient = sesClient;
        }


        public void sendEmail(
                final String recipient,
                final String subject,
                final String textContent,
                final String htmlContent) {

                try {
                        Destination destination = Destination.builder()
                                .toAddresses(recipient)
                                .build();

                        Content subjectContent = Content.builder()
                                .data(subject)
                                .charset("UTF-8")
                                .build();

                        Content textBody = Content.builder()
                                .data(textContent)
                                .charset("UTF-8")
                                .build();

                        Content htmlBody = Content.builder()
                                .data(htmlContent)
                                .charset("UTF-8")
                                .build();

                        Body body = Body.builder()
                                .text(textBody)
                                .html(htmlBody)
                                .build();

                        Message message = Message.builder()
                                .subject(subjectContent)
                                .body(body)
                                .build();

                        SendEmailRequest request = SendEmailRequest.builder()
                                .source(SOURCE)
                                .destination(destination)
                                .message(message)
                                .build();

                        sesClient.sendEmail(request);

                        LOG.infof("E-mail enviado com sucesso para %s", recipient);

                } catch (SesException e) {
                        Log.error("Erro ao enviar e-mail via SES", e);
                        throw e;
                }
        }
}

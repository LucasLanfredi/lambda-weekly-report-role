package fiap.service;

import org.jboss.logging.Logger;

import io.quarkus.logging.Log;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class ReportStorageService {

    private static final Logger LOG = Logger.getLogger(ReportStorageService.class);

    private static final String BUCKET = System.getenv("REPORT_BUCKET");

    private final S3Client s3Client;

    public ReportStorageService() {
        this.s3Client = S3Client.builder().build();
    }

    public void storeReport(String reportText, String reportHtml) {

        try {
            String date = LocalDate.now().toString();

            putObject("reports/weekly-report-" + date + ".txt",
                    reportText,
                    "text/plain; charset=UTF-8");

            putObject("reports/weekly-report-" + date + ".html",
                    reportHtml,
                    "text/html; charset=UTF-8");

            LOG.info("Relatório semanal armazenado com sucesso no S3");

        } catch (S3Exception e) {
            Log.error("Erro ao armazenar relatório no S3", e);
            throw e;
        }
    }

    private void putObject(String key, String content, String contentType) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(key)
                .contentType(contentType)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(content.getBytes(StandardCharsets.UTF_8))
        );
    }
}

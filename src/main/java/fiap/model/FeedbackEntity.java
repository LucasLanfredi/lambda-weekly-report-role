package fiap.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class FeedbackEntity {

    private String id;            // PK da tabela
    private String date;          // GSI partition key (yyyy-MM-dd) -> attribute name "date"
    private Long createdAt;       // GSI sort key (epoch seconds) -> attribute name "createdAt"
    private String descricao;
    private Integer nota;

    // ===== Primary Key (tabela) =====
    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // ===== GSI Partition Key =====
    @DynamoDbSecondaryPartitionKey(indexNames = {"createdAt"})
    @DynamoDbAttribute("date")
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    // ===== GSI Sort Key =====
    @DynamoDbSecondarySortKey(indexNames = {"createdAt"})
    @DynamoDbAttribute("createdAt")
    public Long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    // ===== Outros campos =====
    @DynamoDbAttribute("descricao")
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @DynamoDbAttribute("nota")
    public Integer getNota() {
        return nota;
    }
    public void setNota(Integer nota) {
        this.nota = nota;
    }

    @DynamoDbIgnore
    public Boolean getUrgencia() {
        return nota != null && nota < 3;
    }
}

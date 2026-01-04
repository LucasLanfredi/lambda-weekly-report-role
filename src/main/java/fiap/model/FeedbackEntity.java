package fiap.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class FeedbackEntity {

    private String id;
    private String descricao;
    private Integer nota;
    private Boolean urgencia;
    private String timestamp;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}

    public Boolean getUrgencia() {return urgencia;}

    public void setUrgencia(Boolean urgencia) {this.urgencia = urgencia;}
}
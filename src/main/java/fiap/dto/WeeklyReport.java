package fiap.dto;

import java.util.Map;

public class WeeklyReport {

    private double mediaAvaliacoes;
    private Map<String, Long> quantidadePorDia;
    private Map<Boolean, Long> quantidadePorUrgencia;

    public double getMediaAvaliacoes() {
        return mediaAvaliacoes;
    }

    public void setMediaAvaliacoes(double mediaAvaliacoes) {
        this.mediaAvaliacoes = mediaAvaliacoes;
    }

    public Map<String, Long> getQuantidadePorDia() {
        return quantidadePorDia;
    }

    public void setQuantidadePorDia(Map<String, Long> quantidadePorDia) {
        this.quantidadePorDia = quantidadePorDia;
    }

    public Map<Boolean, Long> getQuantidadePorUrgencia() {
        return quantidadePorUrgencia;
    }

    public void setQuantidadePorUrgencia(Map<Boolean, Long> quantidadePorUrgencia) {
        this.quantidadePorUrgencia = quantidadePorUrgencia;
    }
}

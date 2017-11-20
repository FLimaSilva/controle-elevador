package edu.tcc.controleelevador.model;

/**
 * Created by uellisson on 31/08/2017.
 *
 * Modelo do objeto que representa o relatório de ultrapassagem
 * dos limites de subida e descida do elevador.
 */
public class ReportLdLs {
    /**
     * Atributos da classe
     */
    private String date;
    private String hour;
    private String event;

    /**
     * Construtor da classe
     * @param date
     * @param hour
     * @param event
     */
    public ReportLdLs(String date, String hour, String event) {
        this.date = date;
        this.hour = hour;
        this.event = event;
    }

    /**
     * Métodos usados para capturar informações dos atributos (gets)
     */
    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public String getEvent() {
        return event;
    }
}
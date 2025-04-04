package com.transportes.urgentes.gestion_viajes.travels;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TravelDTO {
    private Long id;
    private String origen;
    private String destino;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaEstimadaLlegada;
    private LocalDateTime fechaRealLlegada;
    private Double latitudActual;
    private Double longitudActual;
    private Double distanciaRecorrida;
    private Double distanciaTotal;
    private Double velocidadActual;
    private Integer tiempoEstimadoRestante;
    private Long conductorId;
    private String codigoSeguimiento;
    private String observaciones;
    private LocalDateTime ultimaActualizacion;
} 
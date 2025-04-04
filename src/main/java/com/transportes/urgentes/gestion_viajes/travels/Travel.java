package com.transportes.urgentes.gestion_viajes.travels;

import com.transportes.urgentes.gestion_viajes.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String origen;
    private String destino;
    private String estado;
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_estimada_llegada")
    private LocalDateTime fechaEstimadaLlegada;
    
    @Column(name = "fecha_real_llegada")
    private LocalDateTime fechaRealLlegada;
    
    @Column(name = "latitud_actual")
    private Double latitudActual;
    
    @Column(name = "longitud_actual")
    private Double longitudActual;
    
    @Column(name = "distancia_recorrida")
    private Double distanciaRecorrida;
    
    @Column(name = "distancia_total")
    private Double distanciaTotal;
    
    @Column(name = "velocidad_actual")
    private Double velocidadActual;
    
    @Column(name = "tiempo_estimado_restante")
    private Integer tiempoEstimadoRestante;
    
    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private User conductor;
    
    @Column(name = "codigo_seguimiento")
    private String codigoSeguimiento;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    // Getters y setters
}

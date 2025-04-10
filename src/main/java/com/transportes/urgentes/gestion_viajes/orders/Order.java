package com.transportes.urgentes.gestion_viajes.orders;

import com.transportes.urgentes.gestion_viajes.travels.Travel;
import com.transportes.urgentes.gestion_viajes.users.User;
import com.transportes.urgentes.gestion_viajes.drivers.Conductor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numeroPedido;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private User cliente;
    
    @OneToOne
    @JoinColumn(name = "travel_id")
    private Travel travel;
    
    private String estado;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "codigo_seguimiento")
    private String codigoSeguimiento;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "peso")
    private Double peso;
    
    @Column(name = "dimensiones")
    private String dimensiones;
    
    @Column(name = "direccion_origen")
    private String direccionOrigen;
    
    @Column(name = "direccion_destino")
    private String direccionDestino;
    
    @Column(name = "instrucciones_especiales")
    private String instruccionesEspeciales;
    
    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private Conductor conductor;

    @Column(name = "total_amount")
    private Double totalAmount;
    
    private LocalDateTime createdAt;
    private LocalDateTime fechaCompletada;
    private LocalDateTime fechaAsignacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        calculateTotalAmount();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
        calculateTotalAmount();
    }

    private void calculateTotalAmount() {
        double basePrice = 50.0;
        
        if (peso != null) {
            basePrice += peso * 10;
        }
        
        if (dimensiones != null && !dimensiones.isEmpty()) {
            basePrice += 20;
        }
        
        this.totalAmount = basePrice;
    }
}

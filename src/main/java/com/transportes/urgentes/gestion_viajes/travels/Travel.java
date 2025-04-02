package com.transportes.urgentes.gestion_viajes.travels;


import com.transportes.urgentes.gestion_viajes.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne
    private User conductor;

    // Getters y setters
}

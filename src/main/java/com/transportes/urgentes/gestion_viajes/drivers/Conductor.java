package com.transportes.urgentes.gestion_viajes.drivers;

import com.transportes.urgentes.gestion_viajes.users.User;
import com.transportes.urgentes.gestion_viajes.users.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "drivers")
@Getter
@Setter
public class Conductor extends User {
    
    private String licenciaConduccion;
    private String tipoLicencia;
    private boolean disponible;
    private Double calificacion;
    private Integer viajesCompletados;
    
    public Conductor() {
        super();
        this.setRole(UserRole.DRIVER);
        this.disponible = true;
        this.calificacion = 0.0;
        this.viajesCompletados = 0;
    }
}

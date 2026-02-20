package com.ae.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @Column(name = "id_factura")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_factura"
    )
    @SequenceGenerator(
            name = "seq_factura",
            sequenceName = "factura_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(
            name = "numero_factura",
            nullable = false
    )
    private String numeroFactura;

    @Column(
            name = "fecha_creacion",
            insertable = false,
            updatable = false
    )
    private LocalDateTime fechaCreacion;

    @Column(name = "cliente")
    private String cliente;

    @Column(name = "total", precision = 12, scale = 2)
    private BigDecimal total;

    @OneToMany(
            mappedBy = "factura",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<FacturaDetalle> detalles = new ArrayList<>();

    public Factura() {
        /* Empty Constructor */
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<FacturaDetalle> getDetalles(){
        return this.detalles;
    }

    public void addDetalle(FacturaDetalle facturaDetalle){
        this.detalles.add(facturaDetalle);
        facturaDetalle.setFactura(this);
    }

    public void removeDetalle(FacturaDetalle facturaDetalle){
        detalles.remove(facturaDetalle);
        facturaDetalle.setFactura(null);
    }
}

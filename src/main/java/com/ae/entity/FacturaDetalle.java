package com.ae.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "factura_detalle")
public class FacturaDetalle {

    @Id
    @Column(name = "id_factura_detalle")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_factura_detalle"
    )
    @SequenceGenerator(
            name = "seq_factura_detalle",
            sequenceName = "factura_detalle_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "id_factura",
            nullable = false
    )
    private Factura factura;

    @Column(name = "producto")
    private String producto;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "precio_unitario", precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "descuento", precision = 12, scale = 2)
    private BigDecimal descuento;

    @Column(name = "sub_total", precision = 12, scale = 2)
    private BigDecimal subTotal;

    public FacturaDetalle() {
        /* Empty Constructor */
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
}
package com.ae.service;

import com.ae.entity.Factura;

import java.util.List;

public interface FacturaService {

    List<Factura> getAll();

    Factura getById(Long id);

    Factura create(Factura factura);

    Factura update(Long id, Factura factura);

    void delete(Long id);
}

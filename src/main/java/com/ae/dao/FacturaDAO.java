package com.ae.dao;

import com.ae.entity.Factura;

import java.util.List;

public interface FacturaDAO {

    public List<Factura> getInvoices();

    public Factura getInvoiceById(Long id);

    public Factura saveInvoice(Factura factura);

    public Factura deleteInvoice(Long id);

    public Factura updateInvoice(Long id, Factura invoiceUpdate);

}

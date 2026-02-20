package com.ae.service.impl;

import com.ae.dao.FacturaDAO;
import com.ae.entity.Factura;
import com.ae.entity.FacturaDetalle;
import com.ae.service.FacturaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@RequestScoped
public class FacturaServiceImpl implements FacturaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacturaServiceImpl.class);
    private FacturaDAO facturaDAO;

    @Inject
    public FacturaServiceImpl(FacturaDAO facturaDAO){
        this.facturaDAO = facturaDAO;
    }

    @Override
    public List<Factura> getAll() {
        LOGGER.info("getAll() => Starting");
        List<Factura> facturas = this.facturaDAO.getInvoices();
        LOGGER.info("getAll() => Complete");
        return facturas;
    }

    @Override
    public Factura getById(Long id) {
        LOGGER.info("getById() => Starting");
        LOGGER.info("getById() => Parameter: Id=\"{}\"", id);
        Factura factura = this.facturaDAO.getInvoiceById(id);
        LOGGER.info("getById() => Complete");
        return factura;
    }

    @Override
    public Factura create(Factura factura) {
        LOGGER.info("create() => Starting");
        BigDecimal total = BigDecimal.ZERO;
        for (FacturaDetalle facturaDetalle : factura.getDetalles()){
            facturaDetalle.setFactura(factura);

            BigDecimal precioUnitario = facturaDetalle.getPrecioUnitario();
            BigDecimal precioFinal = precioUnitario;
            BigDecimal descuento = facturaDetalle.getDescuento();

            if (descuento != null && descuento.compareTo(BigDecimal.ZERO) > 0){
                precioFinal = precioUnitario.subtract(descuento);
            }

            BigDecimal subTotal = precioFinal
                    .multiply(
                            BigDecimal.valueOf(facturaDetalle.getCantidad())
                    );
            facturaDetalle.setSubTotal(subTotal);
            total = total.add(subTotal);
        }
        factura.setTotal(total);
        Factura result = this.facturaDAO.saveInvoice(factura);
        LOGGER.info("create() => Complete");
        return result;
    }

    @Override
    public Factura update(Long id, Factura factura) {
        LOGGER.info("update() => Starting");
        LOGGER.info("update() => Parameter: Id=\"{}\"", id);
        LOGGER.info("update() => Complete");
        return null;
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("delete() => Starting");
        LOGGER.info("delete() => Parameter: Id=\"{}\"", id);
        facturaDAO.deleteInvoice(id);
        LOGGER.info("delete() => Complete");
    }
}

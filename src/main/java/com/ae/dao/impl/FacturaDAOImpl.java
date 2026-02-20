package com.ae.dao.impl;

import com.ae.configuration.JpaConfiguration;
import com.ae.dao.FacturaDAO;
import com.ae.entity.Factura;
import com.ae.entity.FacturaDetalle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequestScoped
public class FacturaDAOImpl implements FacturaDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacturaDAOImpl.class);
    private JpaConfiguration jpaConfiguration;

    @Inject
    public FacturaDAOImpl(JpaConfiguration jpaConfiguration){
        this.jpaConfiguration = jpaConfiguration;
    }

    @Override
    public List<Factura> getInvoices() {
        EntityManager entityManager = this.jpaConfiguration.getEntityManager();
        List<Factura> result = entityManager
                .createQuery("SELECT DISTINCT f FROM Factura f INNER JOIN FETCH f.detalles", Factura.class)
                .getResultList();
        entityManager.close();
        return result;
    }

    @Override
    public Factura getInvoiceById(Long id) {
        EntityManager entityManager = this.jpaConfiguration.getEntityManager();
        Factura result = entityManager
                .createQuery("SELECT DISTINCT f FROM Factura f INNER JOIN FETCH f.detalles WHERE f.id = :id", Factura.class)
                .setParameter("id", id)
                .getSingleResult();
        entityManager.close();
        return result;
    }

    @Override
    public Factura saveInvoice(Factura factura) {
        this.jpaConfiguration.executeInTransaction(em -> {
            em.persist(factura);
            em.flush();
        });
        return factura;
    }

    @Override
    public Factura deleteInvoice(Long id) {
        return null;
    }

    @Override
    public Factura updateInvoice(Long id, Factura invoiceUpdate) {
        Factura[] result = new Factura[1];
        jpaConfiguration.executeInTransaction(em -> {

            Factura persisted = em.createQuery(
                            "SELECT DISTINCT f FROM Factura f INNER JOIN FETCH f.detalles WHERE f.id = :id",
                            Factura.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();

            // Actualizar campos simples
            persisted.setCliente(invoiceUpdate.getCliente());
            persisted.setNumeroFactura(invoiceUpdate.getNumeroFactura());
            persisted.setTotal(invoiceUpdate.getTotal());

            List<FacturaDetalle> nuevosDetalles = invoiceUpdate.getDetalles();

            Map<Long, FacturaDetalle> mapDataPersisted = persisted.getDetalles()
                    .stream().collect(Collectors.toMap(
                            FacturaDetalle::getId,
                            Function.identity()
                    ));

            // Insertar o actualizar
            for (FacturaDetalle nuevo : nuevosDetalles) {

                if (nuevo.getId() == null) {
                    persisted.addDetalle(nuevo);
                } else {
                    FacturaDetalle existente = mapDataPersisted.get(nuevo.getId());
                    if (existente == null) {
                        throw new RuntimeException("Detalle inconsistente");
                    }
                    existente.setProducto(nuevo.getProducto());
                    existente.setCantidad(nuevo.getCantidad());
                    existente.setPrecioUnitario(nuevo.getPrecioUnitario());
                    existente.setDescuento(nuevo.getDescuento());
                    existente.setSubTotal(nuevo.getSubTotal());

                    mapDataPersisted.remove(nuevo.getId());
                }
            }
            mapDataPersisted.values().forEach(persisted::removeDetalle);
            result[0] = persisted;
        });
        return result[0];
    }
}
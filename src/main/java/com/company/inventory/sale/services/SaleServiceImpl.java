package com.company.inventory.sale.services;

import com.company.inventory.sale.dao.ISaleDao;
import com.company.inventory.sale.model.Sale;
import com.company.inventory.sale.response.SaleResponseRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements ISaleService {

    @Autowired
    private ISaleDao saleDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<SaleResponseRest> search() {

        SaleResponseRest response = new SaleResponseRest();
        try {
            List<Sale> sales = (List<Sale>) saleDao.findAll();

            response.getSaleResponse().setSale(sales);
            response.setMetadata("Respuesta ok", "00", "Ventas obtenidas correctamente");
        } catch (Exception e) {
            response.setMetadata("Respuesta Nok", "-1", "Error al consultar ventas");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<SaleResponseRest> searchById(Long id) {
        SaleResponseRest response = new SaleResponseRest();
        List<Sale> list = new ArrayList<>();
        try {
            Optional<Sale> sale = saleDao.findById(id);
            if (sale.isPresent()) {
                list.add(sale.get());
                response.getSaleResponse().setSale(list);
                response.setMetadata("Respuesta ok", "00", "Venta encontrada");
            } else {
                response.setMetadata("Respuesta Nok", "-1", "Venta no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta Nok", "-1", "Error al consultar venta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<SaleResponseRest> save(Sale sale) {
        SaleResponseRest response = new SaleResponseRest();
        List<Sale> list = new ArrayList<>();
        try {
            Sale saleSaved = saleDao.save(sale);
            list.add(saleSaved);
            response.getSaleResponse().setSale(list);
            response.setMetadata("Respuesta ok", "00", "Venta registrada correctamente");
        } catch (Exception e) {
            response.setMetadata("Respuesta Nok", "-1", "Error al registrar venta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<SaleResponseRest> update(Sale sale, Long id) {
        SaleResponseRest response = new SaleResponseRest();
        List<Sale> list = new ArrayList<>();
        try {
            Optional<Sale> saleSearch = saleDao.findById(id);
            if (saleSearch.isPresent()) {
                saleSearch.get().setCustomer(sale.getCustomer());
                saleSearch.get().setTotal(sale.getTotal());
                Sale saleUpdated = saleDao.save(saleSearch.get());
                list.add(saleUpdated);
                response.getSaleResponse().setSale(list);
                response.setMetadata("Respuesta ok", "00", "Venta actualizada correctamente");
            } else {
                response.setMetadata("Respuesta nok", "-1", "Venta no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta nok", "-1", "Error al actualizar venta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<SaleResponseRest> deleteById(Long id) {
        SaleResponseRest response = new SaleResponseRest();
        try {
            saleDao.deleteById(id);
            response.setMetadata("Respuesta ok", "00", "Venta eliminada correctamente");
        } catch (Exception e) {
            response.setMetadata("Respuesta nok", "-1", "Error al eliminar venta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
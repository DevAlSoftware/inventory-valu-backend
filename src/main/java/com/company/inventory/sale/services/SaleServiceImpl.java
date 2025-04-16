package com.company.inventory.sale.services;

import com.company.inventory.customers.dao.ICustomerDao;
import com.company.inventory.customers.model.Customer;
import com.company.inventory.products.dao.IProductDao;
import com.company.inventory.products.model.Product;
import com.company.inventory.sale.dao.ISaleDao;
import com.company.inventory.sale.model.Sale;
import com.company.inventory.sale.response.SaleResponseRest;
import com.company.inventory.saleDetail.model.SaleDetail;
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

    @Autowired
    private ICustomerDao customerDao;

    @Autowired
    private IProductDao productDao;

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
            // Asociar la venta a cada detalle
            if (sale.getSaleDetails() != null) {
                for (SaleDetail detail : sale.getSaleDetails()) {
                    detail.setSale(sale);
                }
            }

            // Guardar la venta (y en cascada los detalles si está bien mapeado)
            Sale saleSaved = saleDao.save(sale);

            // Recargar el customer completo
            Customer customerFull = customerDao.findById(saleSaved.getCustomer().getId()).orElse(null);
            saleSaved.setCustomer(customerFull);

            // Recargar cada producto completo en los detalles y actualizar stock
            for (SaleDetail detail : saleSaved.getSaleDetails()) {
                Product productFull = productDao.findById(detail.getProduct().getId()).orElse(null);
                if (productFull != null) {
                    // Restar la cantidad vendida del stock
                    int updatedStock = productFull.getAccount() - detail.getQuantity();
                    productFull.setAccount(updatedStock);

                    // Guardar el producto con el stock actualizado
                    productDao.save(productFull);
                }
                detail.setProduct(productFull);
            }

            list.add(saleSaved);
            response.getSaleResponse().setSale(list);
            response.setMetadata("Respuesta ok", "00", "Venta registrada correctamente");

        } catch (Exception e) {
            e.printStackTrace();
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
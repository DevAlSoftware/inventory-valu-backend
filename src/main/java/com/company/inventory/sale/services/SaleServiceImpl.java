package com.company.inventory.sale.services;

import com.company.inventory.customers.dao.ICustomerDao;
import com.company.inventory.customers.model.Customer;
import com.company.inventory.products.dao.IProductDao;
import com.company.inventory.products.model.Product;
import com.company.inventory.productsSize.dao.IProductSizeDao;
import com.company.inventory.productsSize.model.ProductSize;
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

    @Autowired
    private IProductSizeDao productSizeDao;

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
    @Transactional(readOnly = true)
    public ResponseEntity<SaleResponseRest> searchSalesByProductId(Long productId) {

        SaleResponseRest response = new SaleResponseRest();
        List<Sale> salesList = new ArrayList<>();

        try {
            salesList = saleDao.findSalesByProductId(productId);

            if (!salesList.isEmpty()) {
                response.getSaleResponse().setSale(salesList);
                response.setMetadata("Respuesta OK", "00", "Ventas encontradas con el producto");
            } else {
                response.setMetadata("Respuesta Nok", "-1", "No se encontraron ventas con ese producto");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setMetadata("Respuesta Nok", "-1", "Error al buscar ventas por producto");
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
            double totalVenta = 0;

            if (sale.getSaleDetails() != null) {
                for (SaleDetail detail : sale.getSaleDetails()) {
                    detail.setSale(sale);

                    // Cargar talla y producto
                    ProductSize productSize = productSizeDao.findById(detail.getProductSize().getId()).orElse(null);
                    if (productSize == null) throw new RuntimeException("Talla no encontrada");

                    Product product = productSize.getProduct();
                    if (product == null) throw new RuntimeException("Producto no encontrado");

                    // Establecer el producto
                    detail.setProduct(product);

                    // Setear el precio si viene null (por si el front lo olvidó)
                    if (detail.getPrice() == null) {
                        detail.setPrice(product.getRetail()); // o wholesalePrice según el tipo
                    }

                    // Calcular subtotal, ganancia y total
                    double subtotal = detail.getPrice() * detail.getQuantity();
                    double ganancia = subtotal * (detail.getProfitPercentage() / 100);
                    double total = subtotal + ganancia;

                    detail.setSubtotalSinGanancia(subtotal);
                    detail.setGanancia(ganancia);
                    detail.setTotal(total);

                    totalVenta += total;

                    // Descontar del stock de esa talla
                    int stockActual = productSize.getAccount();
                    int nuevoStock = stockActual - detail.getQuantity();
                    if (nuevoStock < 0) throw new RuntimeException("Stock insuficiente");

                    productSize.setAccount(nuevoStock);
                    productSizeDao.save(productSize);
                    detail.setProductSize(productSize);
                }
            }

            sale.setTotal(totalVenta);

            Sale saleSaved = saleDao.save(sale);
            Customer customerFull = customerDao.findById(saleSaved.getCustomer().getId()).orElse(null);
            saleSaved.setCustomer(customerFull);

            list.add(saleSaved);
            response.getSaleResponse().setSale(list);
            response.setMetadata("Respuesta ok", "00", "Venta registrada correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            response.setMetadata("Respuesta Nok", "-1", "Error al registrar venta: " + e.getMessage());
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
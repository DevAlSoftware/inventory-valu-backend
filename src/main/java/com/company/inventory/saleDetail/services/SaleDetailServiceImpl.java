package com.company.inventory.saleDetail.services;

import com.company.inventory.products.dao.IProductDao;
import com.company.inventory.products.enums.PriceType;
import com.company.inventory.products.model.Product;
import com.company.inventory.productsSize.dao.IProductSizeDao;
import com.company.inventory.productsSize.model.ProductSize;
import com.company.inventory.saleDetail.dao.ISaleDetailDao;
import com.company.inventory.saleDetail.model.SaleDetail;
import com.company.inventory.saleDetail.response.SaleDetailResponseRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleDetailServiceImpl implements ISaleDetailService {

    @Autowired
    private ISaleDetailDao saleDetailDao;

    @Autowired
    private IProductDao productDao;

    @Autowired
    private IProductSizeDao productSizeDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<SaleDetailResponseRest> search() {
        SaleDetailResponseRest response = new SaleDetailResponseRest();
        try {
            List<SaleDetail> saleDetails = (List<SaleDetail>) saleDetailDao.findAll();
            response.getSaleDetailResponse().setSaleDetail(saleDetails);
            response.setMetadata("Respuesta ok", "00", "Detalles de venta obtenidos correctamente");
        } catch (Exception e) {
            response.setMetadata("Respuesta Nok", "-1", "Error al consultar detalles de venta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<SaleDetailResponseRest> searchById(Long id) {
        SaleDetailResponseRest response = new SaleDetailResponseRest();
        List<SaleDetail> list = new ArrayList<>();

        try {
            Optional<SaleDetail> saleDetail = saleDetailDao.findById(id);

            if (saleDetail.isPresent()) {
                list.add(saleDetail.get());
                response.getSaleDetailResponse().setSaleDetail(list);
                response.setMetadata("Respuesta OK", "00", "Detalle de venta encontrado");
            } else {
                response.setMetadata("Respuesta NOK", "-1", "Detalle de venta no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta NOK", "-1", "Error al consultar detalle de venta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<SaleDetailResponseRest> save(SaleDetail saleDetail) {
        SaleDetailResponseRest response = new SaleDetailResponseRest();
        List<SaleDetail> list = new ArrayList<>();

        try {
            ProductSize productSize = saleDetail.getProductSize();

            if (productSize.getAccount() >= saleDetail.getQuantity()) {
                // Descontar stock
                productSize.setAccount(productSize.getAccount() - saleDetail.getQuantity());
                productSizeDao.save(productSize);

                // Obtener el precio según el tipo
                Product product = productSize.getProduct();
                double unitPrice = 0.0;
                if (saleDetail.getPriceType() == PriceType.RETAIL) {
                    unitPrice = product.getRetail();
                } else if (saleDetail.getPriceType() == PriceType.WHOLESALER) {
                    unitPrice = product.getWholesaler();
                }

                // Calcular montos
                double subtotal = unitPrice * saleDetail.getQuantity();
                double ganancia = subtotal * saleDetail.getProfitPercentage();
                double total = subtotal + ganancia;

                saleDetail.setPrice(unitPrice);
                saleDetail.setSubtotalSinGanancia(subtotal);
                saleDetail.setGanancia(ganancia);
                saleDetail.setTotal(total);

                // 🔥 Esta línea es la que FALTABA: usa el price (precio de costo sin ganancia)
                double subtotalSinGanancia = product.getPrice() * saleDetail.getQuantity();
                saleDetail.setSubtotalSinGanancia(subtotalSinGanancia);

                SaleDetail saved = saleDetailDao.save(saleDetail);
                list.add(saved);
                response.getSaleDetailResponse().setSaleDetail(list);
                response.setMetadata("OK", "00", "Detalle guardado correctamente");
            } else {
                response.setMetadata("Error", "-1", "Stock insuficiente para la talla seleccionada");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            response.setMetadata("Error", "-1", "Error al guardar: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<SaleDetailResponseRest> update(SaleDetail saleDetail, Long id) {
        SaleDetailResponseRest response = new SaleDetailResponseRest();
        List<SaleDetail> list = new ArrayList<>();

        try {
            Optional<SaleDetail> existingOpt = saleDetailDao.findById(id);

            if (existingOpt.isPresent()) {
                SaleDetail existing = existingOpt.get();

                // 1. Devolver stock anterior
                ProductSize oldSize = existing.getProductSize();
                oldSize.setAccount(oldSize.getAccount() + existing.getQuantity());
                productSizeDao.save(oldSize);

                // 2. Verificar nueva talla y descontar
                ProductSize newSize = saleDetail.getProductSize();

                if (newSize.getAccount() >= saleDetail.getQuantity()) {
                    newSize.setAccount(newSize.getAccount() - saleDetail.getQuantity());
                    productSizeDao.save(newSize);

                    // Actualizar campos
                    double subtotal = saleDetail.getPrice() * saleDetail.getQuantity();
                    double ganancia = subtotal * saleDetail.getProfitPercentage();
                    double total = subtotal + ganancia;

                    existing.setSale(saleDetail.getSale());
                    existing.setProductSize(saleDetail.getProductSize());
                    existing.setQuantity(saleDetail.getQuantity());
                    existing.setPrice(saleDetail.getPrice());
                    existing.setProfitPercentage(saleDetail.getProfitPercentage());
                    existing.setSubtotalSinGanancia(subtotal);
                    existing.setGanancia(ganancia);
                    existing.setTotal(total);

                    SaleDetail updated = saleDetailDao.save(existing);
                    list.add(updated);
                    response.getSaleDetailResponse().setSaleDetail(list);
                    response.setMetadata("OK", "00", "Detalle actualizado correctamente");

                } else {
                    response.setMetadata("Error", "-1", "Stock insuficiente en la talla nueva");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }

            } else {
                response.setMetadata("Error", "-1", "Detalle no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            response.setMetadata("Error", "-1", "Error al actualizar: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<SaleDetailResponseRest> deleteById(Long id) {
        SaleDetailResponseRest response = new SaleDetailResponseRest();

        try {
            Optional<SaleDetail> existingOpt = saleDetailDao.findById(id);

            if (existingOpt.isPresent()) {
                SaleDetail detail = existingOpt.get();

                // Devolver stock
                ProductSize productSize = detail.getProductSize();
                productSize.setAccount(productSize.getAccount() + detail.getQuantity());
                productSizeDao.save(productSize);

                // Eliminar
                saleDetailDao.deleteById(id);
                response.setMetadata("OK", "00", "Detalle eliminado correctamente");

            } else {
                response.setMetadata("Error", "-1", "Detalle no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            response.setMetadata("Error", "-1", "Error al eliminar: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SaleDetailResponseRest> findBySaleId(Long saleId) {
        SaleDetailResponseRest response = new SaleDetailResponseRest();

        try {
            List<SaleDetail> saleDetails = saleDetailDao.findBySaleId(saleId);
            response.getSaleDetailResponse().setSaleDetail(saleDetails);
            response.setMetadata("00", "Success", "Detalles encontrados");
        } catch (Exception e) {
            response.setMetadata("01", "Error", "Error al obtener detalles por venta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
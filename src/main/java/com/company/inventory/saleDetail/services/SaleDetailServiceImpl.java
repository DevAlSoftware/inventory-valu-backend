package com.company.inventory.saleDetail.services;

import com.company.inventory.products.dao.IProductDao;
import com.company.inventory.products.model.Product;
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
            Optional<Product> productOptional = productDao.findById(saleDetail.getProduct().getId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                if (product.getAccount() >= saleDetail.getQuantity()) {
                    // Restar el stock
                    product.setAccount(product.getAccount() - saleDetail.getQuantity());
                    productDao.save(product);

                    // 💡 Cálculos clave
                    double subtotal = saleDetail.getPrice() * saleDetail.getQuantity();
                    double totalConGanancia = subtotal + (subtotal * saleDetail.getProfitPercentage());

                    saleDetail.setSubtotalSinGanancia(subtotal);
                    saleDetail.setGanancia(totalConGanancia - subtotal);
                    saleDetail.setTotal(totalConGanancia);

                    SaleDetail saleDetailSaved = saleDetailDao.save(saleDetail);
                    list.add(saleDetailSaved);
                    response.getSaleDetailResponse().setSaleDetail(list);
                    response.setMetadata("Respuesta ok", "00", "Detalle de venta registrado correctamente");
                } else {
                    response.setMetadata("Respuesta Nok", "-1", "Stock insuficiente para la venta");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                response.setMetadata("Respuesta Nok", "-1", "Producto no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta Nok", "-1", "Error al registrar detalle de venta");
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
            Optional<SaleDetail> saleDetailOptional = saleDetailDao.findById(id);

            if (saleDetailOptional.isPresent()) {
                SaleDetail saleDetailToUpdate = saleDetailOptional.get();

                // Restaurar stock anterior
                Optional<Product> productOptional = productDao.findById(saleDetailToUpdate.getProduct().getId());
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    product.setAccount(product.getAccount() + saleDetailToUpdate.getQuantity());
                    productDao.save(product);
                }

                // Actualizar datos
                saleDetailToUpdate.setQuantity(saleDetail.getQuantity());
                saleDetailToUpdate.setPrice(saleDetail.getPrice());
                saleDetailToUpdate.setProfitPercentage(saleDetail.getProfitPercentage());
                saleDetailToUpdate.setProduct(saleDetail.getProduct());

                // Ajustar stock con nueva cantidad
                Optional<Product> newProductOptional = productDao.findById(saleDetail.getProduct().getId());
                if (newProductOptional.isPresent()) {
                    Product newProduct = newProductOptional.get();
                    if (newProduct.getAccount() >= saleDetail.getQuantity()) {
                        newProduct.setAccount(newProduct.getAccount() - saleDetail.getQuantity());
                        productDao.save(newProduct);
                    } else {
                        response.setMetadata("Respuesta NOK", "-2", "Stock insuficiente para la actualización");
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    }
                }

                // 💡 Recalcular campos
                double subtotal = saleDetail.getPrice() * saleDetail.getQuantity();
                double totalConGanancia = subtotal + (subtotal * saleDetail.getProfitPercentage());

                saleDetailToUpdate.setSubtotalSinGanancia(subtotal);
                saleDetailToUpdate.setGanancia(totalConGanancia - subtotal);
                saleDetailToUpdate.setTotal(totalConGanancia);

                // Guardar
                SaleDetail updatedSaleDetail = saleDetailDao.save(saleDetailToUpdate);
                list.add(updatedSaleDetail);
                response.getSaleDetailResponse().setSaleDetail(list);
                response.setMetadata("Respuesta OK", "00", "Detalle de venta actualizado");

            } else {
                response.setMetadata("Respuesta NOK", "-1", "Detalle de venta no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta NOK", "-1", "Error al actualizar el detalle de venta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<SaleDetailResponseRest> deleteById(Long id) {
        SaleDetailResponseRest response = new SaleDetailResponseRest();

        try {
            Optional<SaleDetail> saleDetailOptional = saleDetailDao.findById(id);

            if (saleDetailOptional.isPresent()) {
                SaleDetail saleDetail = saleDetailOptional.get();

                // Restaurar el stock del producto antes de eliminar
                Optional<Product> productOptional = productDao.findById(saleDetail.getProduct().getId());
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    product.setAccount(product.getAccount() + saleDetail.getQuantity()); // Devolver stock
                    productDao.save(product);
                }

                saleDetailDao.deleteById(id);
                response.setMetadata("Respuesta OK", "00", "Detalle de venta eliminado");
            } else {
                response.setMetadata("Respuesta NOK", "-1", "Detalle de venta no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta NOK", "-1", "Error al eliminar el detalle de venta");
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
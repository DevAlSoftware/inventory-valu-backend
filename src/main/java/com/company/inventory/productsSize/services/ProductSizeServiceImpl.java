package com.company.inventory.productsSize.services;

import com.company.inventory.products.dao.IProductDao;
import com.company.inventory.products.model.Product;
import com.company.inventory.products.response.ProductResponseRest;
import com.company.inventory.productsSize.dao.IProductSizeDao;
import com.company.inventory.productsSize.model.ProductSize;
import com.company.inventory.productsSize.response.ProductSizeResponseRest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductSizeServiceImpl implements IProductSizeService {

    private IProductDao productDao;
    private IProductSizeDao productSizeDao;

    public ProductSizeServiceImpl(IProductDao productDao, IProductSizeDao productSizeDao) {
        this.productDao = productDao;
        this.productSizeDao = productSizeDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductSizeResponseRest> searchById(Long id) {

        ProductSizeResponseRest response = new ProductSizeResponseRest();
        List<ProductSize> list = new ArrayList<>();

        try {

            //search product size by id
            Optional<ProductSize> productSize = productSizeDao.findById(id);

            if (productSize.isPresent()) {
                list.add(productSize.get());
                response.getProductSizes().setProductSizes(list);
                response.setMetadata("Respuesta ok", "00", "Talla encontrada");
            } else {
                response.setMetadata("respuesta nok", "-1", "Talla no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("respuesta nok", "-1", "Error al buscar talla por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ProductSizeResponseRest> save(ProductSize productSize, Long productId) {

        ProductSizeResponseRest response = new ProductSizeResponseRest();
        List<ProductSize> list = new ArrayList<>();

        try {

            //search product to set in the productSize object
            Optional<Product> product = productDao.findById(productId);

            if (product.isPresent()) {
                productSize.setProduct(product.get());
            } else {
                response.setMetadata("respuesta nok", "-1", "Producto no encontrado para asociar talla");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            //save the productSize
            ProductSize savedProductSize = productSizeDao.save(productSize);

            if (savedProductSize != null) {
                list.add(savedProductSize);
                response.getProductSizes().setProductSizes(list);
                response.setMetadata("respuesta ok", "00", "Talla guardada");
            } else {
                response.setMetadata("respuesta nok", "-1", "Talla no guardada");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("respuesta nok", "-1", "Error al guardar talla");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductSizeResponseRest> deleteById(Long id) {
        ResponseEntity<ProductSizeResponseRest> response = new ResponseEntity<ProductSizeResponseRest>(HttpStatus.INTERNAL_SERVER_ERROR);

        try {
            Optional<ProductSize> tallaOpt = productSizeDao.findById(id);
            if (tallaOpt.isPresent()) {
                ProductSize talla = tallaOpt.get();
                Product producto = talla.getProduct();

                // Elimina la talla
                productSizeDao.deleteById(id);

                // Actualiza el account del producto
                List<ProductSize> tallasRestantes = productSizeDao.findByProductId(producto.getId());
                int total = tallasRestantes.stream()
                        .mapToInt(ProductSize::getAccount)
                        .sum();

                producto.setAccount(total);
                productDao.save(producto);

                response = new ResponseEntity<ProductSizeResponseRest>(HttpStatus.OK);
            } else {
                response = new ResponseEntity<ProductSizeResponseRest>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductSizeResponseRest> search() {
        ProductSizeResponseRest response = new ProductSizeResponseRest();
        try {
            List<ProductSize> sizes = (List<ProductSize>) productSizeDao.findAll();
            if (!sizes.isEmpty()) {
                response.getProductSizes().setProductSizes(sizes);
                response.setMetadata("respuesta ok", "00", "Tallas encontradas");
            } else {
                response.setMetadata("respuesta nok", "-1", "Tallas no encontradas");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMetadata("respuesta nok", "-1", "Error al buscar tallas");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    @Transactional
    public ResponseEntity<ProductSizeResponseRest> update(ProductSize productSize, Long productId, Long id) {
        ProductSizeResponseRest response = new ProductSizeResponseRest();
        try {
            Optional<Product> product = productDao.findById(productId);
            if (product.isPresent()) {
                Optional<ProductSize> sizeSearch = productSizeDao.findById(id);
                if (sizeSearch.isPresent()) {
                    sizeSearch.get().setSize(productSize.getSize());
                    sizeSearch.get().setAccount(productSize.getAccount());
                    sizeSearch.get().setProduct(product.get());
                    productSizeDao.save(sizeSearch.get());
                    response.setMetadata("respuesta ok", "00", "Talla actualizada");
                } else {
                    response.setMetadata("respuesta nok", "-1", "Talla no encontrada");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
            } else {
                response.setMetadata("respuesta nok", "-1", "Producto no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMetadata("respuesta nok", "-1", "Error al actualizar talla");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductSizeResponseRest> searchByProductId(Long productId) {
        ProductSizeResponseRest response = new ProductSizeResponseRest();
        List<ProductSize> list = new ArrayList<>();

        try {
            list = productSizeDao.findByProductId(productId);
            if (!list.isEmpty()) {
                response.getProductSizes().setProductSizes(list);
                response.setMetadata("respuesta ok", "00", "Tallas encontradas para el producto");
            } else {
                response.setMetadata("respuesta nok", "-1", "No hay tallas para este producto");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMetadata("respuesta nok", "-1", "Error al buscar tallas del producto");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

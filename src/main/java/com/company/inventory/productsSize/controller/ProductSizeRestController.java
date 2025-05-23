package com.company.inventory.productsSize.controller;

import com.company.inventory.productsSize.model.ProductSize;
import com.company.inventory.productsSize.response.ProductSizeResponseRest;
import com.company.inventory.productsSize.services.IProductSizeService;
import com.company.inventory.util.ProductSizeExcelExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = {"http://localhost:4200", "http://69.62.67.49:4200"})
@RestController
@RequestMapping("/api/v1")
public class ProductSizeRestController {

    private final IProductSizeService productSizeService;

    public ProductSizeRestController(IProductSizeService productSizeService) {
        this.productSizeService = productSizeService;
    }

    // Guardar talla para un producto
    @PostMapping("/product-sizes")
    public ResponseEntity<ProductSizeResponseRest> save(
                                                        @RequestParam("size") String size,
                                                        @RequestParam("account") int account,
                                                        @RequestParam("productId") Long productId
    )throws IOException {

        ProductSize productSize = new ProductSize();
        productSize.setSize(size);
        productSize.setAccount(account);

        ResponseEntity<ProductSizeResponseRest> response = productSizeService.save(productSize, productId);

        return response;
    }

    // Buscar por ID
    @GetMapping("/product-sizes/{id}")
    public ResponseEntity<ProductSizeResponseRest> searchById(@PathVariable Long id) {
        return productSizeService.searchById(id);
    }

    // Eliminar por ID
    @DeleteMapping("/product-sizes/{id}")
    public ResponseEntity<ProductSizeResponseRest> deleteById(@PathVariable Long id) {
        return productSizeService.deleteById(id);
    }

    // Buscar todos
    @GetMapping("/product-sizes")
    public ResponseEntity<ProductSizeResponseRest> search() {
        return productSizeService.search();
    }

    // Actualizar por ID
    @PostMapping("/product-sizes/{id}")
    public ResponseEntity<ProductSizeResponseRest> update(
            @RequestParam("account") int account,
            @RequestParam("size") String size,
            @RequestParam("productId") Long productId,
            @PathVariable Long id
            ) throws IOException {

        ProductSize productSize = new ProductSize();
        productSize.setSize(size);
        productSize.setAccount(account);

        ResponseEntity<ProductSizeResponseRest> response = productSizeService.update(productSize, productId, id);

        return response;
    }

    // Buscar tallas por ID de producto
    @GetMapping("/product-sizes/product/{productId}")
    public ResponseEntity<ProductSizeResponseRest> searchByProductId(@PathVariable Long productId) {
        return productSizeService.searchByProductId(productId);
    }

    @GetMapping("/product-sizes/product-name/{productName}")
    public ResponseEntity<ProductSizeResponseRest> searchByProductName(@PathVariable String productName) {
        return productSizeService.searchByProductName(productName);
    }

    /**
     * export product in excel file
     * @param response
     * @throws IOException
     */
    @GetMapping("/product-sizes/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=result_product.xlsx";
        response.setHeader(headerKey, headerValue);

        ResponseEntity<ProductSizeResponseRest> products = productSizeService.search();

        ProductSizeExcelExporter excelExporter = new ProductSizeExcelExporter(
                products.getBody().getProductSizes().getProductSizes());

        excelExporter.export(response);


    }

}

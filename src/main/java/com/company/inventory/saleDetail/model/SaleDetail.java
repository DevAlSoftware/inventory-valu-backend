package com.company.inventory.saleDetail.model;

import com.company.inventory.products.enums.PriceType;
import com.company.inventory.products.model.Product;
import com.company.inventory.productsSize.model.ProductSize;
import com.company.inventory.sale.model.Sale;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sale_details")
public class SaleDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    @JsonBackReference
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_size_id", nullable = false)
    private ProductSize productSize;

    private Integer quantity;
    private Double price;
    private Double profitPercentage;
    private Double total;

    @Column(name = "subtotal_sin_ganancia")
    private Double subtotalSinGanancia;
    private Double ganancia;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getProfitPercentage() {
        return profitPercentage;
    }

    public void setProfitPercentage(Double profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getSubtotalSinGanancia() {
        return subtotalSinGanancia;
    }

    public void setSubtotalSinGanancia(Double subtotalSinGanancia) {
        this.subtotalSinGanancia = subtotalSinGanancia;
    }

    public Double getGanancia() {
        return ganancia;
    }

    public void setGanancia(Double ganancia) {
        this.ganancia = ganancia;
    }

    public ProductSize getProductSize() {
        return productSize;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }
}



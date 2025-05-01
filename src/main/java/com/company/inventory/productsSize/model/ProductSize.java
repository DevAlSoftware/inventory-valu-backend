package com.company.inventory.productsSize.model;

import com.company.inventory.products.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name="product_size")
public class ProductSize implements Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -6561797637918588494L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;

    private int account;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

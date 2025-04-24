package com.company.inventory.products.model;

import com.company.inventory.categories.model.Category;
import com.company.inventory.productsSize.model.ProductSize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name="product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = -1573552508413183887L;
    /**
     *
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    private String ubication;

    private Double retail;

    private Double wholesaler;

    private int price;

    private int account;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties("product")
    private List<ProductSize> sizes = new ArrayList<>();

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column( name ="picture", columnDefinition = "longblob")
    private byte[] picture;

    public Product() {
    }

    public Product(Long id, String name, String code, String ubication, Double retail, Double wholesaler, int price, int account, Category category, List<ProductSize> sizes, byte[] picture) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.ubication = ubication;
        this.retail = retail;
        this.wholesaler = wholesaler;
        this.price = price;
        this.account = account;
        this.category = category;
        this.sizes = sizes;
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUbication() {
        return ubication;
    }

    public void setUbication(String ubication) {
        this.ubication = ubication;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public double getRetail() {
        return retail;
    }

    public void setRetail(double retail) {
        this.retail = retail;
    }

    public double getWholesaler() {
        return wholesaler;
    }

    public void setWholesaler(double wholesaler) {
        this.wholesaler = wholesaler;
    }

    public List<ProductSize> getSizes() {
        return sizes;
    }

    public void setSizes(List<ProductSize> sizes) {
        this.sizes = sizes;
    }
}

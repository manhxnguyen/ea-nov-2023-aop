package miu.ea.aop.service;

import lombok.AllArgsConstructor;
import miu.ea.aop.model.Product;
import miu.ea.aop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product existProduct = productRepository.findById(id).orElse(null);
        if(existProduct == null) return null;
        existProduct.setName(product.getName());
        existProduct.setPrice(product.getPrice());
        existProduct.setRating(product.getRating());
        return productRepository.save(existProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

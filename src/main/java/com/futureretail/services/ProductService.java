package com.futureretail.services;

import com.futureretail.dto.response.ProductSnapshotResponse;
import com.futureretail.models.repositories.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductSnapshotResponse> getActiveProductsBySkus(List<String> skus) {
        log.debug("Fetching active products for SKUs: {}", skus);
        if (skus == null || skus.isEmpty()) {
            log.warn("Empty SKU list provided");
            return List.of();
        }
        return productRepository.findActiveProductsBySkus(skus)
                .stream()
                .map(ProductSnapshotResponse::fromEntity)
                .collect(Collectors.toList());
    }
}

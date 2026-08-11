package com.futureretail.grpc;

import com.futureretail.dto.response.ProductSnapshotResponse;
import com.futureretail.services.ProductService;
import io.grpc.stub.StreamObserver;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceGrpcImpl extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductService productService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void getProductsBySkus(SkuListRequest request, StreamObserver<ProductListResponse> responseObserver) {
        try {
            log.info("gRPC request to get products for {} SKUs", request.getSkusList().size());
            List<ProductSnapshotResponse> products = productService.getActiveProductsBySkus(request.getSkusList());
            ProductListResponse.Builder responseBuilder = ProductListResponse.newBuilder();
            for (ProductSnapshotResponse product : products) {
                com.futureretail.grpc.Product grpcProduct = com.futureretail.grpc.Product.newBuilder()
                        .setId(product.getId() != null ? product.getId() : 0L)
                        .setSku(product.getSku() != null ? product.getSku() : "")
                        .setName(product.getName() != null ? product.getName() : "")
                        .setDescription(product.getDescription() != null ? product.getDescription() : "")
                        .setPrice(product.getPrice() != null ? product.getPrice().toPlainString() : "0")
                        .setCategory(product.getCategory() != null ? product.getCategory() : "")
                        .setStatus(product.getStatus() != null ? product.getStatus() : "UNKNOWN")
                        .setCreatedAt(product.getCreatedAt() != null ? product.getCreatedAt().format(DATE_FORMATTER) : "")
                        .setUpdatedAt(product.getUpdatedAt() != null ? product.getUpdatedAt().format(DATE_FORMATTER) : "")
                        .build();
                responseBuilder.addProducts(grpcProduct);
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            log.info("gRPC response sent with {} products", products.size());
        } catch (Exception e) {
            log.error("Error in gRPC getProductsBySkus", e);
            responseObserver.onError(e);
        }
    }
}

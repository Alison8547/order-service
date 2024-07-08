package com.br.orderservice.service;

import com.br.orderservice.client.InventoryClient;
import com.br.orderservice.dto.OrderRequest;
import com.br.orderservice.dto.OrderResponse;
import com.br.orderservice.exception.BusinessException;
import com.br.orderservice.model.Order;
import com.br.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderResponse placeOrder(OrderRequest orderRequest) {

        if (inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity())) {
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .skuCode(orderRequest.skuCode())
                    .price(orderRequest.price())
                    .quantity(orderRequest.quantity())
                    .build();

            orderRepository.save(order);
            log.info("Order Placed: {}", order.getOrderNumber());
            return mapToResponse(order);
        } else {
            throw new BusinessException("Product with SkuCode " + orderRequest.skuCode() + " is not in stock");
        }

    }


    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderNumber(), order.getSkuCode(), order.getPrice(), order.getQuantity());
    }
}

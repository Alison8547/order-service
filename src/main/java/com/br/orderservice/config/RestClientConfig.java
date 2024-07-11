package com.br.orderservice.config;

import com.br.orderservice.client.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value(value = "${inventory.url}")
    private String urlInventory;

    @Bean
    public InventoryClient inventoryClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(urlInventory)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);

        var httpServiceProxy = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return httpServiceProxy.createClient(InventoryClient.class);
    }
}

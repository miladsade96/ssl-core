package tech.miladsadeghi.sslcore.configurations;


import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;
import java.time.Duration;

@Configuration
@SuppressWarnings("all")
public class RestTemplateConfig {

    @Value("${gateway.ssl.key-store}")
    private Resource keyStore;

    @Value("${gateway.ssl.key-store-password}")
    private String keyStorePassword;

    @Value("${gateway.ssl.trust-store}")
    private Resource trustStore;

    @Value("${gateway.ssl.trust-store-password}")
    private String trustStorePassword;

    @Bean
    public RestTemplate restTemplate() throws Exception {

        // Load KeyStore (client certificate)
        KeyStore clientKeyStore = KeyStore.getInstance("JKS");
        clientKeyStore.load(
                keyStore.getInputStream(),
                keyStorePassword.toCharArray()
        );

        // Load TrustStore (CA certificate)
        KeyStore clientTrustStore = KeyStore.getInstance("JKS");
        clientTrustStore.load(
                trustStore.getInputStream(),
                trustStorePassword.toCharArray()
        );

        // Create SSL Context
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(
                        clientKeyStore,
                        keyStorePassword.toCharArray()
                )
                .loadTrustMaterial(clientTrustStore, null)
                .build();

        // Create SSL Connection Socket Factory with HttpClient 5.x
        SSLConnectionSocketFactory socketFactory =
                SSLConnectionSocketFactoryBuilder.create()
                        .setSslContext(sslContext)
                        .setTlsVersions(TLS.V_1_2, TLS.V_1_3)
                        .build();

        // Create HTTP Client using HttpClient 5.x
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(socketFactory)
                        .build())
                .build();

        // Create Request Factory
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        // Set timeouts using the new method
        requestFactory.setConnectTimeout(Duration.ofSeconds(5));
        requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(5));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        return new RestTemplate(requestFactory);
    }
}

package org.wikitolearn.midtier.course.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
public class SslConfiguration {
  @Value("${server.ssl.trust-store-password}")
  private String trustStorePassword;
  @Value("${server.ssl.trust-store}")
  private Resource trustResource;
  @Value("${server.ssl.key-store-password}")
  private String keyStorePassword;
  @Value("${server.ssl.key-password}")
  private String keyPassword;
  @Value("${server.ssl.key-store}")
  private Resource keyStore;

  @Bean
  public RestTemplate restTemplate() throws Exception {
      RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
      return restTemplate;
  }

  private ClientHttpRequestFactory clientHttpRequestFactory() throws Exception {
      return new HttpComponentsClientHttpRequestFactory(httpClient());
  }

  private HttpClient httpClient() throws Exception {
      SSLContext sslcontext =
              SSLContexts.custom().loadTrustMaterial(trustResource.getFile(), trustStorePassword.toCharArray())
                      .loadKeyMaterial(keyStore.getFile(), keyStorePassword.toCharArray(),
                              keyPassword.toCharArray()).build();
      SSLConnectionSocketFactory sslConnectionSocketFactory =
              new SSLConnectionSocketFactory(sslcontext, new NoopHostnameVerifier());
      return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
  }
}

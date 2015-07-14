package org.burnsearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration providing rest templates for use by application components.
 */
@Configuration
public class RestTemplateConfiguration {
  @Bean
  public RestTemplate restTemplate() {
    RestTemplate template = new RestTemplate();
    List<HttpMessageConverter<?>> messageConverters = template.getMessageConverters();
    for (HttpMessageConverter<?> messageConverter : messageConverters) {
      if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
        MappingJackson2HttpMessageConverter jacksonConverter =
            (MappingJackson2HttpMessageConverter) messageConverter;
        List<MediaType> supportedMediaTypes = new ArrayList<>(jacksonConverter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("text", "javascript"));
        jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
      }
    }
    return template;
  }
}

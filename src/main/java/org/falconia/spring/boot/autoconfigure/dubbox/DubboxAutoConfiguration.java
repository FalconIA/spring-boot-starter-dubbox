/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.falconia.spring.boot.autoconfigure.dubbox;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ModuleConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Configuration
@ConditionalOnClass(AnnotationBean.class)
@EnableConfigurationProperties(DubboProperties.class)
public class DubboxAutoConfiguration {

  private static Logger logger = LoggerFactory.getLogger(DubboxAutoConfiguration.class);

  @Bean
  @ConditionalOnMissingBean
  @ConfigurationProperties("spring.dubbo.annotation")
  public AnnotationBean annotationBean(
    @Value("${spring.dubbo.annotation.package:}") String annotationPackage) {
    logger.info("Dubbox: 'annotationBean' for package: {}", annotationPackage);
    AnnotationBean bean = new AnnotationBean();
    bean.setPackage(annotationPackage);
    return bean;
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty("spring.dubbo.application.name")
  @ConfigurationProperties("spring.dubbo.application")
  public ApplicationConfig applicationConfig(DubboProperties dubboProperties) {
    String name = dubboProperties.getApplication().getName();
    logger.info("Dubbox: 'applicationConfig' witch name: {}", name);
    return new ApplicationConfig(name);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty("spring.dubbo.module.name")
  @ConfigurationProperties("spring.dubbo.module")
  public ModuleConfig moduleConfig(DubboProperties dubboProperties) {
    String name = dubboProperties.getModule().getName();
    logger.info("Dubbox: 'moduleConfig' witch name: {}", name);
    return new ModuleConfig(name);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConfigurationProperties("spring.dubbo.registry")
  public RegistryConfig registryConfig(DubboProperties dubboProperties) {
    String address = dubboProperties.getRegistry().getAddress();
    logger.info("Dubbox: 'registryConfig' witch address: {}", address);
    return new RegistryConfig(address);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty("spring.dubbo.protocol.name")
  @ConfigurationProperties("spring.dubbo.protocol")
  public ProtocolConfig protocolConfig(DubboProperties dubboProperties) {
    String name = dubboProperties.getProtocol().getName();
    logger.info("Dubbox: 'protocolConfig' witch name: {}", name);
    return new ProtocolConfig(dubboProperties.getProtocol().getName());
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(ProtocolConfig.class)
  @ConditionalOnProperty("spring.dubbo.provider")
  @ConfigurationProperties("spring.dubbo.provider")
  public ProviderConfig providerConfig(ProtocolConfig protocolConfig) {
    logger.info("Dubbox: 'providerConfig'");
    ProviderConfig providerConfig = new ProviderConfig();
    providerConfig.setProtocol(protocolConfig);
    return providerConfig;
  }

  @Bean
  @ConditionalOnMissingBean
  @ConfigurationProperties("spring.dubbo.consumer")
  public ConsumerConfig consumerConfig() {
    logger.info("Dubbox: 'consumerConfig'");
    return new ConsumerConfig();
  }
}

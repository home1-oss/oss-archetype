package $package;

import static java.util.stream.Collectors.toList;

import cn.home1.oss.boot.autoconfigure.AppProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
//@EnableDiscoveryClient
public class ResourcesService {

  private static final Logger log = LoggerFactory.getLogger(ResourcesService.class);

  public static void main(final String... args) {
    final ApplicationContext ctx = SpringApplication.run(ResourcesService.class, args);
    //final ApplicationContext ctx = new SpringApplicationBuilder(ResourcesService.class).web(true).run(args);

    log.info("Current environment is: " + ctx.getBean(AppProperties.class).getEnv());
    log.info("Let's inspect the beans provided by Spring Boot:");

    final String[] beanNames = ctx.getBeanDefinitionNames();
    for (final String beanName : Arrays.stream(beanNames).sorted().collect(toList())) {
      final Object bean = ctx.getBean(beanName);
      final Class<?> clazz = bean != null ? bean.getClass() : null;
      final String clazzName = clazz != null ? clazz.getName() : "null";
      log.info(beanName + " - " + clazzName);
    }
  }
}

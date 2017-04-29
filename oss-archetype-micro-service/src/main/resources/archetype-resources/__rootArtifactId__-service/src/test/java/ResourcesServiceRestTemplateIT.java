package ${package};

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

import cn.home1.oss.lib.test.RestTemplateForTest;
import cn.home1.oss.lib.test.RestTemplateForTest14x;
import ${package}.entity.Greeting;
import ${package}.service.GreetingService;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test using RestTemplate
 */
@ActiveProfiles("it.env")
@SpringBootTest(classes = {ResourcesService.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"test0=test0"})
@Slf4j
public class ResourcesServiceRestTemplateIT {

  @Autowired
  private ConfigurableEnvironment environment;

  @Value("${local.server.port}")
  private Integer port;

  @MockBean
  private GreetingService greetingService;

  Greeting minnie;

  private RestTemplateForTest template;

  private String url(final String suffix) {
    return "http://127.0.0.1:" + this.port + suffix;
  }

  @Before
  public void setUp() {
    EnvironmentTestUtils.addEnvironment(this.environment, "test1=test1", "test2=test2");

    this.minnie = new Greeting(2L, "minnie", 0L);

    given( //
      this.greetingService.findOne(this.minnie.getId()) //
    ).willReturn( //
      this.minnie //
    );

    this.template = new RestTemplateForTest14x();
  }

  @Test
  public void testConfigurableEnvironment() {
    assertEquals("test0", this.environment.getProperty("test0"));
    assertEquals("test1", this.environment.getProperty("test1"));
    assertEquals("test2", this.environment.getProperty("test2"));
  }

  @Test
  public void canFetchMinnie() {
    final Long minnieId = this.minnie.getId();

    final ResponseEntity<Greeting> responseEntity = this.template.getForEntity( //
      this.url("/api/greetings/{id}"), Greeting.class, minnieId);
    assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    assertNotNull(responseEntity.getBody());
    assertEquals("minnie", responseEntity.getBody().getToWhom());
  }
}

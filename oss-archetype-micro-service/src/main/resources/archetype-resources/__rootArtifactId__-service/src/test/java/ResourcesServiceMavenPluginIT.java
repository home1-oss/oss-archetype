package ${package};

import static com.jayway.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import cn.home1.oss.lib.test.RestTemplateForTest;
import cn.home1.oss.lib.test.RestTemplateForTest14x;
import com.jayway.restassured.RestAssured;
import ${package}.entity.Greeting;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * integration tests using spring-boot-maven-plugin's server.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResourcesServiceMavenPluginIT {

  private Integer port;

  private RestTemplateForTest template;

  Greeting minnie;

  private String url(final String suffix) {
    return "http://127.0.0.1:" + this.port + suffix;
  }

  @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
  @Before
  public void setUp() {
    this.port = Integer.parseInt(System.getProperty("test.server.port"));
    RestAssured.port = this.port;

    this.template = new RestTemplateForTest14x();

    this.minnie = Greeting.greetingBuilder().toWhom("minnie").build();
  }

  @Test
  public void testCanCreateMinnie() {
    final ResponseEntity<Greeting> responseEntity = this.template.postForEntity( //
      this.url("/api/greetings/"), this.minnie, Greeting.class);

    assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    assertNotNull(responseEntity.getBody());
    assertEquals("minnie", responseEntity.getBody().getToWhom());
  }

  @Test
  public void testCanFetchMinnie() {
    when() //
      .get("/api/greetings/{id}", 1L).
      then().
      statusCode(org.apache.http.HttpStatus.SC_OK).
      body("id", Matchers.is(1)).
      body("toWhom", Matchers.is("minnie")).
      body("version", Matchers.is(0));
  }
}

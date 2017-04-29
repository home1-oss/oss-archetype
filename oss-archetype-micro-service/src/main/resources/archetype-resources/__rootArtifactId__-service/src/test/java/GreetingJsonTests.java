package ${package};

import static org.assertj.core.api.Assertions.assertThat;

import cn.home1.oss.lib.common.Jackson2Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import ${package}.entity.Greeting;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

//@RunWith(SpringRunner.class)
//@JsonTest
// Auto-configure Jackson and/or Gson,
// Add any Module or @JsonComponent beans,
// Trigger initialization of any JacksonTester or GsonTester fields
public class GreetingJsonTests {

  //@Rule
  //public OutputCapture capture = new OutputCapture(); // does not work with maven-surefire-plugin?

  private JacksonTester<Greeting> json;

  @Before
  public void setup() {
    final ObjectMapper objectMapper = Jackson2Utils.setupObjectMapper(null, new ObjectMapper());
    // Possibly configure the mapper
    JacksonTester.initFields(this, objectMapper);
  }

  @SuppressFBWarnings({"NP_UNWRITTEN_FIELD", "UWF_UNWRITTEN_FIELD"})
  @Test
  public void serializeJson() throws IOException {
    //assertThat(this.capture.toString()).contains("JACKSON2_HAL_CONFIGURATOR");
    //assertThat(this.capture.toString()).contains("JACKSON2_JAXB_ANNOTATION_CONFIGUATOR");
    //assertThat(this.capture.toString()).contains("JACKSON2_DATETIME_CONFIGURATOR");
    //assertThat(this.capture.toString()).contains("JACKSON2_SPRINGSECURITY_CONFIGURATOR");
    //assertThat(this.capture.toString()).contains("JACKSON2_DEFAULT_CONFIGURATOR");

    final Greeting greeting = new Greeting(1L, "mickey", 0L);

    assertThat(this.json.write(greeting)).isEqualToJson("greeting.json");

    assertThat(this.json.write(greeting))
      .hasJsonPathStringValue("@.toWhom");

    assertThat(this.json.write(greeting))
      .extractingJsonPathStringValue("@.toWhom")
      .isEqualTo("mickey");
  }

  @SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
  @Test
  public void deserializeJson() throws IOException {
    String content = "{\"id\": 1,\"toWhom\": \"mickey\",\"version\": 0}";

    assertThat(this.json.parse(content))
      .isEqualTo(new Greeting(1L, "mickey", 0L));

    assertThat(this.json.parseObject(content).getToWhom())
      .isEqualTo("mickey");
  }
}

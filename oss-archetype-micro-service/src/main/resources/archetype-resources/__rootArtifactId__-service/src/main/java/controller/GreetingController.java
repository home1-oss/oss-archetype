package ${package}.controller;

import static cn.home1.oss.lib.errorhandle.api.ApplicationExceptions.checkArgument;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import ${package}.entity.Greeting;
import ${package}.service.GreetingService;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/greetings")
public class GreetingController {

  @Autowired
  @Setter
  private GreetingService greetingService;

  @RequestMapping(path = "/", method = POST, //
    consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
  @ResponseStatus(CREATED)
  public Greeting create(@RequestBody final Greeting greeting) {
    return this.greetingService.create(greeting);
  }

  @RequestMapping(path = "/{id}", method = DELETE, produces = {APPLICATION_JSON_VALUE})
  @ResponseStatus(NO_CONTENT)
  public void delete(@PathVariable(name = "id") final Long id) {
    this.greetingService.delete(id);
  }

  @RequestMapping(path = "", method = GET, produces = {APPLICATION_JSON_VALUE})
  public Page<Greeting> find(@PageableDefault(size = 20) final Pageable pageable) {
    return this.greetingService.find(pageable);
  }

  @RequestMapping(path = "/{id}", method = GET, produces = {APPLICATION_JSON_VALUE})
  public Greeting findOne(@PathVariable(name = "id") final Long id) {
    return this.greetingService.findOne(id);
  }

  @RequestMapping(path = "/{id}", method = PUT, //
    consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
  @ResponseStatus(ACCEPTED)
  public Greeting update(@PathVariable(name = "id") final Long id, @RequestBody final Greeting greeting) {
    checkArgument(greeting != null, "greeting must not null.");
    checkArgument(greeting.getId() != null, "must provide a greeting id.");
    checkArgument(greeting.getId().equals(id), "id mismatch {} and {}.", greeting.getId(), id);

    return this.greetingService.update(greeting);
  }
}

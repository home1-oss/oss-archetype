package ${package}.service;

import static cn.home1.oss.lib.errorhandle.api.ApplicationExceptions.check;
import static cn.home1.oss.lib.errorhandle.api.ApplicationExceptions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import ${package}.entity.Greeting;
import ${package}.repository.GreetingRepository;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

  @Autowired
  @Setter
  private GreetingRepository greetingRepository;

  /**
   * create a greeting.
   *
   * @param greeting to create
   * @return created
   */
  public Greeting create(final Greeting greeting) {
    checkArgument(greeting != null, "greeting must not null.");
    checkArgument(greeting.getId() == null, "greeting id is assigned by server only, not by client.");
    checkArgument(isNotBlank(greeting.getToWhom()), "greeting must have a target.");
    return this.greetingRepository.save(greeting);
  }

  /**
   * delete a greeting by id.
   *
   * @param id target id
   * @return deleted
   */
  public Greeting delete(final Long id) {
    checkArgument(id != null, "must provide a greeting id.");

    final Greeting found = this.greetingRepository.findOne(id);
    check(found != null, NOT_FOUND, "greeting {} not found", id);

    this.greetingRepository.delete(found);
    return found;
  }

  /**
   * find paged greetings.
   *
   * @param pageable find condition
   * @return paged
   */
  public Page<Greeting> find(final Pageable pageable) {
    return this.greetingRepository.findAll(pageable);
  }

  /**
   * find a greeting.
   *
   * @param id target id
   * @return found
   */
  public Greeting findOne(final Long id) {
    checkArgument(id != null, "must provide a greeting id.");

    final Greeting found = this.greetingRepository.findOne(id);
    check(found != null, NOT_FOUND, "greeting {} not found", id);
    return found;
  }

  /**
   * update a greeting.
   *
   * @param greeting to update
   * @return updated
   */
  public Greeting update(final Greeting greeting) {
    checkArgument(greeting != null, "greeting must not null.");
    checkArgument(greeting.getId() != null, "must provide a greeting id.");
    checkArgument(isNotBlank(greeting.getToWhom()), "greeting must have a target.");

    final Greeting found = this.greetingRepository.findOne(greeting.getId());
    check(found != null, NOT_FOUND, "greeting {} not found", greeting.getId());

    final Greeting toSave = Greeting.greetingBuilder() //
      .id(greeting.getId()) //
      .toWhom(greeting.getToWhom()) //
      .version(greeting.getVersion() != null ? greeting.getVersion() : found.getVersion())
      .build();

    return this.greetingRepository.save(toSave);
  }
}

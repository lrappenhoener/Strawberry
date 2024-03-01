package botanical.harmony.strawberry;

import botanical.harmony.strawberry.helpers.SimpleTestType;
import botanical.harmony.strawberry.helpers.TestTypeWithDependencies;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BuildTests {

  @Test
  void throws_bad_registration_exception_when_trying_build_container_where_type_not_resolvable() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(TestTypeWithDependencies.class);

    assertThrows(BadRegistrationException.class, builder::build);
  }

}
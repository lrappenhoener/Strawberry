package botanical.harmony.strawberry;

import botanical.harmony.strawberry.helpers.TestTypeCyclicA;
import botanical.harmony.strawberry.helpers.TestTypeCyclicB;
import botanical.harmony.strawberry.helpers.TestTypeWithDependencies;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BuildTests {

  @Test
  void throws_bad_registration_exception_when_trying_build_container_where_type_not_resolvable() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(TestTypeWithDependencies.class);

    assertThrows(BadRegistrationException.class, builder::build);
  }

  @Test
  void throws_bad_registration_exception_when_trying_build_container_where_type_has_cyclic_dependency(){
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(TestTypeCyclicA.class)
           .register(TestTypeCyclicB.class);

    assertThrows(BadRegistrationException.class, builder::build);
  }

}
package botanical.harmony.strawberry;

import botanical.harmony.strawberry.helpers.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResolveTests {
  @Test
  void resolve_simple_type() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(SimpleTestType.class);
    Container container = builder.build();

    SimpleTestType result = container.resolve(SimpleTestType.class);

    assertNotNull(result);
    assertSame(result.getClass(), SimpleTestType.class);
  }

  @Test
  void throws_bad_request_exception_when_resolving_unregistered_type() {
    ContainerBuilder builder = ContainerBuilder.create();
    Container container = builder.build();

    assertThrows(BadRequestException.class, () -> container.resolve(SimpleTestType.class));
  }

  @Test
  void throws_bad_registration_exception_when_trying_build_container_where_type_not_resolvable() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(TestTypeWithDependencies.class);

    assertThrows(BadRegistrationException.class, builder::build);
  }

  @Test
  void resolve_type_with_dependencies() {
    ContainerBuilder builder = getContainerBuilder();
    Container container = builder.build();

    TestTypeWithDependencies result = container.resolve(TestTypeWithDependencies.class);

    assertNotNull(result);
    assertSame(result.getClass(), TestTypeWithDependencies.class);
  }

  @Test
  void resolve_type_with_dependencies_by_factory() {
    ContainerBuilder builder = getContainerBuilder();
    builder.register(TestTypeForFactory.class, (c) -> {
      AnotherTestTypeWithDependencies dependencyA = c.resolve(AnotherTestTypeWithDependencies.class);
      SimpleTestType dependencyB = c.resolve(SimpleTestType.class);
      TestTypeWithDependencies dependencyC = c.resolve(TestTypeWithDependencies.class);
      return new TestTypeForFactory(dependencyA, dependencyB, dependencyC);
    });
    Container container = builder.build();

    TestTypeForFactory result = container.resolve(TestTypeForFactory.class);

    assertNotNull(result);
    assertSame(result.getClass(), TestTypeForFactory.class);
  }

  private static ContainerBuilder getContainerBuilder() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(TestTypeWithDependencies.class);
    builder.register(AnotherTestTypeWithDependencies.class);
    builder.register(SimpleTestType.class);
    return builder;
  }
}


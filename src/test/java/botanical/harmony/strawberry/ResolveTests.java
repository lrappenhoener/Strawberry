package botanical.harmony.strawberry;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class ResolveTests {
  @Test
  void resolve_simple_type() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(SimpleTestType.class);
    Container container = builder.build();

    Optional<SimpleTestType> result = container.resolve(SimpleTestType.class);

    assertTrue(result.isPresent());
    assertTrue(result.get().getClass() == SimpleTestType.class);
  }

  @Test
  void no_result_when_resolving_unregistered_type() {
    ContainerBuilder builder = ContainerBuilder.create();
    Container container = builder.build();

    Optional<SimpleTestType> result = container.resolve(SimpleTestType.class);

    assertTrue(result.isEmpty());
  }

  @Test
  void no_result_when_resolving_type_where_dependency_not_registered() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(TestTypeWithDependencies.class);
    Container container = builder.build();

    Optional<TestTypeWithDependencies> result = container.resolve(TestTypeWithDependencies.class);

    assertTrue(result.isEmpty());
  }

  @Test
  void resolve_type_with_dependencies() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(TestTypeWithDependencies.class);
    builder.register(AnotherTestTypeWithDependencies.class);
    builder.register(SimpleTestType.class);
    Container container = builder.build();

    Optional<TestTypeWithDependencies> result = container.resolve(TestTypeWithDependencies.class);

    assertTrue(result.isPresent());
    assertTrue(result.get().getClass() == TestTypeWithDependencies.class);
  }
}

class TestTypeWithDependencies {
  public TestTypeWithDependencies(SimpleTestType simple, AnotherTestTypeWithDependencies another) {
    super();
  }
}

class AnotherTestTypeWithDependencies {
  public AnotherTestTypeWithDependencies(SimpleTestType simple) {
    super();
  }
}

class SimpleTestType {

}

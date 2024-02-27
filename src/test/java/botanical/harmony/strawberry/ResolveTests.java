package botanical.harmony.strawberry;

import static org.junit.jupiter.api.Assertions.assertSame;
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
    assertSame(result.get().getClass(), SimpleTestType.class);
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
    ContainerBuilder builder = getContainerBuilder();
    Container container = builder.build();

    Optional<TestTypeWithDependencies> result = container.resolve(TestTypeWithDependencies.class);

    assertTrue(result.isPresent());
    assertSame(result.get().getClass(), TestTypeWithDependencies.class);
  }

  @Test
  void resolve_type_with_dependencies_by_factory() {
    ContainerBuilder builder = getContainerBuilder();
    builder.register(TestTypeForFactory.class, (c) -> {
      Optional<AnotherTestTypeWithDependencies> dependencyA = c.resolve(AnotherTestTypeWithDependencies.class);
      Optional<SimpleTestType> dependencyB = c.resolve(SimpleTestType.class);
      Optional<TestTypeWithDependencies> dependencyC = c.resolve(TestTypeWithDependencies.class);
      return new TestTypeForFactory(dependencyA.get(), dependencyB.get(), dependencyC.get());
    });
    Container container = builder.build();

    Optional<TestTypeForFactory> result = container.resolve(TestTypeForFactory.class);

    assertTrue(result.isPresent());
  }

  private static ContainerBuilder getContainerBuilder() {
    ContainerBuilder builder = ContainerBuilder.create();
    builder.register(TestTypeWithDependencies.class);
    builder.register(AnotherTestTypeWithDependencies.class);
    builder.register(SimpleTestType.class);
    return builder;
  }
}

class TestTypeForFactory {
  private final AnotherTestTypeWithDependencies dependencyA;
  private final SimpleTestType dependencyB;
  private final TestTypeWithDependencies dependencyC;

  public TestTypeForFactory(AnotherTestTypeWithDependencies dependencyA, SimpleTestType dependencyB, TestTypeWithDependencies dependencyC) {
    this.dependencyA = dependencyA;
    this.dependencyB = dependencyB;
    this.dependencyC = dependencyC;
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

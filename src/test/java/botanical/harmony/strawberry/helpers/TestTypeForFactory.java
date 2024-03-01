package botanical.harmony.strawberry.helpers;

public class TestTypeForFactory {
  private final AnotherTestTypeWithDependencies dependencyA;
  private final SimpleTestType dependencyB;
  private final TestTypeWithDependencies dependencyC;

  public TestTypeForFactory(AnotherTestTypeWithDependencies dependencyA, SimpleTestType dependencyB, TestTypeWithDependencies dependencyC) {
    this.dependencyA = dependencyA;
    this.dependencyB = dependencyB;
    this.dependencyC = dependencyC;
  }
}

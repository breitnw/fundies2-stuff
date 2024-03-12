package lists;

public class ConsLoInt implements ILoInt {
  public int first;
  public ILoInt rest;

  public ConsLoInt(int first, ILoInt rest) {
    this.first = first;
    this.rest = rest;
  }

  @Override
  public int secondLargestNum() {
    throw new RuntimeException("If you got this to run, your tests compile correctly against the data definitions.");
  }

  @Override
  public int fifthLargestNum() {
    throw new RuntimeException("If you got this to run, your tests compile correctly against the data definitions.");
  }

  @Override
  public int mostCommonNum() {
    throw new RuntimeException("If you got this to run, your tests compile correctly against the data definitions.");
  }

  @Override
  public int thirdMostCommonNum() {
    throw new RuntimeException("If you got this to run, your tests compile correctly against the data definitions.");
  }
}

package edu.brown.cs.jfacey.datastructs;

import java.util.Objects;

/**
 * This class implements a bigram pair of words which is used for ranking
 * results in the autocorrecter.
 *
 * @author jfacey
 *
 */
public class BigramPair {

  private final String iWordA;
  private final String iWordB;
  static final int HASH_ONE = 31;
  static final int HASH_TWO = 17;

  /**
   * This is the constructor for the bigram pair which sets its initial word
   * values.
   *
   * @param wordA
   *          the first word in the bigram
   * @param wordB
   *          the second word in the bigram
   */
  public BigramPair(String wordA, String wordB) {

    iWordA = wordA;
    iWordB = wordB;
  }

  /**
   * The getter for the first word.
   *
   * @return the first word value
   */
  public String getWordA() {
    return iWordA;
  }

  /**
   * The getter for the second word.
   *
   * @return the second word value
   */
  public String getWordB() {
    return iWordB;
  }

  @Override
  public boolean equals(Object object) {
    return Objects.equals(iWordA, ((BigramPair) object).getWordA())
        && Objects.equals(iWordB, ((BigramPair) object).getWordB());
  }

  @Override
  public int hashCode() {
    int hash = HASH_ONE;
    hash = HASH_TWO * hash + iWordA.hashCode();
    hash = HASH_TWO * hash + iWordB.hashCode();
    return hash;
  }
}

package edu.brown.cs.jfacey.autocorrect;

/**
 * This class stores a suggestion for an autocorrected word. It stores the word
 * suggestion, the distance away from the input word by led or prefix matching,
 * and whether this is a led, prefix, or whitespace suggestion. The whitespace
 * suggestion will not have a distance value.
 *
 * @author jfacey
 *
 */
public class Suggestion {

  private final int iDistance;
  private final String iType;
  private final String iWord;

  /**
   * This is a constructor for the suggestion class that initially sets the
   * values.
   *
   * @param distance
   *          the distance from the word and the autocorrect input word
   * @param type
   *          the type of correction done
   * @param word
   *          the new corrected word
   */
  public Suggestion(int distance, String type, String word) {
    iDistance = distance;
    iType = type;
    iWord = word;
  }

  /**
   * This is a getter method for the distance.
   *
   * @return the distance
   */
  public int getDistance() {
    return iDistance;
  }

  /**
   * This is a getter method for the type.
   *
   * @return the type
   */
  public String getType() {
    return iType;
  }

  /**
   * This is a getter method for the word.
   *
   * @return the word
   */
  public String getWord() {
    return iWord;
  }
}

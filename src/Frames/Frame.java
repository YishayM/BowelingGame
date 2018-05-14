package Frames;

public abstract class Frame {
    public final int MAX_PINS = 10;
    public final int MIN_PINS = 0;
    public final int LAST_TURN = 10;


  protected int firstRoll;
  protected int secondRoll;
  private int turnNum;

  public enum Type {
    REGULAR,
    SPARE,
    STRIKE,
    DOUBLE_STRIKE
  }

  public Frame(int turn) {
    turnNum = turn;
  }

  public Type getType() {
    if (firstRoll == MAX_PINS) return Type.STRIKE;
    else if (firstRoll + secondRoll == MAX_PINS) return Type.SPARE;
    else return Type.REGULAR;
  }

  public void rollFirst(int pins) {
    firstRoll = pins;
  }

  public void rollSecond(int pins) {
    secondRoll = pins;
  }

  public int getTurn() {
    return turnNum;
  }

  public int getBasicScore() {
    return firstRoll + secondRoll;
  }

  public int getFirst() {
    return firstRoll;
  }

  public int getSecond() {
    return secondRoll;
  }
}

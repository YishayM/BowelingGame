package Frames;

public class LastFrame extends Frame {
  private int thirdRoll = MIN_PINS;

  public LastFrame() {
    super(10);
  }

  public void rollThird(int thirdRoll) {
    this.thirdRoll = thirdRoll;
  }

  public int getThird() {
    return thirdRoll;
  }

  @Override
  public int getBasicScore() {
    return firstRoll + secondRoll + thirdRoll;
  }

  public Type getType() {
    if ((firstRoll == MAX_PINS) && (secondRoll != MAX_PINS)) {
      return Type.STRIKE;
    } else if (firstRoll == MAX_PINS) return Type.DOUBLE_STRIKE;
    else if (firstRoll + secondRoll == MAX_PINS) return Type.SPARE;
    else return Type.REGULAR;
  }
}

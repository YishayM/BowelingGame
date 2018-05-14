package Game;

import Frames.Frame;
import Frames.LastFrame;
import Frames.RegularFrame;
import org.testng.log4testng.Logger;

import java.util.ArrayList;


public class FrameFactory {
  public final int MAX_PINS = 10;
  public final int LAST_TURN = 10;
  public final int FIRST_ITEM = 0;

  private static final Logger LOGGER = Logger.getLogger(FrameFactory.class);

  ArrayList<Frame> frames;

  int turnIndex;

  public ArrayList<Frame> ParseRolls(ArrayList<Integer> rolls) {
    turnIndex = 1;
    frames = new ArrayList<>();
    while ((turnIndex < LAST_TURN)) {
      Frame currentFrame = getFrame(rolls);
      if (currentFrame != null) {
        LOGGER.debug("Frame created with Turn number: " + turnIndex);
        frames.add(currentFrame);
        turnIndex++;
      } else {
        break;
      }
    }
    if (turnIndex == LAST_TURN) {
      LastFrame lastFrame = getLastFrame(rolls);
      if (lastFrame != null) {
        LOGGER.debug("Last frame created with Turn number: " + turnIndex);
        frames.add(lastFrame);
      } else throw new RuntimeException("Last frame not created. Unexpected error");
    }

    return frames;
  }

  private LastFrame getLastFrame(ArrayList<Integer> rolls) {
    LastFrame lastFrame = null;
    if (!rolls.isEmpty()) { // first roll in frame
      lastFrame = new LastFrame();
      int firstRoll = rolls.remove(FIRST_ITEM);
      lastFrame.rollFirst(firstRoll);

      // strike - allow 2 more rolls

      if (firstRoll == MAX_PINS) {
        if (!rolls.isEmpty()) {
          int secondRoll = rolls.remove(FIRST_ITEM);
          lastFrame.rollSecond(secondRoll);
          if (!rolls.isEmpty()) {
            int thirdRoll = rolls.remove(FIRST_ITEM);
            lastFrame.rollThird(thirdRoll);
          }
        }
      } else { // not strike
        if (!rolls.isEmpty()) {
          int secondRoll = rolls.remove(FIRST_ITEM);
          lastFrame.rollSecond(secondRoll);
          if ((firstRoll + secondRoll == MAX_PINS)
              && (!rolls.isEmpty())) { // spare - allow one bonus roll
            int thirdRoll = rolls.remove(FIRST_ITEM);
            lastFrame.rollThird(thirdRoll);
          }
        }
      }
    }
    return lastFrame;
  }

  private Frame getFrame(ArrayList<Integer> rolls) {
    RegularFrame currnetFrame = null;
    if (!rolls.isEmpty()) { // first roll
      int firstRoll = rolls.remove(FIRST_ITEM);
      currnetFrame = new RegularFrame(turnIndex);
      currnetFrame.rollFirst(firstRoll);

      // not strike - continue with second roll. otherwise return the frame

      if (firstRoll != MAX_PINS) {
        if (!rolls.isEmpty()) {
          int secondRoll = rolls.remove(FIRST_ITEM);
          currnetFrame.rollSecond(secondRoll);
        }
      }
    }
    return currnetFrame;
  }
}

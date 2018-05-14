package Game;

import Frames.Frame;
import Frames.Frame.Type;
import Frames.LastFrame;
import org.testng.log4testng.Logger;

import java.util.ArrayList;


public class Game {

  public static final int MAX_PINS = 10;
  public static final int MIN_PINS = 0;
  public final int LAST_TURN = 10;
  public final int INDEX_DIFFERENCE = 1;
  public final int INDEX_LAST_FRAME = 9;
  public final int INDEX_BEFORE_LAST_FRAME = 8;

  private static final Logger LOGGER = Logger.getLogger(Game.class);

  private ArrayList<Integer> rolls = new ArrayList<>();
  private FrameFactory factory = new FrameFactory();
  private ArrayList<Frame> frames;

  public void roll(int pins) {
    validateRoll(pins);
    rolls.add(pins);
  }

  public int getScore() {
    frames = factory.ParseRolls(rolls);
    return getTotalScore();
  }

  private int getTotalScore() {
    int sum = 0;
    for (Frame currentFrame : frames) {
      int frameSum = 0;
      Type currentType = currentFrame.getType();
      switch (currentType) {
        case REGULAR:
          frameSum = currentFrame.getBasicScore();
          break;
        case STRIKE:
          // checking if next frame is also strike. to apply bonus for next 2 rolls.
          frameSum = getScoreForStrike(currentFrame);
          break;
        case SPARE:
          frameSum = getScoreForSpare(currentFrame);
          break;
        case DOUBLE_STRIKE:
          if (currentFrame.getTurn() == LAST_TURN) {
            frameSum = getScoreForDoubleStrike(currentFrame);
          } else throw new RuntimeException("Something went wrong");
          break;
      }
      sum += frameSum;
      LOGGER.info("Score for frame" + currentFrame.getTurn() + " is " + frameSum + "  sum is " + sum);
    }
    LOGGER.info("====== Total score is " + sum + " ======");

    return sum;
  }

  private int getScoreForDoubleStrike(Frame currentFrame) {
    return currentFrame.getBasicScore();
  }

  /**
   * This method count the score for spare frame according frame position 1. 1-9: basic score + next
   * roll. 2. 10: basic score only.
   *
   * @param currentFrame Single spare frame
   * @return Total score (int) for the frame
   */
  private int getScoreForSpare(Frame currentFrame) {
    int currentFrameIndex = currentFrame.getTurn() - INDEX_DIFFERENCE;
    if (currentFrameIndex < INDEX_LAST_FRAME) { // turn 1-9
      return currentFrame.getBasicScore()
          + frames.get(currentFrameIndex + INDEX_DIFFERENCE).getFirst();
    } else { // turn == 10
      LastFrame lastFrame = (LastFrame) currentFrame;
      return lastFrame.getBasicScore();
    }
  }

  /**
   * This method count the score for strike frame according frame position 1. 1-8: basic score +
   * next rolls. 2. 9: basic score + next roll first and second rolls. 3. 10: basic score only.
   *
   * @param currentFrame Single strike frame
   * @return Total score (int) for the frame
   */
  private int getScoreForStrike(Frame currentFrame) {
    // there is difference between currentTurn and frames array index

    int currentFrameIndex = currentFrame.getTurn() - INDEX_DIFFERENCE;
    // case 1: frame 1-8: calculate basic score + next 2rolls.
    if (currentFrameIndex < INDEX_BEFORE_LAST_FRAME) {
      Frame nextFrame = frames.get(currentFrameIndex + INDEX_DIFFERENCE);
      Type nextFrameType = nextFrame.getType();
      // if the next frame is also strike the bonus score will take from next 2 frames.
      if (nextFrameType == Type.STRIKE)
        return currentFrame.getBasicScore()
            + frames.get(currentFrameIndex + 1).getBasicScore()
            + frames.get(currentFrameIndex + 2).getFirst();
      else return currentFrame.getBasicScore() + frames.get(currentFrameIndex + 1).getBasicScore();

      // case 2: frame 9: calculate basic score + 10th frame first score and bonus roll if exist.

    } else if (currentFrameIndex == INDEX_BEFORE_LAST_FRAME) {
      LastFrame nextFrame = (LastFrame) frames.get(currentFrameIndex + INDEX_DIFFERENCE);
      Type nextFrameType = nextFrame.getType();
      if (nextFrameType == Type.REGULAR) {
        return currentFrame.getBasicScore() + nextFrame.getBasicScore();
      } else {
        return currentFrame.getBasicScore() + nextFrame.getFirst() + nextFrame.getSecond();
      }

      // case 3: frame 10: calculate the bonus for double strike case.

    } else {
      LastFrame lastFrame = (LastFrame) currentFrame;
      return lastFrame.getFirst() + lastFrame.getSecond() + lastFrame.getThird();
    }
  }

  private void validateRoll(int roll) {
    if (roll > MAX_PINS || roll < MIN_PINS) {
      throw new RuntimeException("Wrong pins number");
    }
  }
}

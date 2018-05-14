import Game.Game;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BowlingTest {

  private Game game;

  @BeforeTest
  public void initialize() {
    game = new Game();
  }

  @Test
  public void testAllZero() throws Exception {
    rollMany(20, 0);
    assertEquals(0, game.getScore());
  }

  @Test
  public void testAllOne() throws Exception {
    rollMany(20, 1);
    assertEquals(game.getScore(), 20);
  }

  @Test
  public void testBasicSpare() throws Exception {
    game.roll(5);
    game.roll(5);
    game.roll(3);
    rollMany(17, 0);
    assertEquals(game.getScore(), 16);
  }

  @Test
  public void testBasicStrike() throws Exception {
    game.roll(10);
    game.roll(5);
    game.roll(3);
    rollMany(16, 0);
    assertEquals(game.getScore(), 26);
  }

  @Test
  public void testAllStrike() throws Exception {
    rollMany(12, 10);
    assertEquals(game.getScore(), 300);
  }

  @Test
  public void testAllSpare() throws Exception {
    rollMany(21, 5);
    assertEquals(game.getScore(), 150);
  }

  @Test
  public void testStrikeWithZero() throws Exception {
    rollMany(10, 10);
    game.roll(0);
    game.roll(0);
    assertEquals(game.getScore(), 270);
  }

  @Test
  public void testStrikesWith550() throws Exception {
    rollMany(9, 10);
    game.roll(5);
    game.roll(5);
    game.roll(0);
    assertEquals(game.getScore(), 265);
  }

  @Test
  public void testWithNegative() throws Exception {
    try {
      rollMany(9, 10);
      game.roll(-1);
      game.roll(5);
      game.roll(10);
      assertEquals(game.getScore(), 275);
    } catch (Exception e) {
      Assert.assertEquals("Wrong pins number", e.getMessage());
    }
  }

  @Test
  public void testWithExtraPins() throws Exception {
    try {
      rollMany(9, 12);
      game.roll(5);
      game.roll(5);
      game.roll(10);
      assertEquals(game.getScore(), 275);

    } catch (RuntimeException e) {
      assertEquals("Wrong pins number", e.getMessage());
    }
  }

  private void rollMany(int rolls, int pins) {
    for (int i = 0; i < rolls; i++) {
      game.roll(pins);
    }
  }
}

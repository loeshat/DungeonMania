package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovementTest {
    @Test
    @Tag("1-1")
    @DisplayName("Test the player can move down")
    public void testMovementDown() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame(
                "d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = TestUtils.getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(),
                new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = TestUtils.getPlayer(actualDungonRes).get();

        // assert after movement
        assertTrue(TestUtils.entityResponsesEqual(expectedPlayer, actualPlayer));
    }

    @Test
    @Tag("1-2")
    @DisplayName("Test the player can move up")
    public void testMovementUp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementUp",
                "c_movementTest_testMovementUp");
        EntityResponse initPlayer = TestUtils.getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(),
                new Position(1, 0), false);

        // move player upward
        DungeonResponse actualDungonRes = dmc.tick(Direction.UP);
        EntityResponse actualPlayer = TestUtils.getPlayer(actualDungonRes).get();

        // assert after movement
        assertTrue(TestUtils.entityResponsesEqual(expectedPlayer, actualPlayer));
    }

    @Test
    @Tag("1-3")
    @DisplayName("Test the player can move left")
    public void testMovementLeft() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame(
                "d_movementTest_testMovementLeft", "c_movementTest_testMovementLeft");
        EntityResponse initPlayer = TestUtils.getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(),
                new Position(0, 1), false);

        // move player left
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        EntityResponse actualPlayer = TestUtils.getPlayer(actualDungonRes).get();

        // assert after movement
        assertTrue(TestUtils.entityResponsesEqual(expectedPlayer, actualPlayer));
    }

    @Test
    @Tag("1-4")
    @DisplayName("Test the player can move right")
    public void testMovementRight() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame(
                "d_movementTest_testMovementRight", "c_movementTest_testMovementRight");
        EntityResponse initPlayer = TestUtils.getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(),
                initPlayer.getType(), new Position(2, 1), false);

        // move player right
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = TestUtils.getPlayer(actualDungonRes).get();

        // assert after movement
        assertTrue(TestUtils.entityResponsesEqual(expectedPlayer, actualPlayer));
    }

    @Test
    @DisplayName("test movement of mercenary on swamp tile")
    public void testSwampTileMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
                "d_movementTest_swampTileMerc", "c_movementTest_generic");

        // tick - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 1 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 2 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 3 - move
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), getMercPos(res));
    }

    @Test
    @DisplayName("test movement of mercenary on swamp tile")
    public void testSwampTileMercenaryFive() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
                "d_movementTest_swampTileMercFive", "c_movementTest_generic");

        // tick - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 1 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 2 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 3 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 4 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 5 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 6 - move
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), getMercPos(res));
    }

    @Test
    @DisplayName("test movement of player on swamp tile - no effect")
    public void testSwampTilePlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
                "d_movementTest_swampTilePlayer", "c_movementTest_generic");

        // tick 1 - is the player stuck?
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getPlayerPos(res));
        // tick 2 - omg he isn't!!
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), getPlayerPos(res));
    }

    @Test
    @DisplayName("test multiple swamp tiles all slow")
    public void testMultipleSwampTiles() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
                "d_movementTest_multipleSwampTiles", "c_movementTest_generic");
        // tick - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 1), getMercPos(res));
        // tick 1 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 1), getMercPos(res));
        // tick 2 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 1), getMercPos(res));
        // tick - stuck again
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 1 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 2 - stuck
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        // tick 3 - move
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), getMercPos(res));
    }

    @Test
    @DisplayName("Testing ally movement not imparied by swamp tiles when adjacent to player")
    public void swampTileAdjacentAlly() {
        /*
         * 0 1 2 3 4 5
         * - - - w w w 0
         * p t s s m w 1
         * - - - w w w 2
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_swampTileAlly", "c_movementTest_generic");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), getMercPos(res));
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        // create ally
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(new Position(3, 1), getMercPos(res));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        // follow player around
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), getMercPos(res));
        assertEquals(new Position(2, 1), getPlayerPos(res));
        // now adjacent, mercenary ally should be able to move freely
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), getMercPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 1), getMercPos(res));
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }
}

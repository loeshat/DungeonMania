package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MercenaryTest {

    @Test
    @Tag("12-1")
    @DisplayName("Test mercenary in line with Player moves towards them")
    public void simpleMovement() {
        // Wall Wall Wall Wall Wall Wall
        // P1 P2 P3 P4 M4 M3 M2 M1 . Wall
        // Wall Wall Wall Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_simpleMovement", "c_mercenaryTest_simpleMovement");

        assertEquals(new Position(8, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), getMercPos(res));
    }

    @Test
    @Tag("12-2")
    @DisplayName("Test mercenary stops if they cannot move any closer to the player")
    public void stopMovement() {
        // Wall Wall Wall
        // P1 P2 Wall M1 Wall
        // Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_stopMovement", "c_mercenaryTest_stopMovement");

        Position startingPos = getMercPos(res);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(startingPos, getMercPos(res));
    }

    @Test
    @Tag("12-3")
    @DisplayName("Test mercenaries can not move through closed doors")
    public void doorMovement() {
        // Wall Door Wall
        // P1 P2 Wall M1 Wall
        // Key Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_doorMovement", "c_mercenaryTest_doorMovement");

        Position startingPos = getMercPos(res);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(startingPos, getMercPos(res));
    }

    @Test
    @Tag("12-4")
    @DisplayName("Test mercenary moves around a wall to get to the player")
    public void evadeWall() {
        // Wall M2
        // P1 P2 Wall M1
        // Wall M2
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_evadeWall", "c_mercenaryTest_evadeWall");

        res = dmc.tick(Direction.RIGHT);
        assertTrue(new Position(4, 1).equals(getMercPos(res))
                || new Position(4, 3).equals(getMercPos(res)));
    }

    @Test
    @Tag("12-5")
    @DisplayName("Testing a mercenary can be bribed with a certain amount")
    public void bribeAmount() {
        // Wall Wall Wall Wall Wall
        // P1 P2/Treasure P3/Treasure P4/Treasure M4 M3 M2 M1 Wall
        // Wall Wall Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_bribeAmount", "c_mercenaryTest_bribeAmount");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up first treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getMercPos(res));

        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // pick up second treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(6, 1), getMercPos(res));

        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // pick up third treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(5, 1), getMercPos(res));

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("12-6")
    @DisplayName("Testing a mercenary can be bribed within a radius")
    public void bribeRadius() {
        // Wall Wall Wall Wall Wall
        // P1 P2/Treasure P3 P4 M4 M3 M2 M1 Wall
        // Wall Wall Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_bribeRadius", "c_mercenaryTest_bribeRadius");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getMercPos(res));

        // attempt bribe
        assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("12-7")
    @DisplayName("Testing an allied mercenary does not battle the player")
    public void allyBattle() {
        // Wall Wall Wall
        // P1 P2/Treasure . M2 M1 Wall
        // Wall Wall Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_allyBattle", "c_mercenaryTest_allyBattle");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @DisplayName("mercenary sceptre behaviour tests")
    public void mercenaryMindControlBehaviour() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_MindControl", "c_mercenaryTest_MindControl");

        assertEquals(2, getMercs(res).size());
        // walk into mercenary, a battle occurs and mercenary dies
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        assertEquals(1, getMercs(res).size());

        // collect materials to craft a sceptre and craft it
        res = dmc.tick(Direction.LEFT);

        assertEquals(new Position(8, 1), getMercPos(res));

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // walk with mercenary until sceptre times out
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));
        assertEquals(1, res.getBattles().size());
        assertEquals(1, getMercs(res).size());

        // sceptre times out

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), getMercPos(res));
        Position player = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(0, 1), player);

        // walk into mercenary, a battle occurs and the mercenary dies

        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, res.getBattles().size());
        assertEquals(0, getMercs(res).size());
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }

    private List<EntityResponse> getMercs(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary");
    }

    @Test
    @DisplayName("test that when merc is an ally, it moves towards the player")
    public void testAlliedMercMovement() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_alliesMovement", "c_mercenaryTest_alliesMovement");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure and bribe the mercenary
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // mercenary should try to follow the player instead of going to a random
        // location
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), getMercPos(res));
        assertEquals(1, TestUtils.getInventory(res, "invincibility_potion").size());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(3, 1), getMercPos(res));

        // use potion - player stands still, and the ally mercenary should also stand
        // still
        res = dmc.tick(TestUtils.getFirstItemId(res, "invincibility_potion"));
        assertEquals(0, TestUtils.getInventory(res, "invincibility_potion").size());

        assertEquals(new Position(3, 1), getMercPos(res));

        assertEquals(0, res.getBattles().size());

        // move to the left 1 position, mercenary should follow

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), getMercPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 1), getMercPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 2), getMercPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 3), getMercPos(res));
    }

    @Test
    @DisplayName("test adjacent position, but not cardinally")
    public void testAllyAdjacentPosition() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_alliesCardinalMovement", "c_mercenaryTest_alliesMovement");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(4, 1), getMercPos(res));

        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(3, 1), getMercPos(res));

        // player rams his head into a wall, ally walks towards the player
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(2, 1), getMercPos(res));

        res = dmc.tick(Direction.LEFT);
        assertTrue(new Position(2, 1).equals(getMercPos(res)));

        res = dmc.tick(Direction.DOWN);
        assertTrue(new Position(1, 1).equals(getMercPos(res)));

        res = dmc.tick(Direction.DOWN);
        assertTrue(new Position(1, 2).equals(getMercPos(res)));
    }
}

package dungeonmania.mvp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.*;

public class SunStoneTest {

    @Test
    @DisplayName("test that the player can pick up a sun_stone")
    public void testPlayerPickUpSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sun_stoneTest_pickupSunstone", "c_sun_stoneTest_generic");

        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // pick up sun_stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("test if player is able to pick up multiple sun_stones")
    public void testPlayerPickUpMultipleSunStones() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sun_stoneTest_pickupSunstone", "c_sun_stoneTest_generic");

        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // pick up sun_stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // pick up another sun_stone --> sun_stone count should be two
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("test if player is unable to open the door without the sun_stone, then able to with the sun_stone")
    public void testPlayerOpenDoorWithSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sun_stoneTest_WalkThroughDoor", "c_sun_stoneTest_generic");
        // test player cannot walk through door
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        // try to walk through door and fail
        res = dmc.tick(Direction.RIGHT);
        assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // pick up sun_stone
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // try to walk through the door again
        res = dmc.tick(Direction.RIGHT);
        assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        res = dmc.tick(Direction.RIGHT);
        // see that player still keeps the sun_stone
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @DisplayName("test if player uses the sun_stone to craft a shield")
    public void testPlayerCraftShieldWithSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sun_stoneTest_CraftShield", "c_sun_stoneTest_generic");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Wood x2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // Pick up sun_stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Wood disappear during construction while sun_stone does not
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("test that the player cannot bribe mercenaries when they have the sun_stone")
    public void testSunStoneUnableToBribe() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sun_stoneTest_BribeMercenary", "c_sun_stoneTest_generic");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up first sun_stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(new Position(7, 1), getMercPos(res));

        // attempt bribe - fails
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(6, 1), getMercPos(res));

        // achieve bribe - success
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @DisplayName("test that picking up a sun_stone contributes to treasure goal")
    public void testSunStoneTreasureGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sun_stoneTest_TreasureGoal", "c_sun_stoneTest_generic");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect sun_stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect sun_stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect sun_stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));

    }

    // Helper method for bribeMercenary test
    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }
}

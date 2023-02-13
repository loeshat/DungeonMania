package dungeonmania.mvp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class EnemiesGoalTest {

    @Test
    @Tag("17-1")
    @DisplayName("Enemies goal shows properly")
    public void enemyGoalShows() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_enemyKillGoalAchieved",
                                            "c_enemyGoalTest_enemyKillGoalAchieved");
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("17-2")
    @DisplayName("Kill enemies to achieve goal + no spawner")
    public void enemyKill() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_enemyKillGoalAchieved",
                                            "c_enemyGoalTest_enemyKillGoalAchieved");
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("17-3")
    @DisplayName("Destroy spawner but no enemy kill")
    public void destroySpawner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_spawnerDestructionOnly",
                                            "c_enemyGoalTest_enemyKillGoalAchieved");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals("", TestUtils.getGoals(res));
    }


    @Test
    @Tag("17-4")
    @DisplayName("Destroy spawner and enemy kills")
    public void destroySpawnerEnemyKill() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalsTest_enemyKillSpawnerDestroyed",
                                            "c_enemyGoalTest_enemyKillGoalAchieved");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        System.out.println(TestUtils.getEntities(res, "player").get(0).getPosition());


        res = dmc.tick(Direction.RIGHT);
        System.out.println(TestUtils.getEntities(res, "player").get(0).getPosition());

        res = dmc.tick(Direction.DOWN);
        assertEquals(":enemies", TestUtils.getGoals(res));
        System.out.println(TestUtils.getEntities(res, "player").get(0).getPosition());


        res = dmc.tick(Direction.RIGHT);

        System.out.println(TestUtils.getEntities(res, "player").get(0).getPosition());

        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));

        assertEquals("", TestUtils.getGoals(res));
    }
}

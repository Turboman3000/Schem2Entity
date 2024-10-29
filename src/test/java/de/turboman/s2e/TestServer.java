package de.turboman.s2e;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

import java.io.File;

public class TestServer {
    static InstanceContainer instance;

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();

        instance = MinecraftServer.getInstanceManager().createInstanceContainer();

        instance.setChunkSupplier(LightingChunk::new);
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));

        Schem2Entity.spawn(new File("./test.schem"), instance, new Pos(0, 50, 0));

        eventHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            e.setSpawningInstance(instance);
            e.getPlayer().setRespawnPoint(new Pos(0, 40, 0));
        });

        eventHandler.addListener(PlayerSpawnEvent.class, e -> {
            e.getPlayer().setAllowFlying(true);
        });

        server.start("0.0.0.0", 25565);
    }
}

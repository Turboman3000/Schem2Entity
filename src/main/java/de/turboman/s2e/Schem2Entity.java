package de.turboman.s2e;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.registry.Registry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Schem2Entity {
    private static final Nbt NBT = new Nbt();

    public static void spawn(File schematicFile, InstanceContainer instance, Point location) {
        ArrayList<Entity> entitys = new ArrayList<>();

        CompoundTag data;

        try {
            data = NBT.fromFile(schematicFile).getCompound("Schematic");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final var palette = data.getCompound("Blocks").getCompound("Palette");
        final var dataArray = data.getCompound("Blocks").getByteArray("Data");

        final var width = data.getShort("Width").getValue();
        final var height = data.getShort("Height").getValue();
        final var length = data.getShort("Length").getValue();

        for (short w = 0; w <= width; w++) {
            for (short h = 0; h <= height; h++) {
                for (short l = 0; l <= length; l++) {
                    final var dat = dataArray.get(w + h + l);

                    System.out.println(palette.keySet().toArray()[dat]);
                }
            }
        }
    }
}

package de.turboman.s2e;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.Material;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Schem2Entity {
    private static final Nbt NBT = new Nbt();

    public static ArrayList<Entity> spawn(File schematicFile, InstanceContainer instance, Point location) {
        final ArrayList<Entity> entities = new ArrayList<>();
        CompoundTag data;

        try {
            data = NBT.fromFile(schematicFile).getCompound("Schematic");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final var offset = data.getIntArray("Offset");
        final var palette = data.getCompound("Blocks").getCompound("Palette");
        final var dataArray = data.getCompound("Blocks").getByteArray("Data");

        final var width = data.getShort("Width").getValue();
        final var height = data.getShort("Height").getValue();
        final var length = data.getShort("Length").getValue();

        for (short w = 0; w < width; w++) {
            for (short h = 0; h < height; h++) {
                for (short l = 0; l < length; l++) {
                    final var paletteKey = (String) palette.keySet().toArray()[dataArray.get(w + l * width + h * width * length)];
                    final var material = Material.fromNamespaceId(paletteKey);

                    if (paletteKey.equals("minecraft:air")) continue;
                    if (paletteKey.equals("minecraft:structure_void")) continue;

                    if (material == null) continue;
                    if (material.block() == null) continue;

                    final Entity entity = new Entity(EntityType.BLOCK_DISPLAY);
                    final double finalX = w;
                    final double finalY = h;
                    final double finalZ = l;

                    entity.editEntityMeta(BlockDisplayMeta.class, meta -> {
                        meta.setBlockState(material.block());
                        meta.setTranslation(new Pos(finalX, finalY, finalZ));
                    });

                    entity.setInstance(instance, location);
                    entities.add(entity);
                }
            }
        }

        return entities;
    }
}

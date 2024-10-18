package de.turboman.s2e;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Schem2Entity {
    private static final Nbt NBT = new Nbt();

    /**
     * It's reading a Schematic file (.schem) and converts/spawns Block Display Entities in the Provided Instance and Provided Location
     *
     * @param schematicFile The schematic file
     * @param instance      The Instance to Spawn the Entities in
     * @param location      The Location the Entities should spawn
     * @return An Array of the Entities
     */
    public static @NotNull ArrayList<Entity> spawn(File schematicFile, InstanceContainer instance, Point location) {
        final ArrayList<Entity> entities = new ArrayList<>();
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

        for (short w = 0; w < width; w++) {
            for (short h = 0; h < height; h++) {
                for (short l = 0; l < length; l++) {
                    String paletteKey = (String) palette.keySet().toArray()[dataArray.get(w + l * width + h * width * length)];
                    final var paletteKeySplit = paletteKey.replace("]", "").split("\\[");
                    final var block = Block.fromNamespaceId(paletteKeySplit[0]);
                    final HashMap<String, String> properties = new HashMap<>();

                    if (paletteKeySplit.length >= 2)
                        for (var key : paletteKeySplit[1].split(",")) {
                            var split = key.split("=");
                            properties.put(split[0], split[1]);
                        }

                    if (paletteKey.equals("minecraft:air")) continue;
                    if (paletteKey.equals("minecraft:structure_void")) continue;
                    if (block == null) continue;

                    final Block finalBlock = block.withProperties(properties);
                    final Entity entity = new Entity(EntityType.BLOCK_DISPLAY);
                    final double finalX = w;
                    final double finalY = h;
                    final double finalZ = l;

                    entity.editEntityMeta(BlockDisplayMeta.class, meta -> {
                        meta.setBlockState(finalBlock);
                        meta.setTranslation(new Pos(finalX, finalY, finalZ));
                        meta.setHasNoGravity(true);
                    });

                    entity.setInstance(instance, location);
                    entities.add(entity);
                }
            }
        }

        return entities;
    }
}

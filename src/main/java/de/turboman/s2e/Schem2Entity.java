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
import java.util.concurrent.atomic.AtomicReference;

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

                    if (paletteKey.contains("minecraft:light")
                            && !paletteKey.replace("minecraft:light", "").startsWith("_")) continue;
                    else if (paletteKey.contains("minecraft:air")) continue;
                    else if (paletteKey.contains("minecraft:barrier")) continue;
                    else if (paletteKey.contains("minecraft:structure_void")) continue;
                    else if (paletteKey.contains("bed") && paletteKey.contains("part=foot")) continue;
                    else if (block == null) continue;

                    final Block finalBlock = block.withProperties(properties);
                    final Entity entity = new Entity(EntityType.BLOCK_DISPLAY);

                    AtomicReference<Double> finalX = new AtomicReference<>((double) w);
                    AtomicReference<Double> finalY = new AtomicReference<>((double) h);
                    AtomicReference<Double> finalZ = new AtomicReference<>((double) l);

                    entity.editEntityMeta(BlockDisplayMeta.class, meta -> {
                        meta.setBlockState(finalBlock);
                        meta.setHasNoGravity(true);

                        // Fix Chest Rotation
                        if (paletteKey.contains("minecraft:chest")) {
                            meta.setRightRotation(new float[]{0, 0, 0, 1});

                            if (properties.get("facing").equals("south")) {
                                meta.setLeftRotation(new float[]{
                                        0, 0, 0, 1
                                });
                            } else if (properties.get("facing").equals("west")) {
                                meta.setLeftRotation(new float[]{
                                        0, -0.7071068f, 0, 0.7071068f
                                });

                                finalX.updateAndGet(v -> v + 1);
                            } else if (properties.get("facing").equals("north")) {
                                meta.setLeftRotation(new float[]{
                                        0, 1, 0, 0
                                });

                                finalX.updateAndGet(v -> v + 1);
                                finalZ.updateAndGet(v -> v + 1);
                            } else if (properties.get("facing").equals("east")) {
                                meta.setLeftRotation(new float[]{
                                        0, 0.7071068f, 0, 0.7071068f
                                });

                                finalZ.updateAndGet(v -> v + 1);
                            }
                        }

                        meta.setTranslation(new Pos(finalX.get(), finalY.get(), finalZ.get()));
                    });

                    entity.setInstance(instance, location);
                    entities.add(entity);
                }
            }
        }

        return entities;
    }
}

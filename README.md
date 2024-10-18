# Schem2Entity

Schem2Entity is a library for the [Minestom](https://github.com/Minestom/Minestom) framework that allows reading `.schem` files and converting them into Block Display Entities.

## Installation

You can add this library to your project either by manually including it as a dependency or by adding it to your build tool (e.g., `gradle`, `maven`).

## Usage

The library provides a simple method to load `.schem` files into a Minecraft instance and spawn the blocks as Block Display Entities at a specific location.

### Example

```java
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.coordinate.Point;
import java.io.File;
import de.turboman.s3e.Schem2Entity;

public class Main {
    public static void main(String[] args) {
        // Example of a .schem file
        File schemFile = new File("path/to/your/file.schem");

        // Your Minestom instance
        InstanceContainer worldContainer = /* Your instance initialization */;

        // Spawn location
        Point spawnLocation = /* Position in the worldContainer */;

        // Convert the schematic into Block Display Entities and spawn them
        // Returns an ArrayList with the Entites
        Schem2Entity.spawn(schemFile, worldContainer, spawnLocation);
    }
}

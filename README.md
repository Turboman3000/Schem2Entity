# Schem2Entity

Schem2Entity is a library for the [Minestom](https://github.com/Minestom/Minestom) framework that allows reading `.schem` files and converting them into Block Display Entities.

## Installation

The Library is published via jitpack

```groovy
repositories {
    // ...
    maven { url 'https://jitpack.io' }
}

dependencies {
    // Schem2Entity
    implementation 'com.github.Turboman3000:Schem2Entity:$LATEST_COMMIT_HASH'
}
```

##   Example

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
        // @return = Returns an ArrayList with the Entites
        Schem2Entity.spawn(schemFile, worldContainer, spawnLocation);
    }
}
```

## License
This project is licensed under the [MIT License](./LICENSE).
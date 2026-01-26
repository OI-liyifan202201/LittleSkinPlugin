package in.littlesk.plugin.core.yggdrasil;

import java.util.List;
import java.util.Optional;

public class RemoteGameProfile {
    public final String id;
    public final String name;
    public final List<Property> properties;

    public static class Property {
        public final String name;
        public final String value;
        public final String signature;

        public Property(String name, String value, String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        public Property(String name, String value) {
            this(name, value, null);
        }
    }

    public RemoteGameProfile(String id, String name, List<Property> properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
    }

    public Optional<Property> getProperty(String propertyName) {
        if (this.properties == null) return Optional.empty();
        return this.properties.stream()
                .filter(p -> p.name.equals(propertyName))
                .findFirst();
    }
}

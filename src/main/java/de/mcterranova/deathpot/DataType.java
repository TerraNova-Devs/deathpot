package de.mcterranova.deathpot;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.time.Instant;

public class DataType implements PersistentDataType<byte[], Instant> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<Instant> getComplexType() {
        return Instant.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull Instant instant, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.put
        return new byte[0];
    }

    @Override
    public @NotNull Instant fromPrimitive(byte @NotNull [] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return null;
    }
}

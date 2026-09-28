package dev.learning.rag.store.sqlite;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Encodes float vectors as compact, portable binary values.
 *
 * <p>The byte order is explicitly little-endian; relying on a machine default
 * would make database files architecture-dependent. Study topics: IEEE-754,
 * endianness, binary serialization, BLOB storage.</p>
 */
final class FloatVectorCodec {
    /** Static serializer utility; instances would carry no state. */
    private FloatVectorCodec() { }

    /**
     * Converts a float array into a SQLite BLOB: four little-endian bytes per
     * IEEE-754 float, in the original coordinate order.
     *
     * <p>How to evolve it: a new encoding requires a stored format-version
     * column and migration/backward decoder. Never silently reinterpret existing
     * BLOBs, because corrupt vectors can still look like valid numbers.</p>
     */
    static byte[] encode(float[] vector) {
        ByteBuffer buffer = ByteBuffer.allocate(vector.length * Float.BYTES).order(ByteOrder.LITTLE_ENDIAN);
        for (float value : vector) buffer.putFloat(value);
        return buffer.array();
    }

    /**
     * Reconstructs the original float array and verifies the stored byte count.
     * The expected dimension comes from the same database row.
     */
    static float[] decode(byte[] bytes, int expectedDimension) {
        if (bytes.length != expectedDimension * Float.BYTES) throw new IllegalArgumentException("stored vector byte length does not match dimension");
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        float[] result = new float[expectedDimension];
        for (int index = 0; index < result.length; index++) result[index] = buffer.getFloat();
        return result;
    }
}

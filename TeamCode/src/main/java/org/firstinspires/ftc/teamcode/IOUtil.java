package org.firstinspires.ftc.teamcode;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for I/O operations.
 */
public final class IOUtil {
    /**
     * IOUtil is a utility class with only static methods; it should not be instantinated.
     */
    private IOUtil() { }

    /**
     * Writes a length-prefixed or null-terminated string to a stream.
     *
     * @param stream - the stream to write to.
     * @param str - If not null, the ASCII-encoded string to write. If null, a single NUL is written
     * to the stream (in effect describing a zero-length string). If greater than or equal to 255
     * characters long, marks the string as null-terminated and writes a NUL following the string
     * content.
     * @throws IOException - an I/O error occurred while writing to the stream.
     */
    public static void writeFlexibleString(OutputStream stream, String str) throws IOException {
        if (str == null) {
            stream.write(0);
        } else {
            stream.write(Math.min(str.length(), Byte.MAX_VALUE));
            stream.write(str.getBytes(StandardCharsets.US_ASCII));
            if (str.length() >= Byte.MAX_VALUE) {
                stream.write(0);
            }
        }
    }
}

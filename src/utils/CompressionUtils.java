package utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public final class CompressionUtils {
    public static byte[] decompress(Path objectPath) throws IOException {
        try (InputStream rawInput = Files.newInputStream(objectPath);
                BufferedInputStream bufferedInput = new BufferedInputStream(rawInput);
                InflaterInputStream decompressedInput = new InflaterInputStream(bufferedInput);
                ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = decompressedInput.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            return output.toByteArray();
        }
    }

    public static byte[] compress(byte[] input) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
                DeflaterOutputStream deflater = new DeflaterOutputStream(output)) {
            deflater.write(input);
            deflater.finish();
            return output.toByteArray();
        }
    }
}

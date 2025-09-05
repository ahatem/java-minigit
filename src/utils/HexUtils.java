package utils;

public final class HexUtils {
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xFF)); // convert signed byte to unsigned
        }
        return sb.toString();
    }

    public static byte[] hexToBytes(String hexString) {
        if (hexString == null || hexString.isEmpty()) {
            return new byte[0];
        }

        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have an even length.");
        }

        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // Parse each two-character hex substring into an integer (base 16)
            // and cast it to a byte.
            data[i / 2] = (byte) Integer.parseInt(hexString.substring(i, i + 2), 16);
        }
        return data;
    }
}

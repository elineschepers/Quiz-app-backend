package be.ucll.quizappbackend.Util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Hasher {
    public static String sha256(String str) {
        return Hashing.sha256().hashString(str, StandardCharsets.UTF_8).toString();
    }
}

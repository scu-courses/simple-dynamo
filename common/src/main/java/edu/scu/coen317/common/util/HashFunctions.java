package edu.scu.coen317.common.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import edu.scu.coen317.common.Configuration;

import java.util.Random;

public class HashFunctions {
    private static final String seed = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomMD5() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            sb.append(seed.charAt(random.nextInt(seed.length())));
        }
        return sb.toString();
    }

    public static String md5Hash(String key) {
        HashFunction hf = Hashing.md5();
        HashCode hc = hf.newHasher().putString(key, Configuration.CHARSET).hash();
        return hc.toString();
    }
}

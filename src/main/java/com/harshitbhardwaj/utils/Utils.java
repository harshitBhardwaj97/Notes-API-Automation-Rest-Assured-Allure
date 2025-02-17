package com.harshitbhardwaj.utils;

import java.util.Random;
import java.util.UUID;

public final class Utils {

    private static final String[] NOTE_CATEGORIES = {"Home", "Work", "Personal"};
    private static final boolean[] NOTE_COMPLETION = {true, false};

    private Utils() {
        throw new AssertionError("Cannot instantiate Utils.class");
    }

    public static String getRandomNoteCategory() {
        Random random = new Random();
        int index = random.nextInt(NOTE_CATEGORIES.length);
        return NOTE_CATEGORIES[index];
    }

    public static String generateRandomHexId(int length) {
        UUID uuid = UUID.randomUUID();
        String hexId = uuid.toString().replace("-", "").substring(0, length);
        return hexId;
    }

    public static boolean getRandomNoteCompletion() {
        Random random = new Random();
        int index = random.nextInt(NOTE_COMPLETION.length);
        return NOTE_COMPLETION[index];
    }

}

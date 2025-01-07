package by.zgrundo.braintests.models;

public enum SpeedAlphabetTextType {
    SYLLABLES("Слоги"),
    WORDS("Слова"),
    TEXT("Текст");

    private final String displayName;

    SpeedAlphabetTextType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


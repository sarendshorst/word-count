package sarendshorst.wordcount;

import sarendshorst.wordcount.provided.WordFrequency;

public record WordFrequencyImpl(String word, int frequency) implements WordFrequency {
    @Override
    public String getWord() {
        return word;
    }

    @Override
    public int getFrequency() {
        return frequency;
    }
}
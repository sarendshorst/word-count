package sarendshorst.wordcount;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sarendshorst.wordcount.provided.WordFrequency;
import sarendshorst.wordcount.provided.WordFrequencyAnalyzer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WordCountApplicationTests {
    private static final String WORDS = "The sun shines over the lake";
    private static final String WORDS_WITH_DOUBLE_SEPARATORS = "The  sun  shines  over  the  lake";
    private static final String NO_WORDS = "123456789,.";
    private final WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzerImpl();

    @Test
    void calculateHighestFrequency() {
        assertEquals(2, analyzer.calculateHighestFrequency(WORDS));
        assertEquals(2, analyzer.calculateHighestFrequency(WORDS_WITH_DOUBLE_SEPARATORS));
        assertEquals(0, analyzer.calculateHighestFrequency(NO_WORDS));
        assertEquals(0, analyzer.calculateHighestFrequency(null));
        assertEquals(0, analyzer.calculateHighestFrequency(""));
    }

    @Test
    void calculateFrequencyForWord() {
        assertEquals(1, analyzer.calculateFrequencyForWord(WORDS, "lake"));
        assertEquals(1, analyzer.calculateFrequencyForWord(WORDS_WITH_DOUBLE_SEPARATORS, "lake"));
        assertEquals(0, analyzer.calculateFrequencyForWord(NO_WORDS, "lake"));

        assertEquals(0, analyzer.calculateFrequencyForWord(WORDS, null));
        assertEquals(0, analyzer.calculateFrequencyForWord(WORDS_WITH_DOUBLE_SEPARATORS, null));
        assertEquals(0, analyzer.calculateFrequencyForWord(NO_WORDS, null));

        assertEquals(0, analyzer.calculateFrequencyForWord(WORDS, ""));
        assertEquals(0, analyzer.calculateFrequencyForWord(WORDS_WITH_DOUBLE_SEPARATORS, ""));
        assertEquals(0, analyzer.calculateFrequencyForWord(NO_WORDS, ""));

        assertEquals(0, analyzer.calculateFrequencyForWord(null, null));
        assertEquals(0, analyzer.calculateFrequencyForWord(null, ""));
        assertEquals(0, analyzer.calculateFrequencyForWord("", null));
    }

    @Test
    void calculateMostFrequentNWords() {
        List<WordFrequency> wordFrequencies = new ArrayList<>();
        wordFrequencies.add(new WordFrequencyImpl("the", 2));
        wordFrequencies.add(new WordFrequencyImpl("lake", 1));
        wordFrequencies.add(new WordFrequencyImpl("over", 1));
        assertEquals(wordFrequencies, analyzer.calculateMostFrequentNWords(WORDS, 3));
        assertEquals(wordFrequencies, analyzer.calculateMostFrequentNWords(WORDS_WITH_DOUBLE_SEPARATORS, 3));

        List<WordFrequency> emptyList = new ArrayList<>();
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords(WORDS, 0));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords(WORDS, -1));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords(WORDS_WITH_DOUBLE_SEPARATORS, 0));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords(WORDS_WITH_DOUBLE_SEPARATORS, -1));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords(NO_WORDS, 0));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords(NO_WORDS, -1));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords(NO_WORDS, 1));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords(null, 0));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords(null, -1));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords(null, 1));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords("", 0));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords("", -1));
        assertEquals(emptyList, analyzer.calculateMostFrequentNWords("", 1));
    }
}
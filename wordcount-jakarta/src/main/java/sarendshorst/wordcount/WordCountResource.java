package sarendshorst.wordcount;

import jakarta.annotation.Nonnull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import sarendshorst.wordcount.provided.WordFrequency;

import java.util.*;
import java.util.stream.Collectors;

@Path("word-count")
public class WordCountResource {

    /**
     * Returns the highest frequency in the text. Several words might have this frequency.
     *
     * @param text The text that contains words of which it should calculate the frequencies
     * @return The highest frequency
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("highest-frequency")
    public int calculateHighestFrequency(@QueryParam("text") String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        List<WordFrequency> wordFrequencies = calculateWordFrequencies(text);
        if (wordFrequencies.isEmpty()) {
            return 0;
        } else {
            return Collections.max(wordFrequencies, Comparator.comparingInt(WordFrequency::getFrequency)).getFrequency();
        }
    }

    /**
     * Returns the frequency of the specified word.
     *
     * @param text The text that contains the word of which it should find the frequency
     * @param word The words to find the frequency of
     * @return The frequency of the word
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("frequency-for-word")
    public int calculateFrequencyForWord(@QueryParam("text") String text, @QueryParam("word") String word) {
        if (text == null || text.trim().isEmpty() || word == null || word.trim().isEmpty()) {
            return 0;
        }

        WordFrequency wordFrequency = calculateWordFrequencies(text).stream()
                .filter(o -> o.getWord().equalsIgnoreCase(word))
                .findFirst()
                .orElse(null);

        if (wordFrequency == null) {
            return 0;
        } else {
            return wordFrequency.getFrequency();
        }
    }

    /**
     * Returns a list of the most frequent „n‟ words in the input text, all the words returned in lower case.
     * If several words have the same frequency, this method should return them in ascendant alphabetical order.
     * For input text “The sun shines over the lake” and n = 3, it should return the list {(“the”, 2), (“lake”, 1), (“over”, 1) }
     *
     * @param text The text that contains words of which it should calculate the frequencies
     * @param n    The amount to limit the list
     * @return A list of WordFrequency
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("most-frequent-n-words")
    public List<WordFrequency> calculateMostFrequentNWords(@QueryParam("text") String text, @QueryParam("n") int n) {
        if (text == null || text.trim().isEmpty() || n < 0) {
            return new ArrayList<>();
        }

        return calculateWordFrequencies(text).stream()
                .sorted(Comparator.comparingInt(WordFrequency::getFrequency).reversed().thenComparing(WordFrequency::getWord)) // sort by highest frequency first, then by ascending alphabetic
                .limit(n) // only return the most frequent limited by `n`
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of words with frequencies of every word in the given text.
     *
     * @param text The text that contains words of which it should calculate the frequencies
     * @return A list of WordFrequency
     */
    private List<WordFrequency> calculateWordFrequencies(@Nonnull String text) {
        return Arrays.stream(text.split("[^a-zA-Z]")) // every character besides a to z and A to Z is a separator
                .filter(s -> !s.isEmpty()) // remove empty splits resulting from back to back separators
                .collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting())) // count lower cased splits
                .entrySet().stream().map(e -> new WordFrequencyImpl(e.getKey(), e.getValue().intValue())).collect(Collectors.toList()); // convert to list of custom object
    }
}
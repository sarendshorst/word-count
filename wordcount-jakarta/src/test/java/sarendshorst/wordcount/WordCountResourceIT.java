package sarendshorst.wordcount;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordCountResourceIT {
    private static final String BASE_URL = "http://localhost:8080/word-count/rest/word-count/";
    private static final String WORDS = URLEncoder.encode("The sun shines over the lake", StandardCharsets.UTF_8);
    private static final String WORDS_WITH_DOUBLE_SEPARATORS = URLEncoder.encode("The  sun  shines  over  the  lake", StandardCharsets.UTF_8);
    private static final String NO_WORDS = "123456789,.";
    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void calculateHighestFrequency() throws Exception {
        String path = "highest-frequency?text=%s";
        assertEquals("2", getResponse(path.formatted(WORDS)));
        assertEquals("2", getResponse(path.formatted(WORDS_WITH_DOUBLE_SEPARATORS)));
        assertEquals("0", getResponse(path.formatted(NO_WORDS)));
        assertEquals("0", getResponse(path.formatted("")));
    }

    @Test
    void calculateFrequencyForWord() throws Exception {
        String path = "frequency-for-word?text=%s&word=%s";
        assertEquals("1", getResponse(path.formatted(WORDS, "lake")));
        assertEquals("1", getResponse(path.formatted(WORDS_WITH_DOUBLE_SEPARATORS, "lake")));
        assertEquals("0", getResponse(path.formatted(NO_WORDS, "lake")));

        assertEquals("0", getResponse(path.formatted(WORDS, "")));
        assertEquals("0", getResponse(path.formatted(WORDS_WITH_DOUBLE_SEPARATORS, "")));
        assertEquals("0", getResponse(path.formatted(NO_WORDS, "")));
        assertEquals("0", getResponse(path.formatted("", "")));
    }

    @Test
    void calculateMostFrequentNWords() throws Exception {
        String path = "most-frequent-n-words?text=%s&n=%d";

        String expectedJson = "[{\"frequency\":2,\"word\":\"the\"},{\"frequency\":1,\"word\":\"lake\"},{\"frequency\":1,\"word\":\"over\"}]";
        assertEquals(expectedJson, getResponse(path.formatted(WORDS, 3)));
        assertEquals(expectedJson, getResponse(path.formatted(WORDS_WITH_DOUBLE_SEPARATORS, 3)));

        assertEquals("[]", getResponse(path.formatted(WORDS, 0)));
        assertEquals("[]", getResponse(path.formatted(WORDS, -1)));
        assertEquals("[]", getResponse(path.formatted(WORDS_WITH_DOUBLE_SEPARATORS, 0)));
        assertEquals("[]", getResponse(path.formatted(WORDS_WITH_DOUBLE_SEPARATORS, -1)));
        assertEquals("[]", getResponse(path.formatted(NO_WORDS, 0)));
        assertEquals("[]", getResponse(path.formatted(NO_WORDS, -1)));
        assertEquals("[]", getResponse(path.formatted(NO_WORDS, 1)));
        assertEquals("[]", getResponse(path.formatted("", 0)));
        assertEquals("[]", getResponse(path.formatted("", -1)));
        assertEquals("[]", getResponse(path.formatted("", 1)));
    }

    /**
     * Returns the response from a given String uri.
     *
     * @param uri The path to create a request from to be executed
     * @return The response body of the request
     */
    private String getResponse(String uri) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(new URI(BASE_URL + uri)).build();
        return client.send(request, BodyHandlers.ofString()).body();
    }
}
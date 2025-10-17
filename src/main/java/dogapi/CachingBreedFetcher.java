package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher underlyingFetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.underlyingFetcher = fetcher;

    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Check cache first
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }
        
        // Make the call and increment counter
        callsMade++;
        List<String> result = underlyingFetcher.getSubBreeds(breed);
        
        // Cache successful results only (not exceptions)
        cache.put(breed, result);
        return result;
    }
    public int getCallsMade() {
        return callsMade;
    }
}
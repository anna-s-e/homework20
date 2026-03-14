package org.skypro.skyshop.search;

import java.util.*;

public class SearchEngine {
    private Set<Searchable> searchables;

    public SearchEngine() {
        this.searchables = new HashSet<>();
       }

    public void add(Searchable searchable) {
        searchables.add(searchable);
    }

    public int getSize() {
        return searchables.size();
    }

    public Set<Searchable> search(String searchTerm) {
        Set<Searchable> resultSet = new TreeSet<>(new SearchableNameLengthComparator());
        String lowerCaseSearchTerm = searchTerm.toLowerCase();

        for (Searchable searchable : searchables) {
            if (searchable.getSearchTerm().toLowerCase().contains(lowerCaseSearchTerm)) {
                resultSet.add(searchable);
            }
        }
        return resultSet;
    }

    public Searchable findBestMatch(String searchTerm) throws BestResultNotFound {
        if (searchTerm == null || searchTerm.isBlank()) {
            throw new BestResultNotFound("Поисковый запрос не может быть пустым");
        }

        Searchable bestMatch = null;
        int maxOccurrences = 0;
        String lowerCaseSearchTerm = searchTerm.toLowerCase();

        for (Searchable current : searchables) {
            String content = current.getSearchTerm().toLowerCase();
            int occurrences = countOccurrences(content, lowerCaseSearchTerm);

            if (occurrences > maxOccurrences) {
                maxOccurrences = occurrences;
                bestMatch = current;
            }
        }

        if (bestMatch == null) {
            throw new BestResultNotFound("Не найден подходящий результат для запроса: " + searchTerm);
        }
        return bestMatch;
    }

    private int countOccurrences(String text, String searchTerm) {
        if (searchTerm.isEmpty()) {
            return 0;
        }

        int count = 0;
        int index = 0;
        while ((index = text.indexOf(searchTerm, index)) != -1) {
            count++;
            index += searchTerm.length();
        }
        return count;
    }
}
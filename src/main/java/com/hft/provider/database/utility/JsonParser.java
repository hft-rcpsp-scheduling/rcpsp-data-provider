package com.hft.provider.database.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class JsonParser {

    /**
     * @param jsonString string as json list
     * @return list with integers
     */
    public static List<Integer> convertToList(String jsonString) {
        if (jsonString.equals("[]"))
            return new ArrayList<>();
        return Arrays.stream(jsonString
                        .replaceAll(" ", "")
                        .replace("[", "")
                        .replace("]", "")
                        .split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * @param list list with integers
     * @return string as json list
     */
    public static String convertToJsonString(List<Integer> list) {
        Iterator<Integer> iterator = list.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString());
            if (iterator.hasNext()) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }
}

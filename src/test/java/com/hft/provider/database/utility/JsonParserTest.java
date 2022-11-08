package com.hft.provider.database.utility;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utility.AssertExtension.assertContains;

class JsonParserTest {

    private final List<Integer> expectedList = List.of(1, 2, 3);

    @Test
    void convertToList() {
        assertEquals(expectedList, JsonParser.convertToList("[1,2,3]"));
        assertEquals(expectedList, JsonParser.convertToList("[1, 2, 3]"));
        assertEquals(expectedList, JsonParser.convertToList("[1 , 2 , 3]"));
        assertEquals(new ArrayList<>(), JsonParser.convertToList("[]"));
    }

    @Test
    void convertToJsonString() {
        String jsonArray = JsonParser.convertToJsonString(expectedList);

        assertContains(expectedList.get(0).toString(), jsonArray);
        assertContains(expectedList.get(1).toString(), jsonArray);
        assertContains(expectedList.get(2).toString(), jsonArray);
        assertTrue(jsonArray.startsWith("["), "Should start with '['");
        assertTrue(jsonArray.endsWith("]"), "Should end with ']'");

        assertEquals("[]", JsonParser.convertToJsonString(new ArrayList<>()));
    }
}

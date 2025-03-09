package utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonConverterUtilTest {

    @Test
    void testSimpleObject() {
        String json = "{ \"name\": \"John\", \"age\": 30, \"isAdmin\": true }";
        String expectedPhp = """
                [
                    'name' => 'John',
                    'age' => 30,
                    'isAdmin' => true,
                ]
                """.stripIndent();

        assertEquals(expectedPhp, JsonConverterUtil.convertJsonToPhpArray(new JSONObject(json), 0));
    }

    @Test
    void testNestedObject() {
        String json = "{ \"user\": { \"name\": \"Alice\", \"email\": \"alice@example.com\" } }";
        String expectedPhp = """
                [
                    'user' => [
                        'name' => 'Alice',
                        'email' => 'alice@example.com',
                    ],
                ]
                """.stripIndent();

        assertEquals(expectedPhp, JsonConverterUtil.convertJsonToPhpArray(new JSONObject(json), 0));
    }

    @Test
    void testArray() {
        String json = "[ \"apple\", \"banana\", \"cherry\" ]";
        String expectedPhp = """
                [
                    'apple',
                    'banana',
                    'cherry',
                ]
                """.stripIndent();

        assertEquals(expectedPhp, JsonConverterUtil.convertJsonToPhpArray(new JSONArray(json), 0));
    }

    @Test
    void testComplexArray() {
        String json = "{ \"fruits\": [ \"apple\", \"banana\", \"cherry\" ] }";
        String expectedPhp = """
                [
                    'fruits' => [
                        'apple',
                        'banana',
                        'cherry',
                    ],
                ]
                """.stripIndent();

        assertEquals(expectedPhp, JsonConverterUtil.convertJsonToPhpArray(new JSONObject(json), 0));
    }

    @Test
    void testBooleanAndNullValues() {
        String json = "{ \"active\": true, \"deleted\": false, \"value\": null }";
        String expectedPhp = """
                [
                    'active' => true,
                    'deleted' => false,
                    'value' => null,
                ]
                """.stripIndent();

        assertEquals(expectedPhp, JsonConverterUtil.convertJsonToPhpArray(new JSONObject(json), 0));
    }

    @Test
    void testInvalidJson() {
        String json = "{ \"name\": \"John\", \"age\": 30, ";
        assertThrows(org.json.JSONException.class, () -> new JSONObject(json));
    }

    @Test
    void testJsonWithBackslashKey() {
        String json = "{ \"Tests\\\\\": \"tests/\" }";
        JSONObject jsonObject = new JSONObject(json);

        String expectedPhpArray = "[\n    'Tests\\\\' => 'tests/',\n]";

        String actualPhpArray = JsonConverterUtil.convertJsonToPhpArray(jsonObject, 0);

        assertEquals(expectedPhpArray, actualPhpArray);
    }
}

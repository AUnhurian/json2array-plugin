package toolwindow;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import utils.JsonConverterUtil;

import static org.junit.jupiter.api.Assertions.*;

class JsonConverterTest {

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
}

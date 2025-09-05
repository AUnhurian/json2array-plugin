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

    @Test
    void testJsonWithSingleQuote() {
        String json = "{ \"name\": \"O'Reilly\" }";
        JSONObject jsonObject = new JSONObject(json);

        String expectedPhpArray = "[\n    'name' => 'O\\'Reilly',\n]";

        String actualPhpArray = JsonConverterUtil.convertJsonToPhpArray(jsonObject, 0);

        assertEquals(expectedPhpArray, actualPhpArray);
    }

    // Tests for PHP Array to JSON conversion
    @Test
    void testPhpArrayToJsonSimple() throws Exception {
        String phpArray = "[\n    'name' => 'John',\n    'age' => 30,\n    'isAdmin' => true,\n]";
        String expectedJson = "{\"name\":\"John\",\"age\":30,\"isAdmin\":true}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonNested() throws Exception {
        String phpArray = "[\n    'user' => [\n        'name' => 'Alice',\n        'email' => 'alice@example.com',\n    ],\n]";
        String expectedJson = "{\"user\":{\"name\":\"Alice\",\"email\":\"alice@example.com\"}}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonWithArray() throws Exception {
        String phpArray = "[\n    'fruits' => [\n        'apple',\n        'banana',\n        'cherry',\n    ],\n]";
        String expectedJson = "{\"fruits\":[\"apple\",\"banana\",\"cherry\"]}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonWithNullAndBoolean() throws Exception {
        String phpArray = "[\n    'active' => true,\n    'deleted' => false,\n    'value' => null,\n]";
        String expectedJson = "{\"active\":true,\"deleted\":false,\"value\":null}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonEmpty() throws Exception {
        String phpArray = "[]";
        String expectedJson = "{}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonIndexedArray() throws Exception {
        String phpArray = "[\n    'apple',\n    'banana',\n    'cherry',\n]";
        String expectedJson = "[\"apple\",\"banana\",\"cherry\"]";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonMixedArray() throws Exception {
        String phpArray = "[\n    0 => 'first',\n    1 => 'second',\n    'key' => 'value',\n]";
        String expectedJson = "{\"0\":\"first\",\"1\":\"second\",\"key\":\"value\"}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonInvalidInput() {
        String invalidPhpArray = "invalid php array";
        
        assertThrows(Exception.class, () -> {
            JsonConverterUtil.convertPhpArrayToJson(invalidPhpArray);
        });
    }

    @Test
    void testPhpArrayToJsonEmptyInput() {
        String emptyPhpArray = "";
        
        assertThrows(Exception.class, () -> {
            JsonConverterUtil.convertPhpArrayToJson(emptyPhpArray);
        });
    }

    @Test
    void testPhpArrayToJsonWithEscapedQuotes() throws Exception {
        String phpArray = "[\n    'name' => 'O\\'Reilly',\n    'path' => 'C:\\\\Users\\\\Test',\n]";
        String expectedJson = "{\"name\":\"O'Reilly\",\"path\":\"C:\\\\Users\\\\Test\"}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonComplexNested() throws Exception {
        String phpArray = "[\n    'user' => [\n        'name' => 'John',\n        'settings' => [\n            'theme' => 'dark',\n            'notifications' => true,\n        ],\n        'hobbies' => [\n            'coding',\n            'reading',\n        ],\n    ],\n]";
        String expectedJson = "{\"user\":{\"name\":\"John\",\"settings\":{\"theme\":\"dark\",\"notifications\":true},\"hobbies\":[\"coding\",\"reading\"]}}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonRealWorldExample() throws Exception {
        String phpArray = "[\n'quantity' => 1,\n'discount' => 0,\n'created_at' => '2025-02-27 14:05:10',\n'product_name' => 'Чоловіча синя піжама Porta',\n'sale_price' => 3322,\n'article' => 'svt-50508648-63f8-11ec-820b-305a3a0c66cb',\n'shop_id' => 6207,\n'status_id' => 8,\n'color_id' => 180,\n'price' => 3322,\n'product_id' => 3003,\n'delivery_status_id' => 4,\n'size_id' => 10,\n'tracking_number' => '20451112298539',\n'id' => 12027488,\n'barcode' => '50508648-63f8-11ec-820b-305a3a0c66cb',\n'remarks' => [],\n'product_link' => '/management-of-goods/goods/view?id=67b5a4a51a4ad3df5c0dbb82',\n]";
        String expectedJson = "{\"quantity\":1,\"discount\":0,\"created_at\":\"2025-02-27 14:05:10\",\"product_name\":\"Чоловіча синя піжама Porta\",\"sale_price\":3322,\"article\":\"svt-50508648-63f8-11ec-820b-305a3a0c66cb\",\"shop_id\":6207,\"status_id\":8,\"color_id\":180,\"price\":3322,\"product_id\":3003,\"delivery_status_id\":4,\"size_id\":10,\"tracking_number\":\"20451112298539\",\"id\":12027488,\"barcode\":\"50508648-63f8-11ec-820b-305a3a0c66cb\",\"remarks\":[],\"product_link\":\"/management-of-goods/goods/view?id=67b5a4a51a4ad3df5c0dbb82\"}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonSimpleCase() throws Exception {
        String phpArray = "['name' => 'John', 'age' => 30]";
        String expectedJson = "{\"name\":\"John\",\"age\":30}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonWithUkrainianText() throws Exception {
        String phpArray = "[\n'name' => 'Чоловіча синя піжама Porta',\n'price' => 3322,\n'active' => true,\n]";
        String expectedJson = "{\"name\":\"Чоловіча синя піжама Porta\",\"price\":3322,\"active\":true}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testPhpArrayToJsonWithEmptyArrayValue() throws Exception {
        String phpArray = "[\n    'name' => 'Test',\n    'items' => [],\n    'active' => true,\n]";
        String expectedJson = "{\"name\":\"Test\",\"items\":[],\"active\":true}";

        String actualJson = JsonConverterUtil.convertPhpArrayToJson(phpArray);
        assertEquals(expectedJson, actualJson);
    }
}

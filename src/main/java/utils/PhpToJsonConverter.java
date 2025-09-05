package utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility class specifically for converting PHP arrays to JSON format.
 * This class handles the complex parsing logic required to convert PHP array syntax
 * (including associative arrays, indexed arrays, nested structures, and various data types)
 * into valid JSON format with proper formatting.
 */
public class PhpToJsonConverter {
    
    /**
     * Converts a PHP array string to JSON format with pretty formatting.
     * 
     * @param phpArray The PHP array string to convert
     * @return Formatted JSON string
     * @throws Exception If the PHP array format is invalid or conversion fails
     */
    public static String convertPhpArrayToJson(String phpArray) throws Exception {
        if (phpArray == null || phpArray.trim().isEmpty()) {
            throw new Exception("Empty PHP array input!");
        }

        phpArray = phpArray.trim();
        
        try {
            // Use regex-based conversion approach
            String json = convertPhpArrayToJsonRegex(phpArray);
            return formatJsonPretty(json);
        } catch (Exception e) {
            throw new Exception("Failed to convert PHP array to valid JSON: " + e.getMessage());
        }
    }
    
    /**
     * Formats JSON string with pretty indentation.
     * 
     * @param json The JSON string to format
     * @return Pretty-formatted JSON string
     */
    private static String formatJsonPretty(String json) {
        try {
            // Try to parse and pretty print the JSON
            if (json.trim().startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.toString(2); // 2 spaces indentation
            } else if (json.trim().startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                return jsonArray.toString(2); // 2 spaces indentation
            }
            return json;
        } catch (Exception e) {
            // If pretty printing fails, return original JSON
            return json;
        }
    }
    
    /**
     * Main conversion method using regex-based approach.
     * 
     * @param phpArray The PHP array string to convert
     * @return JSON string
     * @throws Exception If conversion fails
     */
    private static String convertPhpArrayToJsonRegex(String phpArray) throws Exception {
        // Handle empty array
        if (phpArray.trim().isEmpty()) {
            return "[]";
        }
        
        // Remove outer brackets if present
        if (phpArray.trim().startsWith("[") && phpArray.trim().endsWith("]")) {
            String content = phpArray.trim().substring(1, phpArray.trim().length() - 1).trim();
            
            // Check if it's an associative array (has key => value pairs)
            if (isAssociativeArray(content)) {
                return convertAssociativeArrayRegex(content);
            }
            
            // If it's an indexed array
            return convertIndexedArrayRegex(content);
        }
        
        // Check if it's an associative array (has '=>')
        if (!phpArray.contains("=>")) {
            // Indexed array - convert to JSON array
            return convertIndexedArrayRegex(phpArray);
        } else {
            // Associative array - convert to JSON object
            return convertAssociativeArrayRegex(phpArray);
        }
    }
    
    /**
     * Determines if the content represents an associative array by checking for
     * key => value pairs at the top level (not inside nested arrays).
     * 
     * @param content The content to analyze
     * @return true if it's an associative array, false otherwise
     */
    private static boolean isAssociativeArray(String content) {
        // Check if content contains key => value pairs at the top level
        // We need to be more careful - only consider => at the top level, not inside nested arrays
        
        // First, let's check if there are any => at all
        if (!content.contains("=>")) {
            return false;
        }
        
        // Now check if the => are at the top level by parsing the structure
        // Look for patterns like 'key' => value (not inside [])
        boolean inString = false;
        boolean escaped = false;
        int bracketLevel = 0;
        
        for (int i = 0; i < content.length() - 1; i++) {
            char c = content.charAt(i);
            
            if (escaped) {
                escaped = false;
                continue;
            }
            
            if (c == '\\') {
                escaped = true;
                continue;
            }
            
            if (c == '\'') {
                inString = !inString;
            } else if (!inString) {
                if (c == '[') {
                    bracketLevel++;
                } else if (c == ']') {
                    bracketLevel--;
                } else if (c == '=' && content.charAt(i + 1) == '>' && bracketLevel == 0) {
                    // Found => at top level
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Converts associative array content to JSON object format.
     * 
     * @param content The associative array content
     * @return JSON object string
     * @throws Exception If conversion fails
     */
    private static String convertAssociativeArrayRegex(String content) throws Exception {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        // Split by commas, but be very careful with strings
        java.util.List<String> pairs = splitByCommaRegex(content);
        
        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.isEmpty()) continue;
            
            int arrowIndex = findArrowRegex(pair);
            if (arrowIndex == -1) {
                throw new Exception("Invalid associative array syntax: " + pair);
            }
            
            String key = pair.substring(0, arrowIndex).trim();
            String value = pair.substring(arrowIndex + 2).trim(); // Skip '=>'
            
            if (!first) {
                json.append(",");
            }
            first = false;
            
            // Convert key
            String jsonKey = convertValueRegex(key);
            if (jsonKey.startsWith("\"") && jsonKey.endsWith("\"")) {
                jsonKey = jsonKey.substring(1, jsonKey.length() - 1);
            }
            
            // Convert value
            String jsonValue = convertValueRegex(value);
            
            json.append("\"").append(jsonKey).append("\":").append(jsonValue);
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Converts indexed array content to JSON array format.
     * 
     * @param content The indexed array content
     * @return JSON array string
     * @throws Exception If conversion fails
     */
    private static String convertIndexedArrayRegex(String content) throws Exception {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        
        java.util.List<String> items = splitByCommaRegex(content);
        
        for (String item : items) {
            item = item.trim();
            if (item.isEmpty()) continue;
            
            if (!first) {
                json.append(",");
            }
            first = false;
            
            String jsonValue = convertValueRegex(item);
            json.append(jsonValue);
        }
        
        json.append("]");
        return json.toString();
    }
    
    /**
     * Converts individual PHP values to JSON format.
     * Handles strings, numbers, booleans, null, and nested arrays.
     * 
     * @param value The PHP value to convert
     * @return JSON value string
     * @throws Exception If conversion fails
     */
    private static String convertValueRegex(String value) throws Exception {
        value = value.trim();
        
        // Empty array
        if (value.equals("[]")) {
            return "[]";
        }
        
        // String values (single quotes)
        if (value.startsWith("'") && value.endsWith("'")) {
            String str = value.substring(1, value.length() - 1);
            // Escape special characters for JSON
            str = str.replace("\\", "\\\\");
            str = str.replace("\"", "\\\"");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
            str = str.replace("\t", "\\t");
            return "\"" + str + "\"";
        }
        
        // Boolean values
        if (value.equals("true")) return "true";
        if (value.equals("false")) return "false";
        
        // Null values
        if (value.equals("null")) return "null";
        
        // Numeric values
        try {
            if (value.contains(".")) {
                Double.parseDouble(value);
                return value;
            } else {
                Long.parseLong(value);
                return value;
            }
        } catch (NumberFormatException e) {
            // Not a number, continue
        }
        
        // Nested arrays - check for proper array structure
        if (value.startsWith("[") && value.endsWith("]")) {
            // Always try to process as array if it looks like one
            return convertPhpArrayToJsonRegex(value);
        }
        
        // If we get here, treat as string
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
    
    /**
     * Splits content by commas while respecting string boundaries and nested brackets.
     * 
     * @param content The content to split
     * @return List of split items
     */
    private static java.util.List<String> splitByCommaRegex(String content) {
        java.util.List<String> result = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        boolean escaped = false;
        int bracketLevel = 0;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (escaped) {
                current.append(c);
                escaped = false;
                continue;
            }
            
            if (c == '\\') {
                escaped = true;
                current.append(c);
                continue;
            }
            
            if (c == '\'') {
                inString = !inString;
                current.append(c);
            } else if (!inString) {
                if (c == '[') {
                    bracketLevel++;
                    current.append(c);
                } else if (c == ']') {
                    bracketLevel--;
                    current.append(c);
                } else if (c == ',' && bracketLevel == 0) {
                    String item = current.toString().trim();
                    if (!item.isEmpty()) {
                        result.add(item);
                    }
                    current = new StringBuilder();
                } else {
                    current.append(c);
                }
            } else {
                current.append(c);
            }
        }
        
        String item = current.toString().trim();
        if (!item.isEmpty()) {
            result.add(item);
        }
        
        return result;
    }
    
    /**
     * Finds the position of the => operator in a key-value pair.
     * Respects string boundaries to avoid false matches.
     * 
     * @param pair The key-value pair string
     * @return Position of => operator, or -1 if not found
     */
    private static int findArrowRegex(String pair) {
        boolean inString = false;
        boolean escaped = false;
        
        for (int i = 0; i < pair.length() - 1; i++) {
            char c = pair.charAt(i);
            
            if (escaped) {
                escaped = false;
                continue;
            }
            
            if (c == '\\') {
                escaped = true;
                continue;
            }
            
            if (c == '\'') {
                inString = !inString;
            } else if (!inString && c == '=' && pair.charAt(i + 1) == '>') {
                return i;
            }
        }
        
        return -1;
    }
}

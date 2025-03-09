package utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonConverterUtil {

    public static Object validateAndParseJson(String jsonText) throws Exception {
        jsonText = sanitizeJsonInput(jsonText);

        if (jsonText.startsWith("{")) {
            return new JSONObject(jsonText);
        } else if (jsonText.startsWith("[")) {
            return new JSONArray(jsonText);
        } else {
            throw new Exception("Invalid JSON format!");
        }
    }

    public static String convertJsonToPhpArray(Object json, int indentLevel) {
        String indent = "    ".repeat(indentLevel);

        if (json instanceof JSONObject jsonObject) {
            StringBuilder phpArray = new StringBuilder("[\n");

            for (String key : jsonObject.keySet()) {
                String escapedKey = key.replace("\\", "\\\\");

                Object value = jsonObject.get(key);
                phpArray.append(indent).append("    '").append(escapedKey).append("' => ")
                        .append(convertJsonToPhpArray(value, indentLevel + 1)).append(",\n");
            }

            phpArray.append(indent).append("]");
            return phpArray.toString();
        } else if (json instanceof JSONArray jsonArray) {
            StringBuilder phpArray = new StringBuilder("[\n");

            for (int i = 0; i < jsonArray.length(); i++) {
                phpArray.append(indent).append("    ").append(convertJsonToPhpArray(jsonArray.get(i), indentLevel + 1)).append(",\n");
            }

            phpArray.append(indent).append("]");
            return phpArray.toString();
        } else if (json instanceof String) {
            return "'" + ((String) json).replace("\\", "\\\\") + "'";
        } else if (json instanceof Number || json instanceof Boolean) {
            return json.toString();
        } else if (json == JSONObject.NULL) {
            return "null";
        }

        return "null";
    }

    public static void replaceSelectedText(com.intellij.openapi.editor.Editor editor, String newText) {
        if (editor != null) {
            com.intellij.openapi.editor.Document document = editor.getDocument();
            com.intellij.openapi.editor.SelectionModel selectionModel = editor.getSelectionModel();

            com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
                if (selectionModel.hasSelection()) {
                    document.replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), newText);
                }
            });
        }
    }

    public static String sanitizeJsonInput(String input) {
        if (input == null) return "";
        input = input.trim();

        if ((input.startsWith("\"") && input.endsWith("\"")) || (input.startsWith("'") && input.endsWith("'"))) {
            input = input.substring(1, input.length() - 1);
        }

        return input;
    }
}

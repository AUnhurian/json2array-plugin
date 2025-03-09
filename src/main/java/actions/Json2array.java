package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


public class Json2array extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Отримуємо редактор та виділений текст
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        String selectedText = null;

        if (editor != null) {
            SelectionModel selectionModel = editor.getSelectionModel();
            selectedText = selectionModel.getSelectedText();
        }

        // Видаляємо зайві лапки
        String jsonInput = sanitizeJsonInput(selectedText);

        // Відображаємо вікно введення з очищеним текстом
        jsonInput = Messages.showInputDialog(
                "Enter JSON:", "JSON to PHP Array", Messages.getQuestionIcon(),
                jsonInput, null
        );

        if (jsonInput == null || jsonInput.trim().isEmpty()) {
            Messages.showErrorDialog("Invalid JSON input!", "Error");
            return;
        }

        // Перевіряємо, чи JSON валідний
        if (!isValidJson(jsonInput)) {
            Messages.showErrorDialog("Invalid JSON format! Please enter a valid JSON object.", "Error");
            return;
        }

        try {
            // Конвертуємо JSON у PHP масив
            JSONObject jsonObject = new JSONObject(jsonInput);
            String phpArray = convertJsonToPhpArray(jsonObject);

            // Відображаємо результат та даємо можливість замінити текст
            int result = Messages.showYesNoCancelDialog(
                    phpArray, "Converted PHP Array",
                    "Replace Selection", "Copy to Clipboard", "Cancel",
                    Messages.getInformationIcon()
            );

            if (result == Messages.YES) {
                replaceSelectedText(editor, phpArray);
            } else if (result == Messages.NO) {
                copyToClipboard(phpArray);
            }
        } catch (Exception ex) {
            Messages.showErrorDialog("Unexpected error occurred!", "Error");
        }
    }

    private boolean isValidJson(String input) {
        try {
            new JSONObject(input);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private String sanitizeJsonInput(String input) {
        if (input == null) return "";
        input = input.trim();
        if ((input.startsWith("\"") && input.endsWith("\"")) || (input.startsWith("'") && input.endsWith("'"))) {
            input = input.substring(1, input.length() - 1);
        }
        return input;
    }

    private String convertJsonToPhpArray(Object json) {
        if (json instanceof JSONObject) {
            StringBuilder phpArray = new StringBuilder("[\n");
            JSONObject jsonObject = (JSONObject) json;

            for (String key : jsonObject.keySet()) {
                Object value = jsonObject.get(key);
                phpArray.append("    '").append(key).append("' => ").append(convertJsonToPhpArray(value)).append(",\n");
            }

            phpArray.append("]");
            return phpArray.toString();
        } else if (json instanceof JSONArray) {
            StringBuilder phpArray = new StringBuilder("[\n");
            JSONArray jsonArray = (JSONArray) json;

            for (int i = 0; i < jsonArray.length(); i++) {
                phpArray.append("    ").append(convertJsonToPhpArray(jsonArray.get(i))).append(",\n");
            }

            phpArray.append("]");
            return phpArray.toString();
        } else if (json instanceof String) {
            return "'" + json + "'";
        } else if (json instanceof Number || json instanceof Boolean) {
            return json.toString();
        } else if (json == JSONObject.NULL) {
            return "null";
        }

        return "null"; // fallback
    }

    private void replaceSelectedText(Editor editor, String newText) {
        if (editor != null) {
            Document document = editor.getDocument();
            SelectionModel selectionModel = editor.getSelectionModel();
            CaretModel caretModel = editor.getCaretModel();

            WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
                if (selectionModel.hasSelection()) {
                    document.replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), newText);
                } else {
                    document.insertString(caretModel.getOffset(), newText);
                }
            });
        }
    }

    private void copyToClipboard(String text) {
        java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(text);
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    }
}

package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Json2array extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Отримуємо редактор і виділений текст
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        String selectedText = null;

        if (editor != null) {
            SelectionModel selectionModel = editor.getSelectionModel();
            selectedText = selectionModel.getSelectedText();
        }

        // Якщо є виділений текст, підставляємо його у вікно введення
        String jsonInput = Messages.showInputDialog(
                "Enter JSON:", "JSON to PHP Array", Messages.getQuestionIcon(),
                selectedText != null ? selectedText : "", null
        );

        if (jsonInput == null || jsonInput.trim().isEmpty()) {
            Messages.showErrorDialog("Invalid JSON input!", "Error");
            return;
        }

        try {
            // Конвертуємо JSON у PHP масив
            JSONObject jsonObject = new JSONObject(jsonInput);
            String phpArray = convertJsonToPhpArray(jsonObject.toString(2));

            // Виводимо результат у спливаючому вікні
            Messages.showInfoMessage(phpArray, "Converted PHP Array");
        } catch (Exception ex) {
            Messages.showErrorDialog("Invalid JSON format!", "Error");
        }
    }

    private String convertJsonToPhpArray(String json) {
        return "<?php\nreturn " + json
                .replace("{", "[")
                .replace("}", "]")
                .replace(":", " =>")
                .replace("\"", "'") + ";";
    }
}

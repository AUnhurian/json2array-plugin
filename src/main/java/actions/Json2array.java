package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import com.intellij.openapi.command.WriteCommandAction;

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

            // Відобразити результат та запропонувати заміну
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
            Messages.showErrorDialog("Invalid JSON format!", "Error");
        }
    }

    private String convertJsonToPhpArray(String json) {
        return json
                .replace("{", "[")
                .replace("}", "]")
                .replace(":", " =>")
                .replace("\"", "'");
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

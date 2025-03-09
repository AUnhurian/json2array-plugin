package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import utils.JsonConverterUtil;

public class QuickJsonToPhpAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;

        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            Messages.showErrorDialog("No JSON selected!", "Error");
            return;
        }

        try {
            Object jsonObject = JsonConverterUtil.validateAndParseJson(selectedText);
            String phpArray = JsonConverterUtil.convertJsonToPhpArray(jsonObject, 0);
            JsonConverterUtil.replaceSelectedText(editor, phpArray);
        } catch (Exception ex) {
            Messages.showErrorDialog("Invalid JSON format!\n\n" + ex.getMessage(), "JSON Error");
        }
    }
}

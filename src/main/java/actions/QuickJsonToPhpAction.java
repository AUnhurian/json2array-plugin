package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import toolwindow.JsonConverterToolWindowService;
import utils.JsonConverterUtil;
import toolwindow.JsonConverterToolWindowFactory;

public class QuickJsonToPhpAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        var project = e.getProject();
        if (project == null) return;

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;

        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            Messages.showErrorDialog(project, "No JSON selected!", "Error");
            return;
        }

        try {
            Object jsonObject = JsonConverterUtil.validateAndParseJson(selectedText);
            String phpArray = JsonConverterUtil.convertJsonToPhpArray(jsonObject, 0);
            JsonConverterUtil.replaceSelectedText(editor, phpArray);

            JsonConverterToolWindowFactory toolWindow = JsonConverterToolWindowService.getInstance(project).getToolWindow();
            if (toolWindow != null) {
                toolWindow.setJsonInput(selectedText);
            }
        } catch (Exception ex) {
            Messages.showErrorDialog(project, "Invalid JSON format!\n\n" + ex.getMessage(), "JSON Error");
        }
    }
}

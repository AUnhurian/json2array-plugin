package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;
import toolwindow.JsonConverterToolWindowFactory;
import toolwindow.JsonConverterToolWindowService;
import utils.JsonConverterUtil;

import java.util.Objects;

public class Json2array extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        String selectedJson = e.getData(CommonDataKeys.EDITOR) != null ?
                Objects.requireNonNull(e.getData(CommonDataKeys.EDITOR)).getSelectionModel().getSelectedText() : "";

        if (selectedJson == null || selectedJson.trim().isEmpty()) {
            Messages.showErrorDialog(project, "No JSON selected!", "Error");
            return;
        }

        try {
            JsonConverterUtil.validateAndParseJson(selectedJson);
        } catch (Exception ex) {
            Messages.showErrorDialog(project, "Invalid JSON format!\n\n" + ex.getMessage(), "JSON Error");
            return;
        }

        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Json to PHP Array");
        if (toolWindow != null) {
            if (!toolWindow.isVisible()) {
                toolWindow.show();
            }

            JsonConverterToolWindowFactory factory = JsonConverterToolWindowService.getInstance(project).getToolWindow();
            if (factory != null) {
                factory.setJsonInput(selectedJson);
            }
        }
    }
}

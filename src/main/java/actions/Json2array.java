package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;
import toolwindow.JsonConverterToolWindowFactory;

import java.util.Objects;

public class Json2array extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String selectedJson = e.getData(CommonDataKeys.EDITOR) != null ?
                Objects.requireNonNull(e.getData(CommonDataKeys.EDITOR)).getSelectionModel().getSelectedText() : "";

        ToolWindow toolWindow = ToolWindowManager.getInstance(Objects.requireNonNull(e.getProject())).getToolWindow("Json to PHP Array");
        if (toolWindow != null) {
            toolWindow.show(() -> {
                JsonConverterToolWindowFactory.getInstance().setJsonInput(selectedJson);
            });
        }
    }
}

package toolwindow;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

@Service(Service.Level.PROJECT)
public final class JsonConverterToolWindowService {
    private JsonConverterToolWindowFactory toolWindow;

    public void setToolWindow(JsonConverterToolWindowFactory toolWindow) {
        this.toolWindow = toolWindow;
    }

    @Nullable
    public JsonConverterToolWindowFactory getToolWindow() {
        return toolWindow;
    }

    public static JsonConverterToolWindowService getInstance(Project project) {
        return project.getService(JsonConverterToolWindowService.class);
    }
}

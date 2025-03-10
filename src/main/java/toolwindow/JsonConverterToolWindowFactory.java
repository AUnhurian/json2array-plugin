package toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import utils.JsonConverterUtil;

import javax.swing.*;
import java.awt.*;

public class JsonConverterToolWindowFactory implements ToolWindowFactory {
    private JBTextArea jsonInputArea;
    private JBTextArea phpOutputArea;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JPanel panel = new JPanel(new BorderLayout());

        jsonInputArea = new JBTextArea(10, 50);
        phpOutputArea = new JBTextArea(10, 50);
        phpOutputArea.setEditable(true);

        JBScrollPane inputScroll = new JBScrollPane(jsonInputArea);
        JBScrollPane outputScroll = new JBScrollPane(phpOutputArea);

        JButton convertButton = new JButton("Convert to PHP Array");
        JButton replaceButton = new JButton("Replace in Editor");

        JBLabel outputLabel = new JBLabel("Output:");
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(convertButton);
        buttonPanel.add(replaceButton);

        panel.add(inputScroll, BorderLayout.NORTH);
        panel.add(outputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);

        convertButton.addActionListener(e -> {
            String jsonText = jsonInputArea.getText().trim();
            try {
                Object jsonObject = JsonConverterUtil.validateAndParseJson(jsonText);
                String phpArray = JsonConverterUtil.convertJsonToPhpArray(jsonObject, 0);
                phpOutputArea.setText(phpArray);
            } catch (Exception ex) {
                Messages.showErrorDialog(project, "Invalid JSON format!\n\nError: " + ex.getMessage(), "JSON Error");
            }
        });

        replaceButton.addActionListener(e -> {
            var editor = com.intellij.openapi.editor.EditorFactory.getInstance().getAllEditors();
            if (editor.length > 0) {
                JsonConverterUtil.replaceSelectedText(editor[0], phpOutputArea.getText());
            }
        });

        JsonConverterToolWindowService.getInstance(project).setToolWindow(this);
    }

    public void setJsonInput(String json) {
        jsonInputArea.setText(json);
    }

    public static JsonConverterToolWindowFactory getInstance(Project project) {
        return JsonConverterToolWindowService.getInstance(project).getToolWindow();
    }
}

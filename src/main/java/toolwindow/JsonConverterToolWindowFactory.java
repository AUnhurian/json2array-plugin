package toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.SelectionModel;

import javax.swing.*;
import java.awt.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonConverterToolWindowFactory implements ToolWindowFactory {
    private static JsonConverterToolWindowFactory instance;
    private JBTextArea jsonInputArea;
    private JBTextArea phpOutputArea;

    public static JsonConverterToolWindowFactory getInstance() {
        return instance;
    }

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        instance = this;

        JPanel panel = new JPanel(new BorderLayout());

        jsonInputArea = new JBTextArea(10, 50);
        phpOutputArea = new JBTextArea(10, 50);
        phpOutputArea.setEditable(false);

        JBScrollPane inputScroll = new JBScrollPane(jsonInputArea);
        JBScrollPane outputScroll = new JBScrollPane(phpOutputArea);

        JButton convertButton = new JButton("Convert to PHP Array");
        JButton replaceButton = new JButton("Replace in Editor");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(convertButton);
        buttonPanel.add(replaceButton);

        panel.add(inputScroll, BorderLayout.NORTH);
        panel.add(outputScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);

        convertButton.addActionListener(e -> {
            String jsonText = jsonInputArea.getText().trim();

            // Перевіряємо, чи JSON валідний
            try {
                Object jsonObject;

                // Якщо це об'єкт { ... }
                if (jsonText.startsWith("{")) {
                    jsonObject = new JSONObject(jsonText);
                }
                // Якщо це масив [ ... ]
                else if (jsonText.startsWith("[")) {
                    jsonObject = new JSONArray(jsonText);
                }
                // Інакше помилка
                else {
                    phpOutputArea.setText("❌ Invalid JSON format!");
                    return;
                }

                // Конвертуємо в PHP array
                String phpArray = convertJsonToPhpArray(jsonObject, 0);
                phpOutputArea.setText(phpArray);
            } catch (Exception ex) {
                phpOutputArea.setText("❌ Invalid JSON format!");
            }
        });

        replaceButton.addActionListener(e -> {
            Editor editor = com.intellij.openapi.editor.EditorFactory.getInstance().getAllEditors()[0];
            if (editor != null) {
                replaceSelectedText(editor, phpOutputArea.getText());
            }
        });
    }

    public void setJsonInput(String json) {
        jsonInputArea.setText(json);
    }

    private String convertJsonToPhpArray(Object json, int indentLevel) {
        String indent = "    ".repeat(indentLevel); // Tabulation

        if (json instanceof JSONObject) {
            StringBuilder phpArray = new StringBuilder("[\n");
            JSONObject jsonObject = (JSONObject) json;

            for (String key : jsonObject.keySet()) {
                Object value = jsonObject.get(key);
                phpArray.append(indent).append("    '").append(key).append("' => ")
                        .append(convertJsonToPhpArray(value, indentLevel + 1)).append(",\n");
            }

            phpArray.append(indent).append("]");
            return phpArray.toString();
        } else if (json instanceof JSONArray) {
            StringBuilder phpArray = new StringBuilder("[\n");
            JSONArray jsonArray = (JSONArray) json;

            for (int i = 0; i < jsonArray.length(); i++) {
                phpArray.append(indent).append("    ").append(convertJsonToPhpArray(jsonArray.get(i), indentLevel + 1)).append(",\n");
            }

            phpArray.append(indent).append("]");
            return phpArray.toString();
        } else if (json instanceof String) {
            return "'" + json + "'";
        } else if (json instanceof Number || json instanceof Boolean) {
            return json.toString();
        } else if (json == JSONObject.NULL) {
            return "null";
        }

        return "null";
    }

    private void replaceSelectedText(Editor editor, String newText) {
        if (editor != null) {
            Document document = editor.getDocument();
            SelectionModel selectionModel = editor.getSelectionModel();

            WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
                if (selectionModel.hasSelection()) {
                    document.replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), newText);
                }
            });
        }
    }
}

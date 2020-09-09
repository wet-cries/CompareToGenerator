package org.example.ui;

import com.intellij.util.ui.AbstractTableCellEditor;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SortedOrderColumnInfoCellRendererEditor extends AbstractTableCellEditor implements
        TableCellRenderer {
    private final JRadioButton ascendingOrderButton;
    private final JRadioButton descendingOrderButton;
    private final JPanel buttonPanel;

    public SortedOrderColumnInfoCellRendererEditor() {
        this.ascendingOrderButton = new JRadioButton("Ascending");
        ascendingOrderButton.setSelected(true);

        descendingOrderButton = new JRadioButton("Descending");
        descendingOrderButton.setSelected(false);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(ascendingOrderButton);
        buttonGroup.add(descendingOrderButton);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(ascendingOrderButton);
        buttonPanel.add(descendingOrderButton);

        ascendingOrderButton.addActionListener(new StopTableEditingActionListener(buttonPanel));
        descendingOrderButton.addActionListener(new StopTableEditingActionListener(buttonPanel));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (!getCellEditorValue().equals(value)) {
            ascendingOrderButton.setSelected((Boolean.TRUE.equals(value)));
            descendingOrderButton.setSelected((Boolean.FALSE.equals(value)));
        }

        return buttonPanel;
    }

    @Override
    public Object getCellEditorValue() {
        return ascendingOrderButton.isSelected();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (!getCellEditorValue().equals(value)) {
            ascendingOrderButton.setSelected((Boolean.TRUE.equals(value)));
            descendingOrderButton.setSelected((Boolean.FALSE.equals(value)));
        }

        return buttonPanel;
    }

    private static class StopTableEditingActionListener implements ActionListener {
        private final JPanel buttonPanel;

        private StopTableEditingActionListener(JPanel buttonPanel) {
            this.buttonPanel = buttonPanel;
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            MyTable parentTable = (MyTable) buttonPanel.getParent();
            TableCellEditor editor = parentTable.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }
    }
}

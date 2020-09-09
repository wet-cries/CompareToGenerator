package org.example.ui;

import com.intellij.util.ui.ListTableModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class MyTable extends JTable {
    public MyTable(ListTableModel<?> myFields) {
        super(myFields);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        TableModel tableModel = getModel();
        ListTableModel<?> listTableModel = (ListTableModel<?>) tableModel;

        TableCellRenderer renderer = listTableModel.getColumnInfos()[column]
                .getRenderer(listTableModel.getItem(row));
        if (renderer != null) {
            return renderer;
        } else {
            return super.getCellRenderer(row, column);
        }
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        TableModel tableModel = getModel();
        ListTableModel<?> listTableModel = (ListTableModel<?>) tableModel;

        TableCellEditor editor = listTableModel.getColumnInfos()[column].getEditor(listTableModel.getItem(row));
        if (editor != null) {
            return editor;
        } else {
            return super.getCellEditor(row, column);
        }
    }
}

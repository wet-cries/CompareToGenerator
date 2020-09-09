package org.example.ui;

import com.intellij.openapi.util.NlsContexts;
import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class SortedOrderColumnInfo extends ColumnInfo<PsiFieldWithSortedOrder, Boolean> {
    private final SortedOrderColumnInfoCellRendererEditor rendererEditor = new SortedOrderColumnInfoCellRendererEditor();

    public SortedOrderColumnInfo(@NlsContexts.ColumnName String name) {
        super(name);
    }

    @Override
    public @Nullable Boolean valueOf(PsiFieldWithSortedOrder psiFieldWithSortedOrder) {
        return psiFieldWithSortedOrder.isAscending();
    }

    @Override
    public void setValue(PsiFieldWithSortedOrder psiFieldWithSortedOrder, Boolean value) {
        psiFieldWithSortedOrder.setAscending(value);
    }

    @Override
    public @Nullable TableCellRenderer getRenderer(PsiFieldWithSortedOrder psiFieldWithSortedOrder) {
        return rendererEditor;
    }

    @Override
    public boolean isCellEditable(PsiFieldWithSortedOrder psiFieldWithSortedOrder) {
        return true;
    }

    @Override
    public @Nullable TableCellEditor getEditor(PsiFieldWithSortedOrder psiFieldWithSortedOrder) {
        return rendererEditor;
    }
}

package org.example.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GenerateDialog extends DialogWrapper {
    private final LabeledComponent<JPanel> myComponent;
    private final ListTableModel<PsiFieldWithSortedOrder> myFields;


    public GenerateDialog(PsiClass psiClass, String dialogTitle) {
        super(psiClass.getProject());
        setTitle(dialogTitle);

        PsiField[] fields = psiClass.getAllFields();
        List<PsiFieldWithSortedOrder> sortedOrders = new ArrayList<>(fields.length);

        for (PsiField field : fields) {
            sortedOrders.add(new PsiFieldWithSortedOrder(field, true));
        }

        ColumnInfo[] columnInfos = {new SortedOrderColumnInfo("Sort order"),
                new ClassNameColumnInfo("Class name")};
        myFields = new ListTableModel<>(columnInfos, sortedOrders, 1);

        MyTable jTable = new MyTable(myFields);
        jTable.setRowMargin(2);
        jTable.setRowHeight(20);

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(jTable);
        decorator.disableAddAction();
        JPanel panel = decorator.createPanel();
        this.myComponent = LabeledComponent.create(panel, dialogTitle + " (Warning: existing method(s) will be replaced):");

        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return myComponent;
    }

    public List<PsiFieldWithSortedOrder> getMyFields() {
        return myFields.getItems();
    }
}

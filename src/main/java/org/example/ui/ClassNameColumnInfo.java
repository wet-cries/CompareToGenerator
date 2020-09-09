package org.example.ui;

import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.Nullable;

public class ClassNameColumnInfo extends ColumnInfo<PsiFieldWithSortedOrder, String> {
    public ClassNameColumnInfo(String name) {
        super(name);
    }

    @Override
    public @Nullable String valueOf(PsiFieldWithSortedOrder psiFieldWithSortedOrder) {
        return psiFieldWithSortedOrder.getPsiField().getName();
    }
}

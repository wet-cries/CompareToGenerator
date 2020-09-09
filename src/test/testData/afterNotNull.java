import org.jetbrains.annotations.NotNull;

public class NotNullClass implements Comparable<NotNullClass> {
    @NotNull String str;

    @Override
    public int compareTo(NotNullClass that) {
        if (this.str.compareTo(that.str) < 0) {
            return -1;
        } else if (this.str.compareTo(that.str) > 0) {
            return 1;
        }

        return 0;
    }
}
public class Number implements Comparable<Number> {
    int number;

    @Override
    public int compareTo(Number that) {
        if (this.number < that.number) {
            return -1;
        } else if (this.number > that.number) {
            return 1;
        }

        return 0;
    }
}
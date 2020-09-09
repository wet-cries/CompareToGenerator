public class Simple implements Comparable<Simple> {
    private String foo;

    @Override
    public int compareTo(Simple that) {
        if (this.foo != null && that.foo != null) {
            if (this.foo.compareTo(that.foo) < 0) {
                return -1;
            } else if (this.foo.compareTo(that.foo) > 0) {
                return 1;
            }
        } else if (this.foo != null) {
            return 1;
        } else if (that.foo != null) {
            return -1;
        }

        return 0;
    }
}
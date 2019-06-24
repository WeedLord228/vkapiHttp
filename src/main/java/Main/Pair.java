package Main;

public class Pair implements Comparable<Pair> {
    Integer value;
    String name;

    public Pair(String name, Integer value)
    {
        this.value = value;
        this.name = name;
    }

    public int compareTo(Pair o) {
        return this.value.compareTo(o.value);
    }
}

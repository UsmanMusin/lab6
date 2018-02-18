import java.util.HashSet;
import java.util.Set;


public class Street {
    private String name;
    private int count = 0;
    private Set<String> classRoutes = new HashSet<String>();

    Street (String name){
        this.name = name;
    }

    public void printClasses(){
        for (String s: classRoutes) {
            System.out.println(s);
        }
    }

    public String getName() {
        return name;
    }

    public void addCount(){
        this.count++;
    }

    public void addSegment(String classRoute){
        classRoutes.add(classRoute);
        addCount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Street that = (Street) o;

        if (count != that.count) return false;
        if (!name.equals(that.name)) return false;
        return classRoutes.equals(that.classRoutes);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + count;
        result = 31 * result + classRoutes.hashCode();
        return result;
    }
}

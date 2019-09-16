import java.util.ArrayList;
import java.util.List;

public class Datatest {
    public static List<String> getNeighborhood(int x, int y,int lenX,int lenY){
        ArrayList<String> neighborhoods = new ArrayList<String>();
        if ((x-1)>-1){
            neighborhoods.add((x-1)+","+y);
        }
        if ((x+1)<lenX){
            neighborhoods.add((x+1)+","+y);
        }
        if ((y+1)<lenY){
            neighborhoods.add(x+","+(y+1));
        }
        if ((y-1)>-1){
            neighborhoods.add(x+","+(y-1));
        }
        return neighborhoods;
    }
    public static boolean isBoundary(int x,int y){
        if (x == 0 || y == 0 || x == 3 || y == 3)
            return true;
        return false;
    }

    public static void main(String[] args) {
        List<String> neighborhood = getNeighborhood(0, 3,4,4);
        for (String s :neighborhood){
            System.out.println(s);
        }
    }
}

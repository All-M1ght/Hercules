import java.io.*;
import java.util.List;

public class MakeData {
    public static void main(String[] args) {
        int lenX = 4;
        int lenY = 4;
        int Ttop = 100;
        int Tbottom = 0;
        int Tleft = 0;
        int Tright = 30;
        int Tguess = 30;
        int [][] data = new int[lenX][lenY];

        for (int i = 0;i<lenX;i++){
            for (int j = 0;j<lenY;j++){
                data[i][j] = Tguess;
            }
        }


        for (int i = lenX-1;i<lenX;i++){
            for (int j = 0;j<lenY;j++){
                data[i][j] = Ttop;
            }
        }


        for (int i = 0;i<1;i++){
            for (int j = 0;j<lenY;j++){
                data[i][j] = Tbottom;
            }
        }



        for (int i = 0;i<lenX;i++){
            for (int j = lenY-1;j<lenY;j++){
                data[i][j] = Tright;
            }
        }



        for (int i = 0;i<lenX;i++){
            for (int j = 0;j<1;j++){
                data[i][j] = Tleft;
            }
        }

//        System.out.println();
        showData(data,lenX,lenY);

        File fout = new File("/Users/allmight/work/ideaworkspace/Hercules/4/input/4.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < lenX; i++) {
                for (int j = 0;j<lenY;j++){
                    try {
                        String line = i+","+j+","+data[i][j];
                        bw.write(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        bw.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }




    }
    public static void showData(int[][] data,int lenX,int lenY){
        for (int i = 0;i<lenX;i++){
            for (int j = 0;j<lenY;j++){
                System.out.print(data[i][j]+"\t");
                if (j == (lenY-1))
                    System.out.println();
            }
        }
    }
}

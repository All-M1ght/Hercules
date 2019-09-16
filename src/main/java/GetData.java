import java.io.*;

public class GetData {
    public static void main(String[] args) {
        get("/Users/allmight/work/ideaworkspace/Hercules/output/1/part-r-00000");
        System.out.println();
        get("/Users/allmight/work/ideaworkspace/Hercules/output/2/part-r-00000");

    }
    public static void get(String path){
        float[][] data = new float[4][4];
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line=null;
        try {
            while((line=br.readLine())!=null) {
                String[] splited = line.split(",");
                int x = Integer.valueOf(splited[0]);
                int y = Integer.valueOf(splited[1]);
                float state = Float.valueOf(splited[2]);
                data[x][y] = state;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();//关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0;i<4;i++){
            for (int j = 0;j<4;j++){
                if (data[i][j] == 0)
                    System.out.print(0+"\t");
                else
                    System.out.print(data[i][j]+"\t");
                if (j == (4-1))
                    System.out.println();
            }
        }
    }
}

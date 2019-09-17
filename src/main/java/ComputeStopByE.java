import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;



public class ComputeStopByE {
    public static int lenX = 4;
    public static int lenY = 4;
    public static float e = 0.1f;

    public static int maxIter = 100;

    public static class EMapper extends Mapper<Object, Text, Text, Text> {

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] splited = line.split(",");
            if (line.equals("") || line == null)
                return;

            int x = Integer.valueOf(splited[0]);
            int y = Integer.valueOf(splited[1]);
            float state = Float.valueOf(splited[2]);
            if (isBoundary(x, y, lenX, lenY)) {
                String cell = x + "," + y;
                context.write(new Text(cell), new Text(state + ""));
            }

            context.write(new Text(x + "," + y), new Text("p" + state));
            List<String> neighborhoods = getNeighborhood(x, y, lenX, lenY);
            for (String neighborhood : neighborhoods) {
                String[] neighborhoodXY = neighborhood.split(",");
                int neighborhoodX = Integer.valueOf(neighborhoodXY[0]);
                int neighborhoodY = Integer.valueOf(neighborhoodXY[1]);
                if (!isBoundary(neighborhoodX, neighborhoodY, lenX, lenY))
                    context.write(new Text(neighborhood), new Text(state + ""));
            }
        }


        public static List<String> getNeighborhood(int x, int y, int lenX, int lenY) {
            ArrayList<String> neighborhoods = new ArrayList<String>();
            if ((x - 1) > -1) {
                neighborhoods.add((x - 1) + "," + y);
            }
            if ((x + 1) < lenX) {
                neighborhoods.add((x + 1) + "," + y);
            }
            if ((y + 1) < lenY) {
                neighborhoods.add(x + "," + (y + 1));
            }
            if ((y - 1) > -1) {
                neighborhoods.add(x + "," + (y - 1));
            }
            return neighborhoods;
        }
    }

    public static class EReducer extends Reducer<Text, Text, Text, NullWritable> {

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String[] splited = key.toString().trim().split(",");
            int x = Integer.valueOf(splited[0]);
            int y = Integer.valueOf(splited[1]);
            float sum = 0;
            float post = 0;
            for (Text val : values) {
                String value = val.toString();
                if (value.contains("p")) {
                    value = value.substring(1);
                    post = Float.valueOf(value);
                } else {
                    sum += Float.valueOf(value);
                }
            }
            if (!isBoundary(x, y, lenX, lenY))
                sum /= 4;
            float loss = sum - post;
            if (loss > e || loss < -e) {
                writeFlag(x, y, loss);
            }
            String result = key.toString() + "," + sum;
            context.write(new Text(result), NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();


        String input = "/Users/allmight/work/ideaworkspace/Hercules/4/input";
//        String input = "/Users/allmight/work/ideaworkspace/Hercules/16/output/100";
        String output = "/Users/allmight/work/ideaworkspace/Hercules/4/output/";
        for (int i = 1; 1 == 1; i++) {
            Job job = Job.getInstance(conf, "heat");
            job.setJarByClass(ComputeStopByE.class);
            job.setMapperClass(EMapper.class);
            job.setReducerClass(EReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            FileInputFormat.addInputPath(job, new Path(input));
            FileOutputFormat.setOutputPath(job, new Path(output + i));
            input = output + i;
            job.waitForCompletion(true);
            if (!readFlag("/Users/allmight/work/ideaworkspace/Hercules/4/flag.txt"))
                break;
            rebuildFlag("/Users/allmight/work/ideaworkspace/Hercules/4/flag.txt");


        }
    }

    public static boolean isBoundary(int x, int y, int lenX, int lenY) {
        if (x == 0 || y == 0 || x == (lenX - 1) || y == (lenY - 1))
            return true;
        return false;
    }

    public static void writeFlag(int x, int y, float loss) {
        File fout = new File("/Users/allmight/work/ideaworkspace/Hercules/4/flag.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            try {
                String line = x + "," + y + "," + loss;
                bw.write(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
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

    public static boolean readFlag(String path) {
        BufferedReader br = null;
        int i = 0;
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();//关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i!=0?true:false;
    }
    public static void rebuildFlag(String path){
        File file =new File(path);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

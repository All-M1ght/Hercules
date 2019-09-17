import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;



public class ComputeData {
    public static int lenX = 4;
    public static int lenY = 4;
    public static int maxIter = 100;
    public static float loss = 50;
    public static int flag = 0;


    public static class HeatMapper extends Mapper<Object, Text, Text, FloatWritable> {

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
//            System.out.println(line);
            String[] splited = line.split(",");
            if (line.equals("")||line == null)
                return;
//            System.out.println(splited.length);
//            System.out.println(splited[0]);
            int x = Integer.valueOf(splited[0]);
            int y = Integer.valueOf(splited[1]);
            float state = Float.valueOf(splited[2]);
            if(isBoundary(x,y,lenX,lenY)){
                String cell = x+","+y;
                context.write(new Text(cell),new FloatWritable(state));
            }
            List<String> neighborhoods = getNeighborhood(x, y, lenX, lenY);
            for (String neighborhood:neighborhoods){
                String[] neighborhoodXY = neighborhood.split(",");
                int neighborhoodX = Integer.valueOf(neighborhoodXY[0]);
                int neighborhoodY = Integer.valueOf(neighborhoodXY[1]);
                if (!isBoundary(neighborhoodX,neighborhoodY,lenX,lenY))
                    context.write(new Text(neighborhood),new FloatWritable(state));
            }
        }


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
    }


    public static class HeatReducer extends Reducer<Text, FloatWritable, Text, NullWritable> {

        public void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
            String[] splited = key.toString().trim().split(",");
            int x = Integer.valueOf(splited[0]);
            int y = Integer.valueOf(splited[1]);
            float sum = 0;
            for (FloatWritable val : values) {
                sum += val.get();
            }
            if (!isBoundary(x,y,lenX,lenY))
                sum /= 4;
            String result = key.toString() + "," + sum;
            context.write(new Text(result), NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
//        conf.set("dfs.client.use.datanode.hostname", "true");

        String input = "/Users/allmight/work/ideaworkspace/Hercules/4/input";
//        String input = "/Users/allmight/work/ideaworkspace/Hercules/16/output/100";
        String output = "/Users/allmight/work/ideaworkspace/Hercules/4/output/";
        for (int i = 1;i<=maxIter;i++) {
            Job job = Job.getInstance(conf, "heat");
            job.setJarByClass(ComputeData.class);
            job.setMapperClass(HeatMapper.class);
//        job.setCombinerClass(HeatReducer.class);
            job.setReducerClass(HeatReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(FloatWritable.class);
            FileInputFormat.addInputPath(job, new Path(input));
            FileOutputFormat.setOutputPath(job, new Path(output + i));
//        System.exit(job.waitForCompletion(true) ? 0 : 1);
            input = output+i;
            job.waitForCompletion(true);
        }
    }
    public static boolean isBoundary(int x,int y,int lenX,int lenY){
        if (x == 0 || y == 0 || x == (lenX-1) || y == (lenY-1))
            return true;
        return false;
    }
}

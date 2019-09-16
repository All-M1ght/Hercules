
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Test {
    public static void main(String[] args) throws Exception {
        /**
         * 插曲：创建对象的方式有五种：
         * 1.构造方法(一般用这种)
         * 2.静态方法(一般用这种)
         * 3.反射
         * 4.克隆
         * 5.反序列化
         */
        //Configuration是配置对象，conf可以理解为包含了所有配置信息的一个集合，可以认为是Map
        //在执行这行代码的时候底层会加载一堆配置文件 core-default.xml;hdfs-default.xml;mapred-default.xml;yarn-default.xml
        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        //相当于通过配置文件的key获取到value的值
        conf.set("fs.defaultFS","hdfs://localhost:9000");
        /**
         * 更改操作用户有两种方式：（系统会自动识别我们的操作用户，如果我们设置，将会报错会拒绝Administrator用户（windows用户））
         * 1.直接设置运行环境中的用户名为hadoop，此方法不方便因为打成jar包执行还是需要改用户，右键Run As--Run Configurations--Arguments--VM arguments--输入-DHADOOP_USER_NAME=hadoop
         * 2.直接在代码中进行声明
         */
        //更改操作用户为hadoop
        System.setProperty("HADOOP_USER_NAME","root");
        //获取文件系统对象(目的是获取HDFS文件系统)
        FileSystem fs=FileSystem.get(conf);
        //直接输出fs对象是org.apache.hadoop.fs.LocalFileSystem@70e8f8e
        //这说明是本地文件系统对象。代码在eclipse所嵌入的jvm中执行的，jvm是安装在Windows下的，所以是windows文件系统对象
        //所以要返回来指定HDFS
        System.out.println(fs);
        //上传的API
        fs.copyFromLocalFile(new Path("/Users/allmight/work/ideaworkspace/Hercules/src/file/my.txt"), new Path("/"));
        //下载的API										不改名就不用写文件名字也行
       // fs.copyToLocalFile(new Path("/test.txt"), new Path("/Users/allmight/work/ideaworkspace/Hercules/src/file"));
        fs.close();
        /**
         * .crc 是校验文件
         * 每个块的元数据信息都只会记录合法的数据起始偏移量。
         * 如果进行了非法的数据追加，最终是能够下载正确的数据的。
         * 如果在数据的中间更改了数据，造成了采用CRC算法计算出来的校验值和最初存入HDFS的校验值不一致。HDFS就认为当前这个文件被损坏了。
         */
    }

}

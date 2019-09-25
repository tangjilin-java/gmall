package ab.tjl.gmall.manage;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageWebApplicationTests {

    /**
     * Descrption:测试fdfs图片上传
     * @Param: []
     * @Return: void
     */
    @Test
    public void contextLoads() throws IOException, MyException {
        //获取配置文件的路径 配置fdfs的全局链接地址
        String tracker =GmallManageWebApplicationTests.class.getResource("/tracker.conf").getPath();

        ClientGlobal.init(tracker);

        TrackerClient trackerClient=new TrackerClient();
        //获取一个trackerServer实例
        TrackerServer trackerServer=trackerClient.getConnection();
        //通过tracker获得一个Storage连接客户端
        StorageClient storageClient=new StorageClient(trackerServer,null);

        String orginalFilename="d://v.jpg";

        String url = "http://192.168.3.4";

        String[] upload_file = storageClient.upload_file(orginalFilename, "jpg", null);
        for (String uploadInfo:upload_file) {
               url += "/" + uploadInfo;
        }
        System.out.println(url);

    }

}

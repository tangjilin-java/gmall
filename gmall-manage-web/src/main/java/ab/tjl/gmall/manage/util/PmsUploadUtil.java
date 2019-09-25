package ab.tjl.gmall.manage.util;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author:tangjilin
 * @Description:fdfs图片上传工具类
 * @Date:Created in 16:42 2019/9/10
 * @Modified By:
 */
public class PmsUploadUtil {
    public static String uploadImage(MultipartFile multipartFile) {

        String imgUrl = "http://192.168.56.4";

        //上传图片到服务器
        //获取配置文件的路径 配置fdfs的全局链接地址
        String tracker =PmsUploadUtil.class.getResource("/tracker.conf").getPath();

        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrackerClient trackerClient=new TrackerClient();
        //获取一个trackerServer实例
        TrackerServer trackerServer= null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //通过tracker获得一个Storage连接客户端
        StorageClient storageClient=new StorageClient(trackerServer,null);

        try {

            //获取上传的二进制对象
            byte[] bytes = multipartFile.getBytes();
            //获取后缀名a.jpg
            String originalFilename = multipartFile.getOriginalFilename();
            System.out.println(originalFilename);
            int i = originalFilename.lastIndexOf(".");
            String extName = originalFilename.substring(i+1);

            String[]  uploadInfos = storageClient.upload_file(bytes, extName, null);

            for (String uploadInfo:uploadInfos) {
                imgUrl += "/" + uploadInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
}

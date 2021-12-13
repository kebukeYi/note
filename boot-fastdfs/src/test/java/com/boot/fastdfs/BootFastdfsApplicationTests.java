package com.boot.fastdfs;

import com.boot.fastdfs.config.FastDFSClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
class BootFastdfsApplicationTests {

    @Test
    public void Upload() {
        String fileUrl = this.getClass().getResource("/daqiao.png").getPath();
        File file = new File(fileUrl);
        String str = FastDFSClient.uploadFile(file);
        FastDFSClient.getResAccessUrl(str);
    }

    @Test
    public void Delete() {
        FastDFSClient.deleteFile("group1/M00/00/00/rBEAClu8OiSAFbN_AAbhXQnXzvw031.jpg");
    }
}

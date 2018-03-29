package com.csye6225.spring2018.Utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

public class PictureStoreUtility {


    public static final String pictureLocalPath = "/var/lib/tomcat8/webapps/ROOT";
    public static final String pictureApplicationPath = "/resources/images/";
    public static final String pictureApplicationAbsolutePath = "/home/huziy/csye6225/dev/webapp/src/main/resources/images/";


    public static File convertFromMultipart(MultipartFile file) throws Exception {
        File newFile = new File(pictureLocalPath + file.getOriginalFilename());
        //System.out.println(newFile);
        //Files.createTempFile(file.getOriginalFilename(),"");
        newFile.createNewFile();
        FileOutputStream fs = new FileOutputStream(newFile);
        fs.write(file.getBytes());
        fs.close();
        return newFile;
    }
}

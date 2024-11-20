package com.movieflix.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        //geting name from the file without .jpg ,png etc by using multipart methods

        String fileName = file.getOriginalFilename();

        /*getting file path where to upload basically the path we provided
        is concatinated with file name we are uploading
         */
        String filePath = path + File.separator +fileName;

        //create a file object if that file doesn't exist in our server target file
       File f = new File(path);// we created a file object with path we given
        if(!f.exists()){
            f.mkdir();
        }

       // copying the contents to the desired location
       Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {

        // getting file path from file name and giving to input stream ie to give it in bite wise

        String filePath = path + File.separator+fileName;
        return new FileInputStream(filePath);
    }
}

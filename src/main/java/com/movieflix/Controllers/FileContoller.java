package com.movieflix.Controllers;

import com.movieflix.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file/")
public class FileContoller {
    private final FileService fileService;
    // contructor injection
    public FileContoller(FileService fileService) {
        this.fileService = fileService;
    }

    //setting pile path to posters/ because we can change application properties if needed directly
    @Value("${project.poster}")
   private String path;


    // adding resources to the database by posting it we take file as input

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestPart MultipartFile file) throws IOException{
        String uploadedFileName = fileService.uploadFile(path,file);
        return ResponseEntity.ok("file uploaded: "+uploadedFileName);
    }

    //get used to display the information to the user
    @GetMapping(value="/{fileName}")
    public void  serverFileHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException {

        //getting file into resource file
        InputStream resourceFile=fileService.getResourceFile(path,fileName);

        /* we are giving output as photo which cant be done by
         datatypes so we are giving response with response type as png file*/
        response.setContentType(MediaType.IMAGE_PNG_VALUE);

        //copy to output response

        StreamUtils.copy(resourceFile,response.getOutputStream());

    }
}

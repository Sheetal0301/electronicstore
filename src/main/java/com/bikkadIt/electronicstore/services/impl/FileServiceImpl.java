package com.bikkadIt.electronicstore.services.impl;

import com.bikkadIt.electronicstore.exception.BadApiRequest;
import com.bikkadIt.electronicstore.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        logger.info("filename:{}",originalFilename);
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filenamewithextension=filename+extension;
        String fullPathFileName=path+File.separator+filenamewithextension;

       if(extension.equalsIgnoreCase(".png")||extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".jpeg"))
       {
           //file save
           File folder=new File(path);
           if(!folder.exists())
           {
               //create the folder
               folder.mkdirs();

           }
           Files.copy(file.getInputStream(), Paths.get(fullPathFileName));
           return filenamewithextension;
       }
       else
       {
           throw new BadApiRequest("File with this "+extension+" not found");
       }
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath=path+File.separator+fileName;
        InputStream is=new FileInputStream(fullPath);
        return is;
    }
}

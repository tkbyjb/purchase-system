package com.purchase.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    Boolean isFileLegal(MultipartFile file);
    String[] saveFilesToOnePath(MultipartFile[] files, String path) throws IOException;
    String saveFile(MultipartFile file,  String path) throws IOException;
}

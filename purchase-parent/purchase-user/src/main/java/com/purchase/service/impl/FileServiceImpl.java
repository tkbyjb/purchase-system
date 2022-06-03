package com.purchase.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.purchase.global.exception.UploadFileNotLegalException;
import com.purchase.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public Boolean isFileLegal(MultipartFile file) {
        //判断上传的文件是否合法没有恶意上传文件
        return true;
    }

    @Override
    public String[] saveFilesToOnePath(MultipartFile[] files, String path) throws IOException {
        //保存多个文件到一个path路径下,返回新文件名
        String[] filenames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filenames[i] = saveFile(files[i], path);
        }
        return filenames;
    }

    @Override
    public String saveFile(MultipartFile file, String path) throws IOException {
        //保存一个文件到path下,返回默认生成的新文件名,默认生成时间+uuid.后缀名作为文件名
        File folder = new File(path);
        if(!folder.exists()) {
            folder.mkdirs();
        }
        String filename = DateUtil.format(new Date(), "yyyy_MM_dd_HH") + "_" + IdUtil.simpleUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        file.transferTo(new File(path + '/' + filename));
        return filename;
    }
}

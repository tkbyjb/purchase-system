package com.purchase.global;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "purchase-default-data")
public class DefaultData {
    //默认头像链接
    public String AVATAR_URL ;
    //默认用户专属上传文件根地址 如: /a/b/用户id/申请附件/文件名.xxx  /a/b就是根目录
    public String USER_UPLOAD_BASE_PATH;
    //默认头像上传路径
    public String AVATAR_UPLOAD_PATH;
    //头像地址
    public String AVATAR_ACCESS_BASE_URL;
    //合同文件上传根地址 /.../{purchaseId}/合同文件名
    public String CONTRACT_UPLOAD_BASE_PATH;
    //附件下载根url+/appliUserId/附件名
    public String ATTACHMENT_ACCESS_BASE_URL;
    //合同下载根url+/purchaseId/合同名
    public String CONTRACT_ACCESS_BASE_URL;
}

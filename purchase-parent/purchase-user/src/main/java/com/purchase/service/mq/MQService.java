package com.purchase.service.mq;

import com.purchase.global.DefaultData;
import com.purchase.service.EmailService;
import com.purchase.vo.param.VerifyCodeEmailParam;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
public class MQService {
    @Resource
    private EmailService emailService;
    @Resource
    private DefaultData defaultData;

    //发送验证码的消息队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("routing_queue_purchase_verifyCode"),
            exchange = @Exchange(value = "routing_exchange", type = "direct"),
            key = "purchase_verifyCode_email")
    )
    public void registerEmailQueue(VerifyCodeEmailParam verifyCodeEmailParam) {
        if(verifyCodeEmailParam.getType().equals("register")){
            emailService.sendRegisterVerifyCode(verifyCodeEmailParam.getEmail(), verifyCodeEmailParam.getVerifyCode());
        }
//        else if(verifyCodeEmailParam.getType().equals("editEmail")){
//            emailService.sendEditEmailVerifyCode(verifyCodeEmailParam.getEmail(), verifyCodeEmailParam.getVerifyCode());
//        }else if(verifyCodeEmailParam.getType().equals("forgetPassword")){
//            emailService.sendForgetPasswordVerifyCode(verifyCodeEmailParam.getEmail(), verifyCodeEmailParam.getVerifyCode());
//        }
    }

    //删除没用头像文件的消息队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("routing_queue_purchase_delete_avatar"),
            exchange = @Exchange(value = "routing_exchange", type = "direct"),
            key = "purchase_delete_avatar")
    )
    public void deleteAvatarQueue(String avatar) {
        File path = new File(defaultData.AVATAR_UPLOAD_PATH);
        String[] avators = path.list();
        for (String avator : avators) {
            if(avator.indexOf(avatar) != -1) {
                File old = new File(defaultData.AVATAR_UPLOAD_PATH+"/"+avator);
                old.delete();
                break;
            }
        }
    }
}

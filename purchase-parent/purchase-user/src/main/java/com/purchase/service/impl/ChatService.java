package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.purchase.component.WebSocketServer;
import com.purchase.dao.mapper.ChatMapper;
import com.purchase.dao.mapper.SupplierMapper;
import com.purchase.dao.mapper.UserMapper;
import com.purchase.dao.po.Chat;
import com.purchase.dao.po.Supplier;
import com.purchase.dao.po.User;
import com.purchase.global.DefaultData;
import com.purchase.vo.chat.param.SendMessParam;
import com.purchase.vo.chat.vo.ChatLeftVo;
import com.purchase.vo.chat.vo.MessVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    @Resource
    private ChatMapper chatMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private SupplierMapper supplierMapper;
    @Resource
    private DefaultData defaultData;

    public List<ChatLeftVo> getChatLeftVo(Long receiveUserId) {
        List<ChatLeftVo> vos = chatMapper.getSendUserIds(receiveUserId);
        for (ChatLeftVo vo : vos) {
            User user = userMapper.selectById(vo.getUserId());
            if(user != null) {
                if (!user.getAvatar().equals(defaultData.AVATAR_URL)) {
                    vo.setAvatar(defaultData.AVATAR_ACCESS_BASE_URL + "/" + user.getAvatar());
                } else {
                    vo.setAvatar(user.getAvatar());
                }
                vo.setRealname(user.getRealname());
            }else{
                Supplier supplier = supplierMapper.selectById(vo.getUserId());
                if (!supplier.getAvatar().equals(defaultData.AVATAR_URL)) {
                    vo.setAvatar(defaultData.AVATAR_ACCESS_BASE_URL + "/" + supplier.getAvatar());
                } else {
                    vo.setAvatar(supplier.getAvatar());
                }
                vo.setRealname(supplier.getSuppliername());
            }
            if(WebSocketServer.sessionMap.contains(vo.getUserId()+"")) {
                vo.setIsOnline(1);
            }else{
                vo.setIsOnline(0);
            }
        }
        return vos;
    }

    public List<MessVo> getMesss(Long sendUserId, Long receiveUserId) {
        List<MessVo> vos = chatMapper.getMesss(sendUserId, receiveUserId);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("sendUserId", sendUserId);
        updateWrapper.eq("receiveUserId", receiveUserId);
        updateWrapper.set("isRead", 1);
        chatMapper.update(null, updateWrapper);
        return vos;
    }

    public Boolean addMess(SendMessParam param) {
        Chat chat = new Chat();
        chat.setCreateTime(LocalDateTime.now());
        chat.setContent(param.getMess());
        chat.setSendUserId(param.getSendUserId());
        chat.setReceiveUserId(param.getReceiveUserId());
        chat.setState(1);
        chat.setIsDelete(0);
        chat.setIsRead(0);
        chatMapper.insert(chat);
        return true;
    }
}

package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.purchase.dao.po.Chat;
import com.purchase.vo.chat.vo.ChatLeftVo;
import com.purchase.vo.chat.vo.MessVo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
    List<ChatLeftVo> getSendUserIds(Long receiveUserId);
    List<MessVo> getMesss(Long sendUserId, Long receiveUserId);
}

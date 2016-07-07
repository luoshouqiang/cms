package mcarport.business.cms.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import mcarport.business.cms.dto.MessageDTO;
import mcarport.business.cms.exception.BusinessException;
import mcarport.business.cms.push.PushMessageService;
import mcarport.business.cms.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class PushMsgService {
	
	@Autowired
	PushMessageService baiduPushService;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Transactional
	public  void pushMsg(String title,String content){
		if(StringUtils.isEmpty(title)
				||StringUtils.isEmpty(content)){
			throw new BusinessException("请先填写标题和内容");
		}
		
		MessageDTO msgDTO=new MessageDTO();
		msgDTO.setTitle(title);
		msgDTO.setDetail(content);
		Date startTime=new Date();
		try {
			
			baiduPushService.pushBroadcastMessage(msgDTO, true);
			recordMsg(startTime,new Date(),msgDTO,true);
		} catch (Exception e) {
			e.printStackTrace();
			recordMsg(startTime,new Date(),msgDTO,false);
			throw new BusinessException("消息推送失败"+e.getMessage());
		} 
	}
	
	
	private void recordMsg(Date startTime,Date endTime,final MessageDTO dto,boolean status){
		String userSql="select user_id from base_user";
		final List<Long> allUserList=jdbcTemplate.queryForList(userSql, Long.class);
		String insertMsgSql="insert into base_messages(created_time,user_id,message_time,message_type,content,message_status)"
				+ " values(?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(insertMsgSql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				long userId=allUserList.get(i);
				ps.setDate(1, new java.sql.Date(System.currentTimeMillis()));
				ps.setLong(2, userId);
				ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
				ps.setString(4, "person");
				ps.setString(5, Utils.objectToJson(dto));
				ps.setInt(6, 1);
			}
			
			@Override
			public int getBatchSize() {
				return allUserList.size();
			}
		});
		String insertSql="insert into base_system_mass_messages(start_time,end_time,message_title,content,mass_status)"
				+ " values(?,?,?,?,?)";
		jdbcTemplate.update(insertSql, startTime,endTime,dto.getTitle(),dto.getDetail(),status);
		
	}
}

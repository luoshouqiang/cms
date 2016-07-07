package mcarport.business.cms.push;

import java.util.List;

import mcarport.business.cms.dto.MessageDTO;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;

/**
 * 推送消息接口
 * @author luosq
 *
 */
public interface PushMessageService {

	/**
	 * 设置一组账户的标签
	 * @param tag
	 * @param accountIds
	 * @throws ChannelClientException
	 * @throws ChannelServerException
	 */
	public abstract void setTag(String tag, List<Long> accountIds) throws ChannelClientException,
			ChannelServerException;

	/**
	 * 发布标签信息
	 * @param tag
	 * @param msg
	 * @param isNotify
	 * @throws ChannelClientException
	 * @throws ChannelServerException
	 */
	public abstract void pushTagMessage(String tag, String msg, boolean isNotify) throws ChannelClientException,
			ChannelServerException;

	/**
	 * 发送单点信息
	 * @param accountId
	 * @param msg
	 * @param isNotify
	 * @throws ChannelClientException
	 * @throws ChannelServerException
	 */
	public abstract void pushUnicastMessage(PushClientUser pushUser, String msg, boolean isNotify)
			throws ChannelClientException, ChannelServerException;
	
	
	/**
	 * 点对点推送系统消息
	 * @param accountId
	 * @param messageDto
	 * @param isNotify
	 * @throws ChannelClientException
	 * @throws ChannelServerException
	 */
	public abstract void pushUnicastMessage(long accountId, MessageDTO messageDto)
			throws ChannelClientException, ChannelServerException;
	
	
	/**
	 * 异步点对点推送消息
	 * @param accountId
	 * @param messageDto
	 * @throws ChannelClientException
	 * @throws ChannelServerException
	 */
	public abstract void asyncPushUnicastMessage(long accountId, MessageDTO messageDto);
	
	
	/**
	 * 全局广播信息
	 * @param msg
	 * @param isNotify
	 * @throws ChannelClientException
	 * @throws ChannelServerException
	 */
	public abstract void pushBroadcastMessage(MessageDTO messageDTO,boolean isNotify) throws ChannelClientException,
			ChannelServerException;
	
	/**
	 * 绑定推送消息用户
	 * @param channelId
	 * @param userId
	 * @param sysUserId
	 * @param deviceType
	 */
	public void bindUser(long channelId,String userId,long sysUserId,int deviceType);

}
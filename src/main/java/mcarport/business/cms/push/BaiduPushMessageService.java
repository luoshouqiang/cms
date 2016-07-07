package mcarport.business.cms.push;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import mcarport.business.cms.dto.MessageDTO;
import mcarport.business.cms.exception.BusinessException;
import mcarport.business.cms.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.SetTagRequest;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

/**
 * 百度处理推送消息
 * 
 * @author luosq
 *
 */
 @Component
public class BaiduPushMessageService implements PushMessageService {
	 
	 private static final Logger LOG=LoggerFactory.getLogger(PushMessageService.class);

	/** 存放所有的用户的信息 */
	private static ConcurrentHashMap<Long, PushClientUser> allClientPushUser = new ConcurrentHashMap<Long, PushClientUser>();

	private static  String ANDROID_APIKEY;
	private static  String ANDROID_SECRETKEY;

	private static  String IOS_APIKEY;
	private static  String IOS_SECRETKEY;
	
	private int iosDeployStatus=1;
	
	private BaiduChannelClient andriodChannelClient;
	
	private BaiduChannelClient iosChannelClient;
	
	
	public static final int ANDROID_DEVICE_TYPE=3;
	public static final int IOS_DEVICE_TYPE=4;
	
	Configuration config;
	
	ExecutorService executorService = Executors.newFixedThreadPool(5);

	@PostConstruct
	public void init() {
		ANDROID_APIKEY=Configuration.getProperty("baidu.andriod.apikey");
		ANDROID_SECRETKEY=Configuration.getProperty("baidu.andriod.secretkey");
		
		IOS_APIKEY=Configuration.getProperty("baidu.ios.apikey");
		IOS_SECRETKEY=Configuration.getProperty("baidu.ios.secretkey");
//		iosDeployStatus=Integer.parseInt(Configuration.getProperty("baidu.ios.deploystatus"));
		andriodChannelClient = getAndriodClient();
		iosChannelClient = getIOSClient();
	}

	/* (non-Javadoc)
	 * @see com.mcarport.business.push.PushMessageIn#setTag(java.lang.String, java.util.List)
	 */
	@Override
	public void setTag(String tag, List<Long> accountIds) throws ChannelClientException, ChannelServerException {
		if (StringUtils.isEmpty(tag)) {
			throw new BusinessException( "请先设置标签");
		}
		if (CollectionUtils.isEmpty(accountIds)) {
			throw new BusinessException("请先选取用户");
		}
		if (CollectionUtils.isEmpty(allClientPushUser)) {
			throw new BusinessException("没有一个推送客户端注册");
		}

		for (Long sysUserId : accountIds) {
			if (sysUserId != null && sysUserId != 0) {
				PushClientUser pushUser = allClientPushUser.get(sysUserId);
				if (pushUser != null) {
					SetTagRequest setTagRequest = new SetTagRequest();
					setTagRequest.setTag(tag);
					setTagRequest.setUserId(pushUser.getUserId());
					getPushClient(pushUser.getDeviceType()).setTag(setTagRequest);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.mcarport.business.push.PushMessageIn#pushTagMessage(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void pushTagMessage(String tag, String msg, boolean isNotify) throws ChannelClientException,
			ChannelServerException {
		PushTagMessageRequest pushTagMessageRequest =MessageRequestFactory.createTagRequest(tag, ANDROID_DEVICE_TYPE, isNotify,iosDeployStatus, msg);
		getAndriodClient().pushTagMessage(pushTagMessageRequest);

		pushTagMessageRequest.setDeviceType(IOS_DEVICE_TYPE);
		getIOSClient().pushTagMessage(pushTagMessageRequest);
	}

	/* (non-Javadoc)
	 * @see com.mcarport.business.push.PushMessageIn#pushUnicastMessage(long, java.lang.String, boolean)
	 */
	@Override
	public void pushUnicastMessage(PushClientUser pushUser, String msg, boolean isNotify) throws ChannelClientException,
			ChannelServerException {		
			PushUnicastMessageRequest request =MessageRequestFactory.createUnicastMessage(pushUser,isNotify,iosDeployStatus,msg);
			getPushClient(pushUser.getDeviceType()).pushUnicastMessage(request);
		
	}

	/* (non-Javadoc)
	 * @see com.mcarport.business.push.PushMessageIn#pushBroadcastMessage(java.lang.String, boolean)
	 */
	@Override
	public void pushBroadcastMessage(MessageDTO msgDTO,boolean isNotify) throws ChannelClientException,
			ChannelServerException {		
		String msgString=convertMessageToPush(msgDTO,ANDROID_DEVICE_TYPE);
		PushBroadcastMessageRequest andriodRequest =MessageRequestFactory.createBroadcastMsg(ANDROID_DEVICE_TYPE,isNotify,iosDeployStatus,msgString);
		andriodChannelClient.pushBroadcastMessage(andriodRequest);
	/*	
		PushBroadcastMessageRequest iosRequest =MessageRequestFactory.createBroadcastMsg(IOS_DEVICE_TYPE,isNotify,iosDeployStatus,msgString);
		iosChannelClient.pushBroadcastMessage(iosRequest);
		*/
	}
	

	@Override
	public void pushUnicastMessage(long sysUserId, MessageDTO messageDto)
			throws ChannelClientException, ChannelServerException {
		PushClientUser pushUser = allClientPushUser.get(sysUserId);
		if (pushUser != null) {
			boolean isNotify=MessageDTO.NOTICE.equals(messageDto.getMessageType())?true:false;
			String pushMsg=convertMessageToPush(messageDto,pushUser.getDeviceType());
			pushUnicastMessage(pushUser, pushMsg, isNotify);
		}else{
			LOG.warn("没有找到推送的用户信息,userId:"+sysUserId+",消息:"+messageDto);
		}
		
	}
	
	private BaiduChannelClient getPushClient(final int deviceType) {
		switch (deviceType) {
		case ANDROID_DEVICE_TYPE:
			return andriodChannelClient;
		case IOS_DEVICE_TYPE:
			return iosChannelClient;
		default:
			return andriodChannelClient;
		}
	}

	private BaiduChannelClient getAndriodClient() {
		return createChannelClient(ANDROID_APIKEY, ANDROID_SECRETKEY);
	}

	private BaiduChannelClient getIOSClient() {
		return createChannelClient(IOS_APIKEY, IOS_SECRETKEY);
	}

	private BaiduChannelClient createChannelClient(String apiKey, String secretkey) {
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretkey);
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				LOG.info(event.getMessage());
			}
		});
		return channelClient;
	}

	

	private String convertMessageToPush(MessageDTO messageDto,int deviceType){
		assert(messageDto!=null);
		if(deviceType==ANDROID_DEVICE_TYPE){
			BaiduAndroidPushMessage andriodMessage=new BaiduAndroidPushMessage();
			andriodMessage.setTitle(messageDto.getTitle());
			andriodMessage.setDescription(messageDto.getDetail());
			andriodMessage.setCustom_content(Utils.objectToJson(messageDto));
			return Utils.objectToJson(andriodMessage);
		}
		if(deviceType==IOS_DEVICE_TYPE){
			BaiduIosPushMessage isoMessage=new BaiduIosPushMessage();
			BaiduIosPushMessage.APS aps= isoMessage.new APS();
			aps.setAlert(messageDto.getTitle());
			aps.setSound("sms-received1.caf");
			isoMessage.setAps(aps);
			isoMessage.setMsgJson(String.valueOf(messageDto.getId()));
			return Utils.objectToJson(isoMessage);
		}
		return "";
	}

	@Override
	public void bindUser(long channelId, String userId, long sysUserId, int deviceType) {
		PushClientUser pushUser=new PushClientUser();
		pushUser.setSysUserId(sysUserId);
		pushUser.setChannelId(channelId);
		pushUser.setDeviceType(deviceType);
		pushUser.setUserId(userId);
		allClientPushUser.put(sysUserId, pushUser);
	}
	
	public static void main(String[] args) {
	}

	@Override
	public void asyncPushUnicastMessage(final long sysUserId, final MessageDTO msg){
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					pushUnicastMessage(sysUserId, msg);
				} catch (Exception e) {
					LOG.error("推送消息失败" + msg, e);
				}
			}
		});
		
	}
}

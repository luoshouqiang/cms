package mcarport.business.cms.push;

import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;

/**
 * 百度消息静态生成类
 * @author luosq
 *
 */
public class MessageRequestFactory {
	
	/**
	 * 标签消息
	 * @param tag
	 * @param deviceType
	 * @param isNotify
	 * @param iosDeployStatus
	 * @param msg
	 * @return
	 */
	public static PushTagMessageRequest createTagRequest(String tag, int deviceType, boolean isNotify,int iosDeployStatus, String msg){
		int msgType = 0;
		if (isNotify) {
			msgType = 1;
		}
		PushTagMessageRequest pushTagMessageRequest = new PushTagMessageRequest();
		pushTagMessageRequest.setTagName(tag);
		pushTagMessageRequest.setMessageType(msgType);
		pushTagMessageRequest.setMessage(msg);
		pushTagMessageRequest.setDeviceType(deviceType);
		pushTagMessageRequest.setDeployStatus(iosDeployStatus);
		return pushTagMessageRequest;
	};
	
	/**
	 * 单点消息
	 * @param pushUser
	 * @param isNotify
	 * @param iosDeployStatus
	 * @param msg
	 * @return
	 */
	public static PushUnicastMessageRequest createUnicastMessage(PushClientUser pushUser,boolean isNotify, int iosDeployStatus,String msg) {
		int msgType = 0;
		if (isNotify) {
			msgType = 1;
		}

		PushUnicastMessageRequest request = new PushUnicastMessageRequest();
		request.setUserId(pushUser.getUserId());
		request.setChannelId(pushUser.getChannelId());
		request.setMessageType(msgType);
		request.setMessage(msg);
		request.setDeployStatus(iosDeployStatus);
		request.setDeviceType(pushUser.getDeviceType());
		return request;
	}
	
	/**
	 * 广播消息
	 * @param deviceType
	 * @param isNotify
	 * @param iosDeployStatus
	 * @param msg
	 * @return
	 */
	public static PushBroadcastMessageRequest createBroadcastMsg(int deviceType, boolean isNotify,int iosDeployStatus,String msg) {
		int msgType = 0;
		if (isNotify) {
			msgType = 1;
		}
		PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
		request.setMessageType(msgType);
		request.setMessage(msg);
		request.setDeviceType(deviceType);
		request.setDeployStatus(iosDeployStatus);
		return request;
	}
}

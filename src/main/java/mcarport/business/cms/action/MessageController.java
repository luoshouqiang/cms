package mcarport.business.cms.action;

import mcarport.business.cms.dto.ReturnData;
import mcarport.business.cms.service.PushMsgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("msg")
public class MessageController {
	@Autowired
	PushMsgService msgService;

	@RequestMapping("pushAll")
	public ReturnData<Void> pushMsgToAll(@RequestParam String title, @RequestParam String content) {
		ReturnData<Void> data = new ReturnData<Void>();
		try {
			msgService.pushMsg(title, content);
		} catch (Exception ex) {
			data.setCode(201);
			data.setMsg(ex.getMessage());
		}
		return data;
	}
}

package org.wechatapps.controllers;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wechatapps.service.WechatProcessor;
import org.wechatapps.pojo.Message;
import org.wechatapps.utils.SHA1;
import org.wechatapps.utils.WechatUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/*
 * @Description Interact with Wechat Open Platform
 * @author Charles Chen
 * @date 14-2-17
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/wechat")
public class RequestHandler {
	@Autowired
	private WechatProcessor wechatProcessor; // Wechat processor

	/**
	 * Wechat message handler
	 * 
	 * @return
	 */
	@RequestMapping(value = "/handler", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String handler(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Message message;

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = out = response.getWriter();

		try {
			message = WechatUtils.retrieveMessage(request); // Read message and
			// build message
			if (message == null) {
				return null;
			}
			// Process the message and respond
			String output = wechatProcessor.process(message);
			out.print(output);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Verify messages from Wechat
	 */
	@RequestMapping(value = "/verify", method = { RequestMethod.GET,
			RequestMethod.POST })
	private String verify(PrintWriter out, @RequestParam(value = "signature")
	String signature, @RequestParam(value = "timestamp")
	String timestamp, @RequestParam(value = "nonce")
	String nonce, @RequestParam(value = "echostr")
	String echostr) throws IOException {
		String[] params = new String[] { WechatUtils.getVerifyToken(),
				timestamp, nonce };
		Arrays.sort(params); // Dictionary sort

		// Encrypt with SHA1
		String encrypted = new SHA1().getDigestOfString(
				(params[0] + params[1] + params[2]).getBytes()).toLowerCase();
		if (StringUtils.equals(signature, encrypted)) {
			return echostr;
		}
		return null;
	}
}

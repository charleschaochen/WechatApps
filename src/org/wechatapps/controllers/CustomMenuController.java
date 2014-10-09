package org.wechatapps.controllers;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wechatapps.pojo.menu.ActionButton;
import org.wechatapps.pojo.menu.Button;
import org.wechatapps.pojo.menu.ComplexButton;
import org.wechatapps.pojo.menu.Menu;
import org.wechatapps.pojo.menu.ViewButton;
import org.wechatapps.utils.StreamUtils;
import org.wechatapps.utils.WechatUtils;

/**
 * Send custom menu request
 * 
 * @author Charles Chen
 * 
 */
@Controller
@RequestMapping(value = "/menu")
public class CustomMenuController {
	/**
	 * Send custom menu request
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/build_menu", method = { RequestMethod.GET }, produces = "text/html")
	public String buildMenu() {
		StringBuffer bufferRes = new StringBuffer();

		try {

			URL realUrl = new URL(
					"https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="
							+ WechatUtils.getAccessToken());
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			/* Start: Set connection properties */
			conn.setConnectTimeout(25000); // Set connection time out
			conn.setReadTimeout(25000); // Set read time out
			HttpURLConnection.setFollowRedirects(true);
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn
					.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0");
			conn.setRequestProperty("Referer", "https://api.weixin.qq.com/");
			/* End: Set connection properties */
			conn.connect(); // Connect to the server

			// Get output stream to write json data
			OutputStreamWriter out = new OutputStreamWriter(conn
					.getOutputStream());
			out.write(buildMenuXML());
			out.flush();
			out.close();

			// Read response text
			InputStream in = conn.getInputStream();
			String responseText = StreamUtils.inputStreamToString(in, "UTF-8");
			in.close();

			if (conn != null) {
				conn.disconnect();
			}

			return responseText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Build wechat customized menu
	 * 
	 * @return JSON data
	 */
	public String buildMenuXML() {
		ActionButton button_1 = new ActionButton("今日歌曲", "V1001_TODAY_MUSIC"); // Create
		// button
		// 1
		ActionButton button_2 = new ActionButton("歌手简介", "V1001_TODAY_SINGER"); // Create
		// Button
		// 2
		/* Start: Sub menu for button_3 */
		ViewButton button_3_1 = new ViewButton("搜索", "http://www.soso.com/");
		ViewButton button_3_2 = new ViewButton("视频", "http://v.qq.com/");
		ActionButton button_3_3 = new ActionButton("赞一下我们", "V1001_GOOD");
		/* End: Sub menu for button_3 */
		ComplexButton button_3 = new ComplexButton("菜单", new Button[] {
				button_3_1, button_3_2, button_3_3 }); // Create button 3

		Menu menu = new Menu(new Button[] { button_1, button_2, button_3 }); // Create
		// menu
		return JSONObject.fromObject(menu).toString();
	}
}

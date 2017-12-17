package com.doubanspider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GrabInfoThread implements Runnable{
	private String bookUrl;
	private Map<String, String> cookies;
	private SaveInfo saveBookInfo;
	public GrabInfoThread(String bookUrl,SaveInfo saveBookInfo,Map<String, String> cookies){
		this.bookUrl = bookUrl;
		this.cookies = cookies;
		this.saveBookInfo = saveBookInfo;
	}
	
	@Override
	public void run() {
		try {
			GrabInfoThread.getBookInfo(bookUrl, saveBookInfo, cookies);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**

	 * 抓取每本书的信息

	 * 

	 * @param ArrayList<String>
	 * @throws IOException 

	 */

	public static void getBookInfo(String bookUrl,SaveInfo saveInfo,Map<String, String> cookies) throws IOException {

		Document doc = Jsoup.connect(bookUrl)

				.header("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)").cookies(cookies)

				.timeout(3000).get();

		Elements titleElement = doc.getElementsByClass("subject clearfix").select("a");

		Elements scoreElement = doc.select("strong");

		Elements ratingSum = doc.getElementsByClass("rating_sum").select("a").select("span");

		Elements authorElement = doc.getElementById("info").select("span").first().select("a");

		Element pressElement = doc.getElementById("info");

		// 书名

		String title = titleElement.attr("title");

		// 评分

		String score = scoreElement.html();

		// 评价人数

		String rating_sum = ratingSum.html();

		// 作者

		String author = authorElement.html();

		// 出版社

		String press = pressElement.text();

		if (press.indexOf("出版社:") > -1) {

			press = pressElement.text().split("出版社:")[1].split(" ")[1];

		} else {

			press = "";

		}

		// 出版日期

		String date = pressElement.text();

		if (date.indexOf("出版年:") > -1) {

			date = pressElement.text().split("出版年:")[1].split(" ")[1];

		} else {

			date = "";

		}

		// 价格

		String price = pressElement.text();

		if (price.indexOf("定价:") > -1) {

			price = pressElement.text().split("定价:")[1].split(" ")[1];

			if (price.equals("CNY")) {

				price = pressElement.text().split("定价:")[1].split(" ")[2];

			}

		} else {

			price = "";

		}

		System.out.println(title);
		// 评价人数大于1000插入数据到数据库

		if (!rating_sum.equals("") && Integer.parseInt(rating_sum) >= 1000) {
			String sql = "insert into books values ('%s','%s','%s','%s','%s','%s','%s')";
			sql=String.format(sql,title,score,rating_sum,author,press,date,price);

			saveInfo.saveBookInfo(sql);

		}

		// 通过睡眠防止ip被封

		try {

			System.out.println("睡眠1秒");

			Thread.currentThread().sleep(1000);

		} catch (InterruptedException e) {

			e.printStackTrace();

		}

	}
	
	
	
	
}

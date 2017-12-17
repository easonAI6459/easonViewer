package com.doubanspider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GrabPageInfo {
	
	public static ExecutorService executorService = Executors.newFixedThreadPool(1);

	public static void main(String[] args) {
		try{
			ArrayList<String> bookUrls = new ArrayList<>();

			//分别抓取：互联网、编程、算法
			bookUrls = downloadBookUrl("算法");
			GrabPageInfo.getBookInfo(bookUrls);

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	/**

	 * 抓取每本书的信息

	 * @param ArrayList<String>

	 */

	public static void getBookInfo(ArrayList<String> bookUrls) {

		SaveInfo saveBookInfo = new SaveInfo();

		Map<String, String> cookies = new HashMap<>();

		CookieSet.setCookies(cookies);


			for (String url : bookUrls) {
	
				GrabInfoThread gt = new GrabInfoThread(url,saveBookInfo,cookies);
				executorService.execute(gt);
	
			}

		}

	/**

	 * 保存书的url地址

	 * 

	 * @param keyWord

	 * @return

	 */

	public static ArrayList<String> downloadBookUrl(String keyWord) {

		ArrayList<String> bookUrls = new ArrayList<>();

		int index = 0;

		try {

			Map<String, String> cookies = new HashMap<>();

			CookieSet.setCookies(cookies);

			while (true) {

				String a = "https://book.douban.com/tag/" + keyWord + "?start=" + index + "&type=T";
				Document doc = Jsoup.connect(a)

						.header("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)").cookies(cookies)

						.timeout(13000).get();
		
				Elements newsHeadlines = doc.select("ul").select("h2").select("a");

				for (Element e : newsHeadlines) {

					System.out.println(e.attr("href"));

					bookUrls.add(e.attr("href"));

				}

				index += newsHeadlines.size();

				System.out.println("共抓取url个数：" + index);

				if (newsHeadlines.size() == 0) {

					System.out.println("end");

					break;

				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return bookUrls;

	}
	
	
}






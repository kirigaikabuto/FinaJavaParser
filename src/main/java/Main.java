import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.*;

public class Main {
	public static void main(String[] args) throws IOException {
		int start = 1;
		int end = 1;
		ParseWebSite(start, end, "organizations");
	}

	public static void ParseWebSite(int start, int end, String jsonFileName) throws IOException {
		end = end * 100;
		ArrayList<String> headers = new ArrayList<>();
		ArrayList<LinkedHashMap<String, String>> allData = new ArrayList<>();
		String url = "https://www.finreg.kz/index.cfm?docid=3227&switch=russian&organization=&organizationid=&organizationtypeid=&typeid=&kindid=&startdate=&enddate=&npatypeid=&sourceid=&view=list&start=" + start;
		Document documents = Jsoup.connect(url).get();
		Elements ths = documents.select("th");
		for (Element th : ths) {
			headers.add(th.text());
		}
		for (int i = 1; i <= end; i += 100) {
			url = "https://www.finreg.kz/index.cfm?docid=3227&switch=russian&organization=&organizationid=&organizationtypeid=&typeid=&kindid=&startdate=&enddate=&npatypeid=&sourceid=&view=list&start=" + i;
			Element document = Jsoup.connect(url).get();
			Element table = document.getElementsByClass("dataTable hover").first();
			Element tbody = table.selectFirst("tbody");
			Elements trs = tbody.select("tr");
			for (Element tr : trs) {
				LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
				Elements tds = tr.select("td");
				for (int j = 0; j < headers.size(); j++) {
					System.out.println(headers.get(j));
					data.put(headers.get(j), tds.get(j).text());
				}
				allData.add(data);
			}
		}
		Writer writer = new FileWriter(jsonFileName + ".json");
		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();

		gson.toJson(allData, writer);
		writer.close();
	}
}

// package com.woodpecker.util;

// import org.elasticsearch.client.RestClient;
// import org.elasticsearch.client.RestHighLevelClient;
// import org.apache.http.HttpHost; 
// import org.elasticsearch.action.search.SearchRequest;
// import org.elasticsearch.search.builder.SearchSourceBuilder;
// import org.elasticsearch.action.search.SearchResponse;
// import org.elasticsearch.index.query.QueryBuilders;
// import org.elasticsearch.search.SearchHit;
// import org.elasticsearch.search.sort.SortOrder;

// import java.util.*;
// import com.fasterxml.jackson.databind.ObjectMapper;  
// import org.json.JSONObject;

// import java.io.File;
// import java.io.PrintWriter;
// import java.io.FileOutputStream;
// import java.io.FileWriter;

// public class EsExtract {
//     public static List<String> esSearch(String host, String stringPort, int beginIndex, int count, String keyword, boolean isMonitor) {
//         ObjectMapper mapper = new ObjectMapper();   //map to json
//         List<String> result = new ArrayList<>();
//         List<String> type = new LinkedList<>();
//         // type.add("DISCUZ");
//         // type.add("okkaoyanluntan");
//         // type.add("chinakaoyanluntan");
//         // type.add("kaidi");
//         // type.add("tianqinluntan");
//         // type.add("tianya");
//         type.add("baidutiebaquanbasousuo");
//         // type.add("DISCUZ");
//         // type.add("DISCUZ");
//         int port = Integer.parseInt(stringPort);
//         try {
//             RestClient restClient = RestClient.builder(
//                 new HttpHost(host, port, "http")).build();
//             RestHighLevelClient client =
//                 new RestHighLevelClient(restClient);
//             SearchRequest searchRequest = new SearchRequest("crawler"); 
//             searchRequest.types(type.toArray(new String[type.size()]));
//             SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//             sourceBuilder.query(QueryBuilders.matchPhraseQuery("content", keyword));
//             sourceBuilder.from(beginIndex);
//             sourceBuilder.size(count);
//             sourceBuilder.sort("time.keyword", SortOrder.DESC);
//             searchRequest.source(sourceBuilder);
//             SearchResponse response = client.search(searchRequest);
//             for(SearchHit hit: response.getHits().getHits()) {
//                 Map<String, Object> source= hit.getSource();
//                 JSONObject tmpJson = new JSONObject(mapper.writeValueAsString(source));
//                 // tmpJson.put("_id", hit.getId());
//                 // tmpJson.put("keyword", keyword);
//                 // if (isMonitor) {  // 监控页面需要这个字段
//                 //     tmpJson.put("contentType", "weibo");
//                 // }
//                 //System.out.println((String)tmpJson.get("content"));
//                 result.add((String)tmpJson.get("content"));
//             }
//             restClient.close();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return result;
//     }


//     public static void main(String [] args) throws Exception{
//         List<String> result = esSearch("114.212.189.147", "10142", 0, 1200, "高考", false);
//         //System.out.println(result.size());
//         File file = new File("esExtract.txt");  
//         FileWriter fw = new FileWriter(file, true);
//         PrintWriter pw = new PrintWriter(fw);
//         for (String s: result) {
//             pw.println(s);// 在已有的基础上添加字符串  
//             pw.println(0);
//         }
//         pw.flush();
//         fw.flush();
//         pw.close();
//         fw.close();  
//     }
// }
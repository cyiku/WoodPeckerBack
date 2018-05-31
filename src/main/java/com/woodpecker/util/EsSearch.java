package com.woodpecker.util;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.apache.http.HttpHost; 
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;  
import org.json.JSONObject;

public class EsSearch {
    public static List<JSONObject> esSearch(String host, String stringPort, int beginIndex, int count, String keyword, List<String> type, boolean isMonitor) {
        ObjectMapper mapper = new ObjectMapper();   //map to json
        List<JSONObject> result = new ArrayList<>();
        int port = Integer.parseInt(stringPort);
        try {
            RestClient restClient = RestClient.builder(
                new HttpHost(host, port, "http")).build();
            RestHighLevelClient client =
                new RestHighLevelClient(restClient);
            SearchRequest searchRequest = new SearchRequest("crawler"); 
            searchRequest.types(type.toArray(new String[type.size()]));
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchPhraseQuery("content", keyword));
            sourceBuilder.from(beginIndex);
            sourceBuilder.size(count);
            sourceBuilder.sort("time.keyword", SortOrder.DESC);
            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest);
            for(SearchHit hit: response.getHits().getHits()) {
                Map<String, Object> source= hit.getSource();
                JSONObject tmpJson = new JSONObject(mapper.writeValueAsString(source));
                tmpJson.put("_id", hit.getId());
                tmpJson.put("keyword", keyword);
                if (isMonitor) {  // 监控页面需要这个字段
                    tmpJson.put("contentType", "portal");
                }
                result.add(tmpJson);
            }
            restClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
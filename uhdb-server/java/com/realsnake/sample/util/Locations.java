package com.realsnake.sample.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by doring on 15. 5. 1..
 */
public class Locations {

    private final static Logger LOGGER = LoggerFactory.getLogger(Locations.class);

    public static class LatLng {

        public LatLng() {
            //
        }

        public LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double lat;
        public double lng;

    }

    /**
     * 주소로 위경도 좌표 가져오기
     *
     * @param addressOld
     * @return
     */
    public static LatLng getLocationFromAddress(String addressOld) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // address 인코딩 하면 안됨. rest template 이 함.
            String resp = restTemplate.getForObject("http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address=" + addressOld, String.class);
            LOGGER.debug("<<위경도좌표찾기, 주소>> {}", addressOld);
            LOGGER.debug("<<위경도좌표찾기, 구글맵 조회 결과>> {}", resp);

            if (resp == null || resp.isEmpty()) {
                LOGGER.debug("<<응답값이 없음>>");
                return null;
            }

            JsonNode rootNode = objectMapper.readTree(resp);
            JsonNode nodeStatus = rootNode.get("status");
            // LOGGER.debug("status : {}", nodeStatus.asText());

            if (nodeStatus.asText() == null || !"OK".equalsIgnoreCase(nodeStatus.asText())) {
                LOGGER.debug("<<status>> {}", nodeStatus.asText());
                return null;
            }

            JsonNode resultsNode = objectMapper.readTree(rootNode.get("results").toString());
            // LOGGER.debug(resultsNode.toString());

            JsonNode locationNode = resultsNode.get(0).get("geometry").get("location");
            // LOGGER.debug(locationNode.toString());

            LatLng latLng = objectMapper.readValue(locationNode.toString(), LatLng.class);
            // LOGGER.debug("lng : {}, lat : {}", latLng.lng, latLng.lat);

            return latLng;
        } catch (Exception e) {
            LOGGER.error("<<JSON 데이타에서 위경도 좌표를 가져오는 중 오류 발생>> {}", e.getMessage());
        }

        return null;
    }

    // public static void main(String[] args) {
    // Locations.getLocationFromAddress("서울시 동작구 상도동 535");
    // }

}

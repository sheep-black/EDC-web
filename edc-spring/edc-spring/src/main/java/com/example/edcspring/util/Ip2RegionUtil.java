package com.example.edcspring.util;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;    //使用jakarta包
import java.io.IOException;

@Component
public class Ip2RegionUtil {
    private static final String DB_PATH = "src/main/resources/ip2region/ip2region.xdb";
    private Searcher searcher;
    private byte[] cBuff;

    @PostConstruct
    public void init() {
        try {
            this.cBuff = Searcher.loadContentFromFile(DB_PATH);
            this.searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            System.out.println("Failed to create searcher: " + e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (searcher != null) {
            try {
                searcher.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getRegionFromIp(String ip) {
        try {
            if (searcher == null) {
                return "Unknown region (searcher not initialized)";
            }
            return searcher.search(ip);
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown region (search failed)";
        }
    }
}

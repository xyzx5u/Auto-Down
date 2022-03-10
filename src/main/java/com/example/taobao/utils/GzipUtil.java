package com.example.taobao.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @创建人: lzh
 * @创建时间: 2022/3/3
 * @描述:
 */
public class GzipUtil {

    public static byte[] compress(String str) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
                gzip.write(str.getBytes(StandardCharsets.UTF_8));
            }
            return out.toByteArray();
        }
    }

    public static String uncompress(byte[] str) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str))) {
            int b;
            while ((b = gis.read()) != -1) {
                baos.write((byte) b);
            }
        }
        //StandardCharsets.UTF_8
        return new String(baos.toByteArray(), "GBK");
    }


}

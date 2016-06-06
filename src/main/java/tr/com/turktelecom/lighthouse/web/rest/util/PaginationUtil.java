package tr.com.turktelecom.lighthouse.web.rest.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Utility class for handling pagination.
 *
 * <p>
 * Pagination uses the same principles as the <a href="https://developer.github.com/v3/#pagination">Github API</api>,
 * and follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>.
 * </p>
 */
public class PaginationUtil {

    public static HttpHeaders generatePaginationHttpHeaders(Page<?> page, String baseUrl)
        throws URISyntaxException {
        return generatePaginationHttpHeaders(page, baseUrl, null);
    }

    public static HttpHeaders generatePaginationHttpHeaders(Page<?> page, String baseUrl, Map<String, String> parameters)
        throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + page.getTotalElements());
        String link = "";
        if ((page.getNumber() + 1) < page.getTotalPages()) {
            link = "<" + (new URI(baseUrl +"?page=" + (page.getNumber() + 1) + "&size=" + page.getSize())).toString() + appendParameters(parameters) +">; rel=\"next\",";
        }
        // prev link
        if ((page.getNumber()) > 0) {
            link += "<" + (new URI(baseUrl +"?page=" + (page.getNumber() - 1) + "&size=" + page.getSize())).toString() + appendParameters(parameters) + ">; rel=\"prev\",";
        }
        // last and first link
        int lastPage = 0;
        if (page.getTotalPages() > 0) {
            lastPage = page.getTotalPages() - 1;
        }
        link += "<" + (new URI(baseUrl +"?page=" + lastPage + "&size=" + page.getSize())).toString() + appendParameters(parameters) +  ">; rel=\"last\",";
        link += "<" + (new URI(baseUrl +"?page=" + 0 + "&size=" + page.getSize())).toString() + appendParameters(parameters) + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }

    private static String appendParameters(Map<String, String> parameters) {
        if (parameters == null) {
            return "";
        }

        Iterator<String> iterator = parameters.keySet().iterator();
        StringBuffer buffer = new StringBuffer();
        buffer.append("&filterParams=");
        while (iterator.hasNext()) {
            String parameter = iterator.next();
            try {
                buffer.append(URLEncoder.encode(parameter, "UTF-8")).append("=").append(URLEncoder.encode(parameters.get(parameter), "UTF-8"));
                if (iterator.hasNext()) {
                    buffer.append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    public static class MapUtils {
        public static String urlEncodeUTF8(String s) {
            try {
                return URLEncoder.encode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new UnsupportedOperationException(e);
            }
        }

        public static String urlDecodeUTF8(String s) {
            try {
                return URLDecoder.decode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new UnsupportedOperationException(e);
            }
        }

        public static String mapToText(Map<?, ?> map) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
                ));
            }
            return sb.toString();
        }

        public static Map<String, String> textToMap(String text){
            return textToMap(text, null);
        }

        public static Map<String, String> textToMap(String text, Class classz) {
            Map<String,String> map = new HashMap<String, String>();
            text = urlDecodeUTF8(text);
            if (!StringUtils.isEmpty(text)) {
                String[] parameters = text.split("&");
                for (String parameter : parameters) {
                    String[] pair = parameter.split("=");
                    try {
                        String paramName = pair[0];
                        String paramVal = pair[1];

                        if (StringUtils.isEmpty(map.get(paramName))) {
                            map.put(paramName, paramVal);
                        } else {
                            map.put(paramName, new StringBuffer().append(map.get(paramName)).append("&").append(paramVal).toString());
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }
            }
            return map;
        }


    }
}

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

    public static Map<String, String> getOnlyFilteringParameters(Map<String, String> requestParameters, String... requiredParameters) {
        Map<String, String> parameters = new HashMap<String, String>(requestParameters);
        parameters.remove("cacheBuster");
        parameters.remove("page");
        parameters.remove("size");
        parameters.remove("sort");
        if (requiredParameters != null) {
            for (String requiredParameter : requiredParameters) {
                parameters.remove(requiredParameter);
            }
        }
        return parameters;
    }

    public static Map<String, String> extractFilterParams(Map<String, String> filterParams) {
        return extractFilterParams(filterParams, new HashMap<String, Class>());
    }
    public static Map<String, String> extractFilterParams(Map<String, String> filterParams, Map<String, Class> enumParameters) {
        String[] filterCriterias = filterParams.get("filterParams").split("&");
        for (String filterParam : filterCriterias) {
            filterParam = filterParam.trim();
            try {
                String paramName = filterParam.split("=")[0];
                String paramVal = filterParam.split("=")[1];
                if (!StringUtils.isEmpty(paramName) && !StringUtils.isEmpty(paramVal)) {
                    //Eğer ki bu bir enum değeri ise object yerine name değerini gönderiyoruz
                    if (enumParameters.get(paramName) != null) {
                        paramVal = Enum.valueOf(enumParameters.get(paramName), paramVal).name();
                    }
                    filterParams.put(URLDecoder.decode(paramName, "UTF8"), URLDecoder.decode(paramVal, "UTF8"));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        filterParams.remove("filterParams");
        return filterParams;
    }
}

/*    */package utils;

/*    */
/*    */import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.gargoylesoftware.htmlunit.util.UrlUtils;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */public class HttpUtils
/*    */{

    /*    */public static HtmlResponse getResponse(HttpClient client, HttpGet get)
    /*    */throws Exception
    /*    */{
        /* 21 */HtmlResponse htmlResponse = new HtmlResponse();
        /*    */
        /* 24 */HttpResponse response = client.execute(get);
        /* 25 */String baseUrl = get.getRequestLine().getUri();
        /* 26 */htmlResponse.setBaseUrl(baseUrl);
        /* 27 */htmlResponse.setCurrentUrl(baseUrl);
        /* 28 */htmlResponse.setRedirect(false);
        /* 29 */int statusCode = response.getStatusLine().getStatusCode();
        /* 30 */int index = 0;
        /*    */
        /* 32 */while ((index <= 5)
                       && ((statusCode == 301) || (statusCode == 302) || (statusCode == 303) || (statusCode == 307)))
        /*    */{
            /* 35 */htmlResponse.setRedirect(true);
            /* 36 */Header header = response.getLastHeader("Location");
            /* 37 */String redirectUrl = null;
            /* 38 */if (null != header) {
                /* 39 */redirectUrl = header.getValue();
                /*    */}
            /*    */
            /* 42 */if (StringUtils.isEmpty(redirectUrl)) {
                /* 43 */redirectUrl = "/";
                /*    */}
            /* 45 */redirectUrl = UrlUtils.resolveUrl(get.getRequestLine().getUri(), redirectUrl);
            /* 46 */htmlResponse.setCurrentUrl(redirectUrl);
            /* 47 */get.abort();
            /*    */
            /* 49 */get = new HttpGet(redirectUrl);
            /*    */try {
                /* 51 */response = client.execute(get);
                /*    */} finally {
                /* 53 */get.abort();
                /*    */}
            /*    */
            /* 56 */statusCode = response.getStatusLine().getStatusCode();
            /* 57 */index++;
            /*    */}
        /* 59 */htmlResponse.setHttpResponse(response);
        /* 60 */return htmlResponse;
        /*    */}
    /*    */
}

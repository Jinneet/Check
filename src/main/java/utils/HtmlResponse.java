/*    */package utils;

/*    */
/*    */import org.apache.http.HttpResponse;

/*    */
/*    */public class HtmlResponse
/*    */{

    /*    */private HttpResponse httpResponse;
    /*    */private String       currentUrl;
    /*    */private String       baseUrl;
    /*    */private boolean      redirect;

    /*    */
    /*    */public void setHttpResponse(HttpResponse httpResponse)
    /*    */{
        /* 17 */this.httpResponse = httpResponse;
        /*    */}

    /*    */
    /*    */public HttpResponse getHttpResponse() {
        /* 21 */return this.httpResponse;
        /*    */}

    /*    */
    /*    */public void setCurrentUrl(String currentUrl) {
        /* 25 */this.currentUrl = currentUrl;
        /*    */}

    /*    */
    /*    */public String getCurrentUrl() {
        /* 29 */return this.currentUrl;
        /*    */}

    /*    */
    /*    */public void setRedirect(boolean redirect)
    /*    */{
        /* 34 */this.redirect = redirect;
        /*    */}

    /*    */
    /*    */public boolean isRedirect() {
        /* 38 */return this.redirect;
        /*    */}

    /*    */
    /*    */public void setBaseUrl(String baseUrl) {
        /* 42 */this.baseUrl = baseUrl;
        /*    */}

    /*    */
    /*    */public String getBaseUrl() {
        /* 46 */return this.baseUrl;
        /*    */}
    /*    */
}

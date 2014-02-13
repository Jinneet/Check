/*    */package urlcheck;

/*    */
/*    */import java.util.ArrayList;
import java.util.List;

/*    */
/*    */
/*    */public class LoginTask
/*    */{

    /*    */private String                 groupName;
    /*    */private String                 queryUrl;
    /* 20 */private List<HttpFormParam> params = new ArrayList();
    /*    */private HttpFormSubmit         submit;
    /*    */private String                 checkText;
    /*    */private Boolean                intlLogin;

    /*    */
    /*    */public String toString()
    /*    */{
        /* 14 */return "LoginTask [checkText=" + this.checkText + ", groupName=" + this.groupName + ", params="
                       + this.params + ", queryUrl=" + this.queryUrl + ", submit=" + this.submit + "]";
        /*    */}

    /*    */
    /*    */public String getGroupName()
    /*    */{
        /* 26 */return this.groupName;
        /*    */}

    /*    */
    /*    */public void setGroupName(String groupName) {
        /* 30 */this.groupName = groupName;
        /*    */}

    /*    */
    /*    */public String getQueryUrl() {
        /* 34 */return this.queryUrl;
        /*    */}

    /*    */
    /*    */public void setQueryUrl(String queryUrl) {
        /* 38 */this.queryUrl = queryUrl;
        /*    */}

    /*    */
    /*    */public List<HttpFormParam> getParams() {
        /* 42 */return this.params;
        /*    */}

    /*    */
    /*    */public void setParams(List<HttpFormParam> params) {
        /* 46 */this.params = params;
        /*    */}

    /*    */
    /*    */public HttpFormSubmit getSubmit() {
        /* 50 */return this.submit;
        /*    */}

    /*    */
    /*    */public void setSubmit(HttpFormSubmit submit) {
        /* 54 */this.submit = submit;
        /*    */}

    /*    */
    /*    */public void setCheckText(String checkText) {
        /* 58 */this.checkText = checkText;
        /*    */}

    /*    */
    /*    */public String getCheckText() {
        /* 62 */return this.checkText;
        /*    */}

    /*    */
    /*    */public void setIntlLogin(Boolean intlLogin) {
        /* 66 */this.intlLogin = intlLogin;
        /*    */}

    /*    */
    /*    */public Boolean getIntlLogin() {
        /* 70 */return this.intlLogin;
        /*    */}
    /*    */
}

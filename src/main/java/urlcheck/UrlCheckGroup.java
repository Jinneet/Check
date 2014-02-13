/*    */package urlcheck;

/*    */
/*    */import java.util.ArrayList;
import java.util.List;

/*    */
/*    */
/*    */public class UrlCheckGroup
/*    */{

    /*    */private String                name;
    /*    */private LoginTask             loginTask;
    /* 14 */private List<UrlCheckList> urlList = new ArrayList();

    /*    */
    /*    */public void setName(String name) {
        /* 17 */this.name = name;
        /*    */}

    /*    */
    /*    */public String getName() {
        /* 21 */return this.name;
        /*    */}

    /*    */
    /*    */public void setLoginTask(LoginTask loginTask) {
        /* 25 */this.loginTask = loginTask;
        /*    */}

    /*    */
    /*    */public LoginTask getLoginTask() {
        /* 29 */return this.loginTask;
        /*    */}

    /*    */
    /*    */public void setUrlList(List<UrlCheckList> urlList) {
        /* 33 */this.urlList = urlList;
        /*    */}

    /*    */
    /*    */public List<UrlCheckList> getUrlList() {
        /* 37 */return this.urlList;
        /*    */}

    /*    */
    /*    */public String toString()
    /*    */{
        /* 42 */return "UrlCheckGroup [loginTask=" + this.loginTask + ", name=" + this.name + ", urlList="
                       + this.urlList + "]";
        /*    */}
    /*    */
}

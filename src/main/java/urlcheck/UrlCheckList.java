/*    */package urlcheck;

/*    */
/*    */import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*    */
/*    */
/*    */
/*    */public class UrlCheckList
/*    */implements Serializable
/*    */{

    /*    */private static final long serialVersionUID = 5498473861447508369L;
    /*    */private Integer           level;
    /* 16 */private List<String>   urlList          = new ArrayList();
    /*    */private Boolean           checkJs;
    /*    */private Boolean           checkCss;
    /*    */private Boolean           checkImg;

    /*    */
    /*    */public void setLevel(Integer level)
    /*    */{
        /* 22 */this.level = level;
        /*    */}

    /*    */
    /*    */public Integer getLevel() {
        /* 26 */return this.level;
        /*    */}

    /*    */
    /*    */public void setUrlList(List<String> urlList) {
        /* 30 */this.urlList = urlList;
        /*    */}

    /*    */
    /*    */public List<String> getUrlList() {
        /* 34 */return this.urlList;
        /*    */}

    /*    */
    /*    */public void setCheckJs(Boolean checkJs) {
        /* 38 */this.checkJs = checkJs;
        /*    */}

    /*    */
    /*    */public Boolean getCheckJs() {
        /* 42 */return this.checkJs;
        /*    */}

    /*    */
    /*    */public void setCheckCss(Boolean checkCss) {
        /* 46 */this.checkCss = checkCss;
        /*    */}

    /*    */
    /*    */public Boolean getCheckCss() {
        /* 50 */return this.checkCss;
        /*    */}

    /*    */
    /*    */public void setCheckImg(Boolean checkImg) {
        /* 54 */this.checkImg = checkImg;
        /*    */}

    /*    */
    /*    */public Boolean getCheckImg() {
        /* 58 */return this.checkImg;
        /*    */}
    /*    */
}

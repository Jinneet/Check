/*     */package urlcheck;

/*     */
/*     */import java.io.Serializable;

/*     */
/*     */public class UrlTask
/*     */implements Serializable
/*     */{

    /*     */private static final long serialVersionUID = 4404674955515562271L;
    /*     */private String            src;
    /*     */private int               level;
    /*     */private int               maxLevel;
    /*     */private String            parentUrl;
    /*     */private String            topParentUrl;
    /*     */private boolean           checkChildren;
    /*     */private String            groupName;
    /*     */private Boolean           checkJs;
    /*     */private Boolean           checkCss;
    /*     */private Boolean           checkImg;

    /*     */
    /*     */public String getSrc()
    /*     */{
        /* 25 */return this.src;
        /*     */}

    /*     */
    /*     */public void setSrc(String src) {
        /* 29 */this.src = src;
        /*     */}

    /*     */
    /*     */public int getLevel() {
        /* 33 */return this.level;
        /*     */}

    /*     */
    /*     */public void setLevel(int level) {
        /* 37 */this.level = level;
        /*     */}

    /*     */
    /*     */public String getParentUrl() {
        /* 41 */return this.parentUrl;
        /*     */}

    /*     */
    /*     */public void setParentUrl(String parentUrl) {
        /* 45 */this.parentUrl = parentUrl;
        /*     */}

    /*     */
    /*     */public void setTopParentUrl(String topParentUrl) {
        /* 49 */this.topParentUrl = topParentUrl;
        /*     */}

    /*     */
    /*     */public String getTopParentUrl() {
        /* 53 */return this.topParentUrl;
        /*     */}

    /*     */
    /*     */public void setCheckChildren(boolean checkChildren) {
        /* 57 */this.checkChildren = checkChildren;
        /*     */}

    /*     */
    /*     */public boolean isCheckChildren() {
        /* 61 */return this.checkChildren;
        /*     */}

    /*     */
    /*     */public void setGroupName(String groupName) {
        /* 65 */this.groupName = groupName;
        /*     */}

    /*     */
    /*     */public String getGroupName() {
        /* 69 */return this.groupName;
        /*     */}

    /*     */
    /*     */public void setMaxLevel(int maxLevel) {
        /* 73 */this.maxLevel = maxLevel;
        /*     */}

    /*     */
    /*     */public int getMaxLevel() {
        /* 77 */return this.maxLevel;
        /*     */}

    /*     */
    /*     */public void setCheckJs(Boolean checkJs) {
        /* 81 */this.checkJs = checkJs;
        /*     */}

    /*     */
    /*     */public Boolean getCheckJs() {
        /* 85 */return Boolean.valueOf(null == this.checkJs ? false : this.checkJs.booleanValue());
        /*     */}

    /*     */
    /*     */public void setCheckCss(Boolean checkCss) {
        /* 89 */this.checkCss = checkCss;
        /*     */}

    /*     */
    /*     */public Boolean getCheckCss() {
        /* 93 */return Boolean.valueOf(null == this.checkCss ? false : this.checkCss.booleanValue());
        /*     */}

    /*     */
    /*     */public void setCheckImg(Boolean checkImg) {
        /* 97 */this.checkImg = checkImg;
        /*     */}

    /*     */
    /*     */public Boolean getCheckImg() {
        /* 101 */return Boolean.valueOf(null == this.checkImg ? false : this.checkImg.booleanValue());
        /*     */}
    /*     */
}

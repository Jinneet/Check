/*    */package urlcheck;

/*    */
/*    */public class HttpFormParam
/*    */{

    /*    */private String name;
    /*    */private String id;
    /*    */private String value;

    /*    */
    /*    */public String toString()
    /*    */{
        /* 6 */return "HttpFormParam [id=" + this.id + ", name=" + this.name + ", value=" + this.value + "]";
        /*    */}

    /*    */
    /*    */public void setName(String name)
    /*    */{
        /* 14 */this.name = name;
        /*    */}

    /*    */
    /*    */public String getName() {
        /* 18 */return this.name;
        /*    */}

    /*    */
    /*    */public void setId(String id) {
        /* 22 */this.id = id;
        /*    */}

    /*    */
    /*    */public String getId() {
        /* 26 */return this.id;
        /*    */}

    /*    */
    /*    */public void setValue(String value) {
        /* 30 */this.value = value;
        /*    */}

    /*    */
    /*    */public String getValue() {
        /* 34 */return this.value;
        /*    */}
    /*    */
}

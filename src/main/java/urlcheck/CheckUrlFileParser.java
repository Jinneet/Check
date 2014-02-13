/*     */package urlcheck;

/*     */
/*     */import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */public class CheckUrlFileParser
/*     */{

    /*     */public List<UrlCheckGroup> parser(File checkUrlFile)
    /*     */throws FileNotFoundException, DocumentException
    /*     */{
        /* 36 */FileInputStream fileInputStream = null;
        /* 37 */BufferedInputStream bufferedInputStream = null;
        /*     */try {
            /* 39 */fileInputStream = new FileInputStream(checkUrlFile);
            /* 40 */bufferedInputStream = new BufferedInputStream(fileInputStream);
            /* 41 */return parser(bufferedInputStream);
            /*     */} finally {
            /*     */try {
                /* 44 */if (bufferedInputStream != null)
                /* 45 */bufferedInputStream.close();
                /* 46 */if (fileInputStream != null)
                /* 47 */fileInputStream.close();
                /*     */}
            /*     */catch (IOException e) {
                /*     */}
            /*     */}
        /*     */}

    /*     */
    /*     */private List<UrlCheckGroup> parser(InputStream in) throws DocumentException {
        /* 55 */SAXReader reader = new SAXReader();
        /* 56 */Document document = reader.read(in);
        /* 57 */List urlCheckGroupList = new ArrayList();
        /*     */
        /* 59 */List nodeList = document.selectNodes("//group");
        /* 60 */for (Iterator i$ = nodeList.iterator(); i$.hasNext();) {
            Object node = i$.next();
            /* 61 */Element e = (Element) node;
            /* 62 */UrlCheckGroup urlCheckGroup = parseGroup(e);
            /* 63 */urlCheckGroupList.add(urlCheckGroup);
            /*     */}
        /* 65 */return urlCheckGroupList;
        /*     */}

    /*     */
    /*     */private UrlCheckGroup parseGroup(Element element)
    /*     */{
        /* 74 */UrlCheckGroup urlCheckGroup = new UrlCheckGroup();
        /* 75 */Node loginNode = element.selectSingleNode("login");
        /* 76 */String name = element.attributeValue("name");
        /* 77 */if (null != loginNode) {
            /* 78 */LoginTask loginTask = parseLoginTask((Element) loginNode);
            /* 79 */loginTask.setGroupName(name);
            /* 80 */urlCheckGroup.setLoginTask(loginTask);
            /*     */}
        /* 82 */urlCheckGroup.setName(name);
        /* 83 */urlCheckGroup.setUrlList(parseUrlList(element));
        /* 84 */return urlCheckGroup;
        /*     */}

    /*     */
    /*     */private LoginTask parseLoginTask(Element element)
    /*     */{
        /* 93 */LoginTask loginTask = new LoginTask();
        /* 94 */Element loginUrlNode = element.element("loginUrl");
        /* 95 */loginTask.setQueryUrl(loginUrlNode.getTextTrim());
        /* 96 */List paramList = element.selectNodes("params/param");
        /* 97 */for (Iterator i$ = paramList.iterator(); i$.hasNext();) {
            Object paramNode = i$.next();
            /* 98 */HttpFormParam formParam = new HttpFormParam();
            /* 99 */Element e = (Element) paramNode;
            /* 100 */List attributes = e.attributes();
            /* 101 */for (Iterator i$ = attributes.iterator(); i$.hasNext();) {
                Object o = i$.next();
                /* 102 */Attribute attribute = (Attribute) o;
                /* 103 */String value = attribute.getValue();
                /* 104 */String name = attribute.getName();
                /* 105 */if ("id".equalsIgnoreCase(name))
                /* 106 */formParam.setId(value);
                /* 107 */else if ("name".equalsIgnoreCase(name))
                /* 108 */formParam.setName(value);
                /* 109 */else if ("value".equalsIgnoreCase(name)) {
                    /* 110 */formParam.setValue(value);
                    /*     */}
                /*     */}
            /* 113 */loginTask.getParams().add(formParam);
            /*     */}
        /* 115 */Element intlLoginElement = element.element("intlLogin");
        /* 116 */if (null == intlLoginElement)
        /* 117 */loginTask.setIntlLogin(Boolean.valueOf(false));
        /*     */else {
            /* 119 */loginTask.setIntlLogin(Boolean.valueOf(intlLoginElement.getTextTrim()));
            /*     */}
        /* 121 */Element submitElement = element.element("submit");
        /* 122 */if (null != submitElement) {
            /* 123 */HttpFormSubmit submit = new HttpFormSubmit();
            /* 124 */String id = submitElement.attributeValue("id");
            /* 125 */String name = submitElement.attributeValue("name");
            /* 126 */String text = submitElement.attributeValue("text");
            /* 127 */String tagName = submitElement.attributeValue("tagName");
            /* 128 */String type = submitElement.attributeValue("type");
            /* 129 */submit.setId(id);
            /* 130 */submit.setName(name);
            /* 131 */submit.setText(text);
            /* 132 */submit.setTagName(tagName);
            /* 133 */submit.setType(type);
            /* 134 */loginTask.setSubmit(submit);
            /* 135 */} else if (!loginTask.getIntlLogin().booleanValue()) {
            /* 136 */throw new RuntimeException("Login Node must be contain the submit element!");
            /*     */}
        /*     */
        /* 139 */Element checkElement = element.element("checktxt");
        /* 140 */if (null != checkElement) {
            /* 141 */loginTask.setCheckText(checkElement.getTextTrim());
            /*     */}
        /* 143 */return loginTask;
        /*     */}

    /*     */
    /*     */private List<UrlCheckList> parseUrlList(Element element) {
        /* 147 */List urlList = new ArrayList();
        /* 148 */List list = element.selectNodes("urllist");
        /* 149 */for (Iterator i$ = list.iterator(); i$.hasNext();) {
            Object o = i$.next();
            /* 150 */Element e = (Element) o;
            /* 151 */UrlCheckList urlCheckList = new UrlCheckList();
            /* 152 */String level = e.attributeValue("level");
            /* 153 */if (StringUtils.isNotEmpty(level)) {
                /* 154 */urlCheckList.setLevel(Integer.valueOf(level));
                /*     */}
            /*     */
            /* 157 */String checkJs = e.attributeValue("checkJs");
            /* 158 */if (StringUtils.isNotEmpty(checkJs)) {
                /* 159 */urlCheckList.setCheckJs(Boolean.valueOf(checkJs));
                /*     */}
            /* 161 */String checkCss = e.attributeValue("checkCss");
            /* 162 */if (StringUtils.isNotEmpty(checkCss)) {
                /* 163 */urlCheckList.setCheckCss(Boolean.valueOf(checkCss));
                /*     */}
            /* 165 */String checkImg = e.attributeValue("checkImg");
            /* 166 */if (StringUtils.isNotEmpty(checkImg)) {
                /* 167 */urlCheckList.setCheckImg(Boolean.valueOf(checkImg));
                /*     */}
            /* 169 */List urlNodeList = e.selectNodes("url");
            /* 170 */List urlStrList = new ArrayList();
            /* 171 */for (Iterator i$ = urlNodeList.iterator(); i$.hasNext();) {
                Object urlNode = i$.next();
                /* 172 */Element node = (Element) urlNode;
                /* 173 */urlStrList.add(node.getTextTrim());
                /*     */}
            /* 175 */urlCheckList.setUrlList(urlStrList);
            /* 176 */urlList.add(urlCheckList);
            /*     */}
        /* 178 */return urlList;
        /*     */}

    /*     */
    /*     */public static void main(String[] args) throws FileNotFoundException, DocumentException {
        /* 182 */CheckUrlFileParser parser = new CheckUrlFileParser();
        /* 183 */List list = parser.parser(new File("D://urlCheck.xml"));
        /* 184 */for (UrlCheckGroup u : list)
            /* 185 */System.out.println(u);
        /*     */}
    /*     */
}

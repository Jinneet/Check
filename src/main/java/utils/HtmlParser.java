/*     */package utils;

/*     */
/*     */import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import urlcheck.UrlTask;

import com.gargoylesoftware.htmlunit.util.UrlUtils;

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
/*     */public class HtmlParser
/*     */{

    /* 23 */private static final Log logger   = LogFactory.getLog(HtmlParser.class);
    /*     */
    /* 25 */private DOMParser        parser   = null;
    /* 26 */private HtmlResponse     response = null;
    /* 27 */private Document         document = null;
    /*     */private UrlTask             parentTask;

    /*     */
    /*     */public HtmlParser(HtmlResponse response, UrlTask parentTask)
    /*     */{
        /* 31 */this.parser = new DOMParser();
        /* 32 */this.response = response;
        /* 33 */this.parentTask = parentTask;
        /* 34 */parse();
        /*     */}

    /*     */
    /*     */private void parse()
    /*     */{
        /* 43 */String encoding = "GBK";
        /* 44 */HttpEntity entity = this.response.getHttpResponse().getEntity();
        /* 45 */Header header = entity.getContentEncoding();
        /* 46 */if (null != header) {
            /* 47 */String value = header.getValue();
            /* 48 */if (StringUtils.isNotEmpty(value))
            /* 49 */encoding = value;
            /*     */}
        /*     */try
        /*     */{
            /* 53 */this.parser.setProperty("http://cyberneko.org/html/properties/default-encoding", encoding);
            /* 54 */this.parser.parse(new InputSource(new ByteArrayInputStream(EntityUtils.toByteArray(entity))));
            /*     */
            /* 58 */this.document = this.parser.getDocument();
            /*     */} catch (Exception e) {
            /* 60 */logger.error(this.parentTask.getSrc() + " parser Eroor! " + e.getMessage(), e);
            /*     */}
        /*     */}

    /*     */
    /*     */public List<UrlTask> getFrameList()
    /*     */{
        /* 66 */List taskList = new ArrayList();
        /* 67 */if (null != this.document) {
            /* 68 */List elements = getDocumentElementsByTagNames(new String[] { "frame", "iframe" });
            /* 69 */for (Element element : elements) {
                /* 70 */String src = element.getAttribute("src");
                /* 71 */if (StringUtils.isNotEmpty(src)) {
                    /* 72 */String url = UrlUtils.resolveUrl(this.response.getCurrentUrl(), src);
                    /* 73 */UrlTask task = new UrlTask();
                    /* 74 */task.setCheckChildren(true);
                    /* 75 */task.setLevel(this.parentTask.getLevel());
                    /* 76 */task.setParentUrl(this.parentTask.getSrc());
                    /* 77 */task.setSrc(url);
                    /* 78 */if (StringUtils.isNotEmpty(this.parentTask.getTopParentUrl()))
                    /* 79 */task.setTopParentUrl(this.parentTask.getTopParentUrl());
                    /* 80 */taskList.add(task);
                    /*     */}
                /*     */}
            /*     */}
        /* 84 */return taskList;
        /*     */}

    /*     */
    /*     */private List<Element> getDocumentElementsByTagNames(String[] tagNames) {
        /* 88 */List elements = new ArrayList();
        /* 89 */if (null != this.document) {
            /* 90 */for (String tagName : tagNames) {
                /* 91 */NodeList list = this.document.getElementsByTagName(tagName);
                /* 92 */for (int i = 0; i < list.getLength(); i++) {
                    /* 93 */Element element = (Element) list.item(i);
                    /* 94 */elements.add(element);
                    /*     */}
                /*     */}
            /*     */}
        /* 98 */return elements;
        /*     */}

    /*     */
    /*     */public List<UrlTask> getLinkList() {
        /* 102 */List taskList = new ArrayList();
        /* 103 */if (null != this.document) {
            /* 104 */List elements = getDocumentElementsByTagNames(new String[] { "a" });
            /* 105 */for (Element element : elements) {
                /* 106 */String src = element.getAttribute("href");
                /* 107 */if ((StringUtils.isNotEmpty(src)) && (!StringUtils.startsWithIgnoreCase(src, "javascript:"))) {
                    /* 108 */String url = UrlUtils.resolveUrl(this.response.getCurrentUrl(), src);
                    /* 109 */UrlTask task = new UrlTask();
                    /* 110 */task.setCheckChildren(true);
                    /* 111 */task.setLevel(this.parentTask.getLevel() + 1);
                    /* 112 */task.setParentUrl(this.parentTask.getSrc());
                    /* 113 */task.setSrc(url);
                    /* 114 */if (StringUtils.isNotEmpty(this.parentTask.getTopParentUrl()))
                    /* 115 */task.setTopParentUrl(this.parentTask.getTopParentUrl());
                    /*     */else
                    /* 117 */task.setTopParentUrl(this.parentTask.getSrc());
                    /* 118 */taskList.add(task);
                    /*     */}
                /*     */}
            /*     */}
        /* 122 */return taskList;
        /*     */}

    /*     */
    /*     */public List<UrlTask> getImageList() {
        /* 126 */List taskList = new ArrayList();
        /* 127 */if (null != this.document) {
            /* 128 */List elements = getDocumentElementsByTagNames(new String[] { "img" });
            /* 129 */for (Element element : elements) {
                /* 130 */String src = element.getAttribute("src");
                /* 131 */if (StringUtils.isNotEmpty(src)) {
                    /* 132 */String url = UrlUtils.resolveUrl(this.response.getCurrentUrl(), src);
                    /* 133 */UrlTask task = new UrlTask();
                    /* 134 */task.setCheckChildren(false);
                    /* 135 */task.setLevel(this.parentTask.getLevel() + 1);
                    /* 136 */task.setParentUrl(this.parentTask.getSrc());
                    /* 137 */task.setSrc(url);
                    /* 138 */if (StringUtils.isNotEmpty(this.parentTask.getTopParentUrl()))
                    /* 139 */task.setTopParentUrl(this.parentTask.getTopParentUrl());
                    /*     */else
                    /* 141 */task.setTopParentUrl(this.parentTask.getSrc());
                    /* 142 */taskList.add(task);
                    /*     */}
                /*     */}
            /*     */}
        /* 146 */return taskList;
        /*     */}

    /*     */
    /*     */public List<UrlTask> getJsList() {
        /* 150 */List taskList = new ArrayList();
        /* 151 */if (null != this.document) {
            /* 152 */List elements = getDocumentElementsByTagNames(new String[] { "script" });
            /* 153 */for (Element element : elements) {
                /* 154 */String src = element.getAttribute("src");
                /* 155 */if (StringUtils.isNotEmpty(src)) {
                    /* 156 */String url = UrlUtils.resolveUrl(this.response.getCurrentUrl(), src);
                    /* 157 */UrlTask task = new UrlTask();
                    /* 158 */task.setCheckChildren(false);
                    /* 159 */task.setLevel(this.parentTask.getLevel() + 1);
                    /* 160 */task.setParentUrl(this.parentTask.getSrc());
                    /* 161 */task.setSrc(url);
                    /* 162 */if (StringUtils.isNotEmpty(this.parentTask.getTopParentUrl()))
                    /* 163 */task.setTopParentUrl(this.parentTask.getTopParentUrl());
                    /*     */else
                    /* 165 */task.setTopParentUrl(this.parentTask.getSrc());
                    /* 166 */taskList.add(task);
                    /*     */}
                /*     */}
            /*     */}
        /* 170 */return taskList;
        /*     */}

    /*     */
    /*     */public List<UrlTask> getCssList() {
        /* 174 */List taskList = new ArrayList();
        /* 175 */if (null != this.document) {
            /* 176 */List elements = getDocumentElementsByTagNames(new String[] { "link" });
            /* 177 */for (Element element : elements) {
                /* 178 */String src = element.getAttribute("src");
                /* 179 */if (StringUtils.isNotEmpty(src)) {
                    /* 180 */String url = UrlUtils.resolveUrl(this.response.getCurrentUrl(), src);
                    /* 181 */UrlTask task = new UrlTask();
                    /* 182 */task.setCheckChildren(false);
                    /* 183 */task.setLevel(this.parentTask.getLevel() + 1);
                    /* 184 */task.setParentUrl(this.parentTask.getSrc());
                    /* 185 */task.setSrc(url);
                    /* 186 */if (StringUtils.isNotEmpty(this.parentTask.getTopParentUrl()))
                    /* 187 */task.setTopParentUrl(this.parentTask.getTopParentUrl());
                    /*     */else
                    /* 189 */task.setTopParentUrl(this.parentTask.getSrc());
                    /* 190 */taskList.add(task);
                    /*     */}
                /*     */}
            /*     */}
        /* 194 */return taskList;
        /*     */}
    /*     */
}

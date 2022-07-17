package de.mirieger.midoc.model;

import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.util.GeneralUtil;

import java.io.Serializable;
import java.util.Objects;

public class PageLink implements Serializable, Comparable<PageLink> {

    private String title;
    private String link;
    private long pageId;

    public PageLink(Page page) {
        this.pageId = page.getId();
        this.title = page.getTitle();
        this.link = GeneralUtil.getGlobalSettings().getBaseUrl() + page.getUrlPath();
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public long getPageId() {
        return pageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PageLink pageLink = (PageLink) o;
        return pageId == pageLink.pageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageId);
    }

    @Override
    public String toString() {
        return "PageLink{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", pageId=" + pageId +
                '}';
    }

    @Override
    public int compareTo(PageLink o) {
        return this.getTitle().compareTo(o.getTitle());
    }
}

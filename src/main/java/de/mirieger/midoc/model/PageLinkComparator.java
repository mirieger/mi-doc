package de.mirieger.midoc.model;

import java.util.Comparator;

public class PageLinkComparator implements Comparator<PageLink> {

    @Override
    public int compare(PageLink o1, PageLink o2) {
        return o1.compareTo(o2);
    }
}

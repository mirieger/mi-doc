package de.mirieger.midoc.api;

import de.mirieger.midoc.model.PageLink;

import java.util.List;
import java.util.Set;

public interface MiDocManager {

    List<PageLink> getIncomingLinksByLabels(long pageId, String labelTitle);

}
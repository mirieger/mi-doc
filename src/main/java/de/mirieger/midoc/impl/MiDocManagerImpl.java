package de.mirieger.midoc.impl;

import com.atlassian.confluence.content.service.PageService;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.labels.Label;
import com.atlassian.confluence.labels.LabelManager;
import com.atlassian.confluence.links.LinkManager;
import com.atlassian.confluence.links.OutgoingLink;
import com.atlassian.confluence.pages.Page;
import com.atlassian.sal.api.ApplicationProperties;
import de.mirieger.midoc.api.MiDocManager;
import de.mirieger.midoc.model.PageLink;
import de.mirieger.midoc.model.PageLinkComparator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hsqldb.lib.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.method.P;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named
public class MiDocManagerImpl implements MiDocManager
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MiDocManagerImpl.class);

    private final PageService pageService;
    private final LinkManager linkManager;
    private final LabelManager labelManager;

    @Inject
    public MiDocManagerImpl(PageService pageService, LinkManager linkManager, LabelManager labelManager)
    {
        this.pageService = pageService;
        this.linkManager = linkManager;
        this.labelManager = labelManager;
    }

    @Override
    public List<PageLink> getIncomingLinksByLabels(long pageId, String labelTitle) {
        LOGGER.debug("Getting incoming links for pageId: " + pageId);
        LOGGER.debug("Getting incoming links for labelTitle: " + labelTitle);

        Set<PageLink> result = new HashSet<>();
        Page page = this.pageService.getIdPageLocator(pageId).getPage();
        if (page != null) {
            List<OutgoingLink> incomingLinks = this.linkManager.getIncomingLinksToContent(page);
            if (CollectionUtils.isNotEmpty(incomingLinks)) {
                /*
                Set<Label> labels = new HashSet<>();
                if (CollectionUtils.isNotEmpty(labelTitles)) {
                    for (String labelTitle : labelTitles) {
                        Label label = this.labelManager.getLabel(labelTitle);
                        if (label != null) {
                            labels.add(label);
                        }
                    }
                }
                 */

                Label label = null;
                if (StringUtils.isNotBlank(labelTitle)) {
                    LOGGER.debug("Fetching label: " + labelTitle);
                    label = this.labelManager.getLabel(labelTitle);
                }
                if (label != null) {
                    LOGGER.debug("Restricting to label: " + label.getName());
                } else {
                    LOGGER.debug("No label specified or specified label not found: " + labelTitle);
                }

                for (OutgoingLink outgoingLink : incomingLinks) {
                    ContentEntityObject ceo = outgoingLink.getSourceContent();
                    if (ceo instanceof Page) {
                        if (LOGGER.isDebugEnabled()) {
                            for (Label pageLabel : ceo.getLabels()) {
                                LOGGER.debug("Found label on source content: " + pageLabel.getName());
                            }
                        }
                        if (label == null || ceo.getLabels().contains(label)) {
                            LOGGER.debug("Adding link to source: " + ceo.getTitle());
                            result.add(new PageLink((Page) ceo));
                        } else {
                            LOGGER.debug("Given source page does not have label '" + label.getName() + "': " + ceo.getTitle());
                        }
                    } else {
                        LOGGER.debug("Source content if of the following non-page type: " + ceo.getClass().getName());
                    }
                }
            } else {
                LOGGER.debug("No incoming links found on page:" + page.getTitle());
            }
        } else {
            LOGGER.debug("Page not found: " + pageId);
        }

        List<PageLink> sortedResult = new ArrayList<>(result);
        sortedResult.sort(new PageLinkComparator());

        return sortedResult;
    }
}
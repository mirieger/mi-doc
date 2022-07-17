package de.mirieger.midoc.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.Renderer;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.velocity.VelocityContextUtils;
import de.mirieger.midoc.api.MiDocManager;
import de.mirieger.midoc.impl.MiDocManagerImpl;
import de.mirieger.midoc.model.PageLink;
import org.apache.commons.lang.StringUtils;
import org.hsqldb.lib.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IncomingLinksMacro implements Macro {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingLinksMacro.class);
    private static final String VELOCITY_TEMPLATE = "/de/mirieger/midoc/vm/incoming-links.vm";

    private final MiDocManager miDocManager;
    private final Renderer renderer;

    @Inject
    public IncomingLinksMacro(MiDocManager miDocManager, Renderer renderer) {
        this.miDocManager = miDocManager;
        this.renderer = renderer;
    }


    @Override
    public String execute(Map<String, String> parameters, String body, ConversionContext conversionContext) {
        Map<String, Object> velocityContext = MacroUtils.defaultVelocityContext();
        String labelTitle = parameters.get("label");
        LOGGER.debug("Found label parameter: " + labelTitle);
        String style = parameters.get("style");
        LOGGER.debug("Found style: " + style);
        if (StringUtils.isBlank(style)) {
            style = "BR";
        }
        velocityContext.put("style", style);
        String count = parameters.get("count");
        LOGGER.debug("Found count: " + count);
        if (StringUtils.isBlank(count)) {
            count = "false";
        }
        velocityContext.put("count", Boolean.valueOf(count));

        List<PageLink> incomingLinks = this.miDocManager.getIncomingLinksByLabels(conversionContext.getEntity().getId(), labelTitle);
        LOGGER.debug("Found number of incoming links: " + incomingLinks.size());
        velocityContext.put("links", incomingLinks);

        return this.renderer.render(VelocityUtils.getRenderedTemplate(VELOCITY_TEMPLATE, velocityContext), conversionContext);
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}

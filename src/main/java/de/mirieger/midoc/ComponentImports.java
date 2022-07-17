package de.mirieger.midoc;

import com.atlassian.confluence.content.render.xhtml.Renderer;
import com.atlassian.confluence.content.service.PageService;
import com.atlassian.confluence.content.service.SpaceService;
import com.atlassian.confluence.labels.LabelManager;
import com.atlassian.confluence.links.LinkManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

public abstract class ComponentImports {
    @ComponentImport
    private LabelManager labelManager;
    @ComponentImport
    private PageService pageService;
    @ComponentImport
    private SpaceService spaceService;
    @ComponentImport
    private LinkManager linkManager;
    @ComponentImport
    private Renderer renderer;
}

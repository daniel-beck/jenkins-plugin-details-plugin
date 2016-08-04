import jenkins.model.Jenkins

def f=namespace(lib.FormTagLib)
def l=namespace(lib.LayoutTagLib)
def st=namespace("jelly:stapler")

l.layout(norefresh:true,  title:_("Plugin Details")) {
    l.main_panel {

        h1("Plugin Details")

        ul {
            for (def plugin : Jenkins.getInstance().getPluginManager().getPlugins()) {
                li {
                    a(href: rootURL + '/pluginDetails/plugin?plugin=' + plugin.getShortName(), plugin.getDisplayName())
                }
            }
        }
    }
}
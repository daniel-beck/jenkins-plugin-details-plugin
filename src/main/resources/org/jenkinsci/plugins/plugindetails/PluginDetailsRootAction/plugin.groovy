import jenkins.model.Jenkins

def f=namespace(lib.FormTagLib)
def l=namespace(lib.LayoutTagLib)
def st=namespace("jelly:stapler")

def plugin = Jenkins.getInstance().getPluginManager().getPlugin(pluginName)

l.layout(norefresh:true,  title:_("Plugin Details")) {
    l.main_panel {

        style(type: 'text/css') {
            raw("#wiki h4:first-of-type { display: none; }")
            raw("#wiki div:first-of-type { display: none; }")
        }

        h1("Plugin Details: " + plugin.displayName)

        p {
            a(href: plugin.url, plugin.url)
        }

        def wiki = my.getWikiContent(plugin)

        div(id: 'wiki') {
            raw(wiki)
        }
    }
}
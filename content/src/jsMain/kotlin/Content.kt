import kotlinx.browser.document
import kotlinx.serialization.json.Json
import org.w3c.dom.*

fun main() {
    val body = document.body ?: return
    val config = MutationObserverInit(childList = true, subtree = true)

    chrome.storage.local.get("setting") { items ->
        val rawSetting = items["setting"]?.let {
            JSON.stringify(it)
        } ?: return@get
        val setting = Json.decodeFromString<Setting>(rawSetting)
        val site = setting.siteList.find {
            it.urlPattern.toRegex().matches(document.URL)
        } ?: return@get
        site.replaceTargetList.forEach { replaceTarget ->
            val replaceWordList = replaceTarget.replaceWordPatternRef?.let {
                setting.replaceWordPatternRef?.get(it)
            } ?: replaceTarget.replaceWordPattern ?: return@get
            val replaceList = replaceWordList.map {
                it.key.toRegex() to it.value
            }
            replaceElements(replaceTarget.selectors, replaceList)
            val observer = MutationObserver { mutationRecords, observer ->
                mutationRecords.forEach { mutation ->
                    if (mutation.type == "childList") {
                        observer.disconnect()
                        replaceElements(replaceTarget.selectors, replaceList)
                        observer.observe(body, config)
                    }
                }
            }
            observer.observe(body, config)
        }
    }
}

fun replaceElements(selectors: List<String>, replaceList: List<Pair<Regex, String>>) {
    val nodes = document.querySelectorAll(selectors.joinToString(","))
    nodes.asList().forEach {
        replaceText(it, replaceList)
    }
}

fun replaceText(target: Node, replaceList: List<Pair<Regex, String>>) {
    var child = target.firstChild
    while (child != null) {
        child = replaceChild(child.nextSibling, child, replaceList)
    }
}

fun replaceChild(next: Node?, node: Node?, replaceList: List<Pair<Regex, String>>): Node? {
    if (node == null) {
        return null
    }
    when (node.nodeType) {
        Node.TEXT_NODE -> {
            val replaced = document.createTextNode(
                replaceList.fold(node.textContent ?: "") { acc, pair ->
                    acc.replace(pair.first, pair.second)
                }
            )
            node.parentNode?.replaceChild(replaced, node)
            return next
        }
        Node.ELEMENT_NODE -> {
            var child = node.firstChild
            while (child != null) {
                child = replaceChild(child.nextSibling, child, replaceList)
            }
            return next
        }
        else -> {
            return next
        }
    }
}

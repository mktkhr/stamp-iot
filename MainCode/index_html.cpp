#include <Arduino.h>
#include <regex>
#include "index_html.h"

String generateHtml(const char *macAddress, const char *ssid, const char *version, const char *bootMode)
{
    std::string html = INDEX_HTML;
    html = regex_replace(html, std::regex(u8R"(\$macAddress\$)"), std::string(macAddress));
    html = regex_replace(html, std::regex(u8R"(\$ssid\$)"), std::string(ssid));
    html = regex_replace(html, std::regex(u8R"(\$version\$)"), std::string(version));
    html = regex_replace(html, std::regex(u8R"(\$bootMode\$)"), std::string(bootMode));
    return html.c_str();
}
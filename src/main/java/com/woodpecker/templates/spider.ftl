# coding=utf-8
import scrapy
from ${projName}.items import ${ClassItem}

class ${ClassSpider}(scrapy.Spider):
    name = "${name}"
    allowed_domains = [<#list allowed_domains as domain> "${domain}.com", </#list>]
    start_urls = [<#list start_urls as url> "http://www.${url}", </#list>]

    def parse(self, response):
        itemPaths = response.xpath('${itemRule}')

        for eachItem in itemPaths:
            item = ${ClassItem}()
            <#list proToRule? keys as property>
            item['${property}'] = eachItem.xpath('${proToRule[property]}').extract()[0]
            </#list>
            yield item

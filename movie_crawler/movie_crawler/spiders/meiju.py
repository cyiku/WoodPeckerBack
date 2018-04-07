# coding=utf-8
import scrapy
from movie_crawler.items import MovieCrawlerItem

class MovieCrawlerSpider(scrapy.Spider):
    name = "meiju"
    allowed_domains = [ "meijutt.com", ]
    start_urls = [ "http://www.www.meijutt.com/new100.html", ]

    def parse(self, response):
        itemPaths = response.xpath('//ul[@class="top-list  fn-clear"]/li')

        for eachItem in itemPaths:
            item = MovieCrawlerItem()
            item['name'] = eachItem.xpath('./h5/a/@title').extract()[0]
            yield item

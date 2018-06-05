# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import pymongo
import sys
from settings import MONGO_HOST, MONGO_PORT, MONGODB_DBNAME, MONGODB_COLLECTION
reload(sys)
sys.setdefaultencoding('utf-8')

class ${ClassPipeline}(object):
    def __init__(self):
        client = pymongo.MongoClient(MONGO_HOST, MONGO_PORT)
        db = client[MONGODB_DBNAME]
        self.collection = db[MONGODB_COLLECTION]


    def process_item(self, item):

        self.collection.save({
            <#list properties as property>
            '${property}', item['${property}'],
            </#list>
        })
        return item
package com.example.newsreader.data.model


data class SearchNewsModel(val result:Result)
//页数从1开始
data class Result(val curpage:Int,val newslist:List<SearchNews>)
//时间，标题，来源，图片，新闻URL
data class SearchNews(val ctime:String,val title:String,val source:String,val picUrl:String,val url:String)
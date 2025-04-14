package com.example.newsreader.collectionAndHistory.ui

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * 创建一个自定义的WebViewClient来处理页面加载和内容提取
 */
class CustomWebViewClient : WebViewClient() {

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)

        // 使用evaluateJavascript方法替代loadUrl
        val js = """
            (function() {
                // 1. 首先添加自定义样式
                var style = document.createElement('style');
                style.textContent = `
                    /* 通用元素隐藏 */
                    .header, .nav, .footer, .app-download-banner, .wrapper-top,
                    .top-bar, .bottom-bar, .index-nav, .top-navigation,
                    div[class*="ad"], div[id*="ad"],
                    .download-app, .related-news, .comment-wrapper,
                    .share-title, .share-wrap, .hot-news {
                        display: none !important;
                    }
                    
                    /* 内容区域样式 */
                    .article-content {
                        padding: 16px !important;
                        margin: 0 auto !important;
                        max-width: 100% !important;
                        font-size: 16px !important;
                        line-height: 1.8 !important;
                    }
                    
                    /* 图片样式 */
                    .article-content img {
                        max-width: 100% !important;
                        height: auto !important;
                        margin: 10px auto !important;
                    }
                    
                    /* 重置body样式 */
                    body {
                        padding: 0 !important;
                        margin: 0 !important;
                        background: #ffffff !important;
                    }
                `;
                document.head.appendChild(style);
                
                // 2. 移除干扰元素
                const removeElements = () => {
                    const selectors = [
                        '.header', '.nav', '.footer', '.app-download-banner',
                        '.wrapper-top', '.top-bar', '.bottom-bar', '.index-nav',
                        'div[class*="ad"]', 'div[id*="ad"]',
                        '.download-app', '.related-news', '.comment-wrapper',
                        '.share-title', '.share-wrap', '.hot-news'
                    ];
                    
                    selectors.forEach(selector => {
                        document.querySelectorAll(selector).forEach(element => {
                            element.remove();
                        });
                    });
                };
                
                // 3. 执行清理
                removeElements();
                
                // 4. 设置一个观察器，处理动态加载的元素
                const observer = new MutationObserver(function(mutations) {
                    removeElements();
                });
                
                observer.observe(document.body, {
                    childList: true,
                    subtree: true
                });
                
                // 5. 返回清理完成的状态
                return "Cleanup completed";
            })();
        """

        view.evaluateJavascript(js) { result ->
            // 可以在这里添加日志来确认JavaScript执行完成
            android.util.Log.d("WebView", "JavaScript execution result: $result")
        }
    }

    override fun shouldInterceptRequest(
        view: WebView,
        request: WebResourceRequest
    ): WebResourceResponse? {
        // 拦截并阻止广告资源加载
        val url = request.url.toString().lowercase()
        if (url.contains("ad") || url.contains("analytics") || url.contains("tracker")) {
            return WebResourceResponse("text/plain", "utf-8", null)
        }
        return super.shouldInterceptRequest(view, request)
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        view.loadUrl(request.url.toString())
        return true
    }
}

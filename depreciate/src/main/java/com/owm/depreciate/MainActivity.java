package com.owm.depreciate;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.owm.depreciate.bean.Commodity;
import com.owm.depreciate.bean.FieldTag;
import com.owm.depreciate.manager.TagManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected WebView wvService;
    private LinearLayout llWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wvService.loadUrl("https://detail.m.tmall.com/item.htm?id=9728372215");
                new Thread() {
                    @Override
                    public void run() {
//                        parseHtml("https://detail.m.tmall.com/item.htm?id=9728372215");
                    }
                }.start();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /** 网页标题tag **/
    private static final String pageTitleTag = "title";
    /** 价格tag **/
    private static final String priceTag = "span.mui-price-integer";
    /** 价格tag **/
    private static final String priceOriginTag = "del.price-origin";
    /** 商品标题tag **/
    private static final String nameTag = "div.main";
    /** 邮费tag **/
    private static final String postageTag = "div.postage.cell";
    /** 月销量tag **/
    private static final String salesCellTag = "div.sales.cell";
    /** 发货点tag **/
    private static final String deliveryCellTag = "div.delivery.cell";
    /** 所属商店名称tag **/
    private static final String tagShopName = "div.shop-t";
    /** 所属商店log tag **/
    private static final String tagShopLogo = "div.shop-logo.cell";

    private static final String tagShowcase = "section.s-showcase";
    private static final String tagScroller = "div.scroller";


    public void parseHtml(String urlStr) {
        try {
            Document document = Jsoup.connect(urlStr).get();
            parse(urlStr, document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parse(String urlStr, Document document) {
        Commodity commodity = new Commodity();
        commodity.url = urlStr;

        List<FieldTag> tagList = TagManager.getInstance().getFieldTagList();
        if (tagList == null) {
            return;
        }
        //结果用json保存
        JSONObject jsonObject = new JSONObject();
        //循环获取属性
        for (int i = 0; i < tagList.size(); i++) {
            FieldTag fieldTag = tagList.get(i);
            if (fieldTag == null) {
                continue;
            }
            List<FieldTag.SelectTag> selectTagList = fieldTag.selectTagList;
            if (selectTagList == null) {
                continue;
            }

            String value;
            List<String> valueList = new ArrayList<>();
            //尝试获取属性值
            for (int j = 0; j < selectTagList.size(); j++) {
                valueList.clear();
                value = "";
                FieldTag.SelectTag selectTag = selectTagList.get(j);
                if (selectTag == null) {
                    continue;
                }
                List<String> selectList = selectTag.selectList;
                if (selectList == null) {
                    continue;
                }
                Elements select = null;
                //循环获取需要的html tag
                for (int k = 0; k < selectList.size(); k++) {
                    if (k == 0) {
                        select = document.select(selectList.get(k));
                    } else if (select != null){
                        select = select.select(selectList.get(k));
                    }
                }
                if (select == null) {
                    continue;
                }
                //获取html tag的值
                if (fieldTag.isList == 1) {//改属性值是数组
                    if (select.size() > 0) {
                        for (int k = 0; k < select.size(); k++) {
                            //获取html tag 的 attr字段值
                            if (selectTag.attr != null && !selectTag.attr.isEmpty()) {
                                for (int l = 0; l < selectTag.attr.size(); l++) {
                                    value = select.get(k).attr(selectTag.attr.get(l));
                                    if (!TextUtils.isEmpty(value)) {
                                        break;
                                    }
                                }
                            } else {
                                value = select.get(k).text();
                            }
                            valueList.add(value);
                        }
                        //保存属性值
                        if (valueList.size() > 0) {
                            JSONArray jsonArray = new JSONArray();
                            for (int k = 0; k < valueList.size(); k++) {
                                String strValue = valueList.get(k);
                                if (strValue.startsWith("//") || strValue.startsWith("\\/\\/")) {
                                    strValue = "http:" + strValue;
                                }
                                jsonArray.put(strValue);
                            }
                            try {
                                jsonObject.putOpt(fieldTag.fieldName, jsonArray);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                } else {
                    if (selectTag.attr != null && !selectTag.attr.isEmpty()) {
                        for (int l = 0; l < selectTag.attr.size(); l++) {
                            value = select.attr(selectTag.attr.get(l));
                            if (!TextUtils.isEmpty(value)) {
                                break;
                            }
                        }
                    } else {
                        value = select.text();
                    }
                    if (!TextUtils.isEmpty(value)) {
                        try {
                            if (value.startsWith("//") || value.startsWith("\\/\\/")) {
                                value = "http:" + value;
                            }
                            jsonObject.putOpt(fieldTag.fieldName, value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
        int length = jsonObject.length();
    }
//
//    private void parse(String urlStr, Document document) {
//        Commodity commodity = new Commodity();
//        commodity.url = urlStr;
//
//        //获取标题
//        commodity.title = document.head().getElementsByTag(pageTitleTag).text();
//        //获取商品标题
//        Elements nameElements = document.select(nameTag);
//        Elements nameH1 = nameElements.select("h1");
//        commodity.name = nameH1.text();
//
//        //获取价格
//        Elements priceElements = document.select(priceTag);
//        if(priceElements != null && !priceElements.isEmpty()) {
//            String price = priceElements.get(0).text();
//            commodity.firstPrice = price;
//            commodity.currentPrice = price;
//        }
//        //原价
//        commodity.originPrice = document.select(priceOriginTag).text();
//
//        //获取快递费用
//        commodity.postage = document.select(postageTag).text();
//        //发货点
//        commodity.deliveryCell = document.select(deliveryCellTag).text();
//        //月销量
//        commodity.salesCell = document.select(salesCellTag).text();
//
//        //获取店铺名字
//        commodity.shopName = document.select(tagShopName).text();
//        //获取店铺logo url
//        commodity.shopLogo = document.select(tagShopLogo).select("img").attr("src");
//
//        Elements showcaseList = document.select(tagShowcase).select("img");
//        if(showcaseList != null) {
//            for(int i = 0; i < showcaseList.size(); i++) {
//                commodity.showcase.add(showcaseList.get(i).attr("src"));
//            }
//        }
//        Elements scrollerList = document.select(tagScroller).select("img");
//        if(scrollerList != null) {
//            for(int i = 0; i < scrollerList.size(); i++) {
//                commodity.showcase.add(scrollerList.get(i).attr("src"));
//            }
//        }
//    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            wvService.loadUrl("javascript:window.local_obj.showSource('<html>'+ document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        }
    };

    private void initView() {
        llWeb = (LinearLayout) findViewById(R.id.ll_web);
        wvService = new WebView(this);
        wvService.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        wvService.setHorizontalScrollBarEnabled(false);
        wvService.setVerticalScrollBarEnabled(false);
        llWeb.addView(wvService);
        wvService.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        wvService.getSettings().setDomStorageEnabled(true);

        WebSettings webSettings = wvService.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);


        // 获取地理位置权限
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationDatabasePath(getFilesDir().getPath());
        // 设置页面自适应webview
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // 从Lollipop(21)开始WebView默认不允许混合模式，https当中不能加载http资源，需要设置开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        wvService.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wvService.removeCallbacks(runnable);
                wvService.postDelayed(runnable, 1000);
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return !(url.contains("http") || url.contains("https") || url.startsWith("tel"));
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            private void launchOtherApp(String url, String urlPrefix) {
            }
        });

        wvService.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            }
        });
    }

    /**
     *  android sdk api >= 17 时需要加@JavascriptInterface
     * @author fei
     *
     */
    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(final String html) {
            new Thread(){
                @Override
                public void run() {
//                            System.out.println("====>html="+html);
                    Document parse = Jsoup.parse(html);
                    parse("123", parse);
                }
            }.start();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

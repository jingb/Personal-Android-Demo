package com.jingb.application.newslistdemo;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jingb.application.R;

import java.util.ArrayList;
import java.util.List;

public class NewsTitleFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView newsTitleListView;

    private List<News> newsList;

    private NewsAdapter newsAdapter;

    private boolean isTwoPanel;

    int i = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        newsList = getNews();
        newsAdapter = new NewsAdapter(context, R.layout.news_item, newsList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        newsList = getNews();
        newsAdapter = new NewsAdapter(activity, R.layout.news_item, newsList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.news_content) != null) {
            isTwoPanel = true;
        } else {
            isTwoPanel = false;
        }
    }

    private List<News> getNews() {
        List<News> list = new ArrayList<>();
        for (int i=1; i<=55; i++) {
            News news = new News();
            news.setTitle("title" + i);
            news.setContent(this.content);
            list.add(news);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = newsList.get(position);
        if (isTwoPanel) {
            NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().
                    findFragmentById(R.id.news_content_fragment);
            newsContentFragment.inflateNews(news.getTitle(), news.getContent());
        } else {
            NewsContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title_frag, container, false);
        newsTitleListView = (ListView) view.findViewById(R.id.news_title_list_view);
        newsTitleListView.setAdapter(newsAdapter);
        newsTitleListView.setOnItemClickListener(this);
        return view;
    }

    static String content = "粉丝名称为“Ki Aile”，是由宋仲基的“Ki”与法文的翅膀“Aile”结合，意思是当他的翅膀让他展翅高飞。1985年主演的电影《狼族少年》上映，票房突破700万观影人次，刷新了韩国爱情片的最高票房纪录。2008年出演电影《霜花店》进入演艺圈，之后在电视剧、电影、综艺主持多方发展。2009年在音乐节目Music Bank中担任主持，后在艺能节目《Running》中担任固定嘉宾（E01——E41）。2012年接拍KBS水木剧《善良的男人》，在KBS演技大赏中获得最佳男演员奖、网络人气奖及最佳情侣奖。宋仲基于2013年8月27日入伍，已于2015年5月26日退伍。退伍后选择KBS2电视剧《太阳的后裔》作为回归作品。";


}

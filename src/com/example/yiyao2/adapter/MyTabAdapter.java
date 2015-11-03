package com.example.yiyao2.adapter;

import com.example.yiyao2.R;
import com.example.yiyao2.fragment.CheckFragment;
import com.example.yiyao2.fragment.CookFragment;
import com.example.yiyao2.fragment.DrugFragment;
import com.example.yiyao2.fragment.LoreFragment;
import com.example.yiyao2.fragment.NewsFragment;
import com.example.yiyao2.fragment.SymptomFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MyTabAdapter extends FragmentPagerAdapter
{

	public static String[] TITLES = new String[]
	{ "健康资讯", "健康知识", "药品直达", "健康食谱", "症状查找","项目检查" };

	public MyTabAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0)
	{
		switch (arg0) {
		case 0:
			NewsFragment newsFragment = new NewsFragment();
			return newsFragment;
		case 1:
			LoreFragment loreFragment = new LoreFragment();
			return loreFragment;
		case 2:
			DrugFragment drugFragment = new DrugFragment();
			return drugFragment;
		case 3:
			CookFragment cookFragment = new CookFragment();
			return cookFragment;
		case 4:
			SymptomFragment symptomFragment = new SymptomFragment();
			return symptomFragment;
		case 5:
			CheckFragment checkFragment = new CheckFragment();
			return checkFragment;
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount()
	{
		return TITLES.length;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return TITLES[position];
	}

}

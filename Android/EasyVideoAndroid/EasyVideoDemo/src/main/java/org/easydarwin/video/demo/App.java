package org.easydarwin.video.demo;

import org.easydarwin.video.recoder.core.EasyVideoRecorder;
import org.easydarwin.video.render.core.EasyVideoRender;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CrashReport.initCrashReport(getApplicationContext(), "900025989", false);
		Bugly.init(getApplicationContext(), "900025989", false);
		
		EasyVideoRecorder.getInstance().init(this).regist(getRecorderKey());
		EasyVideoRender.getInstance().init(this).regist(getRenderKey());

		initImageLoader();
	}

	/**
	 * 获取密钥 ，正式环境中 应妥善保管密钥以免被盗取,并保证密钥正确，不正确的密钥将导致崩溃
	 * 
	 * @return key
	 */
	private String getRecorderKey() {
		return "WWplOS2JreHRWbWhqTTJ4cldWaEtNMkZYTkhWa2JXeHJXbGM0ZFZwSFZuUmlNRUY0VGtSak1VMXFWWGhOYWtGM1VVUkZid1Q3S"; //测试key
	}

	private String getRenderKey() {
		return "WW4pOS2JreHRWbWhqTTJ4cldWaEtNMkZYTkhWa2JXeHJXbGM0ZFZwSFZuUmlNRUY0VGtSak1VMXFWWGhOYWtGM1VVUkpOQTU2Y"; //测试key
	}

	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(config);
	}

}

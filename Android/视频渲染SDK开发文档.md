# EasyVideoRender SDK 文档


## 依赖准备
鉴于Android Studio的普及趋势，EasyVideoRender 将只支持 Android Studio，提供aar形式依赖，具体参照 EasyVideoDemo。

* 将 `EasyVideoCommonLib-release.aar` 拷贝到 项目`/libs` 下
* 将 `EasyVideoRenderLib-release.aar` 拷贝到 项目`/libs` 下
* 在项目`/build.gradle` 添加 aar 依赖。 （注意：`EasyVideoRenderLib` 中使用了 `universal-image-loader`以及`android-support-v4`，请也添加这两个依赖）

> e.g:

  	dependencies {
	    compile(name: 'EasyVideoRenderLib-release', ext: 'aar')
	    compile(name: 'EasyVideoCommonLib-release', ext: 'aar')
		compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
		compile "com.android.support:support-v4:22.2.1"
	}

## 初始化
* 在 `Application` 中初始化 `EasyVideoRender` 并传入`key`进行注册   
* `EasyVideoRender中使用了` `universalimageloader`，所以务必也要初始化 `universalimageloader`，`如果你的项目也使用了universalimageloader`，请注意版本一致防止冲突。

> e.g:

	public class App extends Application {
	
		@Override
		public void onCreate() {
			super.onCreate();
			EasyVideoRender.getInstance().init(this).regist(getKey());
			initImageLoader();
			copyTestVideo();
		}
	
		/**
		 * 获取密钥 ，正式环境中 应妥善保管密钥以免被盗取,并保证密钥正确，不正确的密钥将导致崩溃
		 * 
		 * @return key
		 */
		private String getKey() {
			return "WWpOS22JreHRWbWhqTTJ4cldWaEtNMkZYTkhWa2JXeHJXbGM0ZFdOdFZuVmFSMVo1VEcxU2JHSlhPVUZOVkZFelRsUkpNVTFVU1hkTlJVRjVkZzU6n"; //测试key
		}
	
		private void initImageLoader() {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
			ImageLoader.getInstance().init(config);
		}
	}

## 准备视频渲染材料

将视频渲染资源拷贝至app 的 `assets` 目录中
  
* common 通用资源目录，包括水印等 
* filter 滤镜资源目录     
* frame 相框资源目录    
* music 音乐资源目录   
* heme MV资源目录

## 设置参数

		    
EasyVideoRecorder 的通用参数通过 RenderConfig 进行设置，支持的参数将不断完善。  
> e.g:

	RenderConfig config = RenderConfig.create().setEndLogoShow(true);
	EasyVideoRender.getInstance().setRenderConfig(config)；


EasyVideoRecorder 的相关回调设置 支持的回调有cancel 事件回调，处理完毕回调，错误事件回调等  
> e.g:

	EasyVideoRender.getInstance()//
		.setRenderConfig(config)
		.setInputVideo(Environment.getExternalStorageDirectory() + "/1/test.mp4")//设置入参视频地址
		.setMoreRenderAction(EasyVideoRender.RENDER_TYPE_FILTER, "org.easydarwin.video.render.demo.DownloadFilterActivity")
		.setOnFinishListener(new OnFinishListener() {// 设置渲染结果回调函数
			/**
			 * Activity activity 渲染界面的activity
			 * String videoFile 渲染后的视频文件地址，如果是null 则渲染失败
			 */
			@Override
			public void onFinish(Activity activity, String videoFile) {
				if (videoFile == null) {
					Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent(getBaseContext(), VideoPlayActivity.class);
					intent.putExtra("path", videoFile);
					startActivity(intent);
					Toast.makeText(getApplicationContext(), videoFile, Toast.LENGTH_LONG).show();
				}
			}
		});

> ps：

	setMoreRenderAction(int type,String action); //设置更多按钮的响应activity 


> 例如:设置滤镜按钮中的更多按钮点击的响应activity，设置步骤如下:  

### 1: 配置响应action 

	<activity  
	    android:name="org.easydarwin.video.render.demo.DownloadFilterActivity"  
	    android:screenOrientation="portrait" >  
	    <intent-filter>  
	        <action android:name="org.easydarwin.video.render.demo.DownloadFilterActivity" />  
	        <category android:name="android.intent.category.DEFAULT" />  
	    </intent-filter>  
	</activity>

### 2: setAction 

	config.setMoreRenderAction(EasyVideoRender.RENDER_TYPE_FILTER, "org.easydarwin.video.render.demo.DownloadFilterActivity")//
	参数一为 响应类型，参数二 为 action

### 3: 回传资源包 

#####（1）回传单个资源包
	Intent in = new Intent();
	in.putExtra(EasyVideoRender.INPUT_RES_URL, "xxxxxx.zip");//回传资源包地址
	setResult(RESULT_OK, in);
	finish();


#####（2）回传资源包列表
	String res = Environment.getExternalStorageDirectory() + "/1/101.zip";
	ArrayList<String> resList=new ArrayList<>();
	resList.add(res);
	resList.add(res);
	in.putStringArrayListExtra(EasyVideoRender.INPUT_RES_URL_LIST, resList);//回传资源部本地地址列表
 	setResult(RESULT_OK, in);
	finish();

## 启动sdk
	EasyVideoRender.getInstance().start();//启动SDK

### 附：错误码
* 301 输入的视频文件为空
* 302 视频文件不存在

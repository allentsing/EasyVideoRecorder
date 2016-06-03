# EasyVideoRecorder SDK 文档 

## 依赖准备
鉴于Android Studio的普及趋势，EasyVideoRecorder 将只支持 Android Studio，提供`aar`形式依赖，具体参照 EasyVideoDemo。

* 将 `EasyVideoCommonLib-release.aar` 拷贝到 项目`/libs` 下
* 将 `EasyVideoRecorderLib-release.aar` 拷贝到 项目`/libs` 下
* 在项目`/build.gradle` 添加 `aar` 依赖 

> e.g:

  	dependencies {
	    compile(name: 'EasyVideoRecorderLib-release', ext: 'aar')
	    compile(name: 'EasyVideoCommonLib-release', ext: 'aar')
	}

## 初始化 
* EasyVideoRecorder 通过单例获取实例` EasyVideoRecorder.getInstance()`;
* `init(Context context)`  
传入context 初始化 ，EasyVideoRecorder 维持单例，因此对传入的对象需谨慎，防止内存泄露，建议传入 `ApplicationContext`

* `regist(String key)`   
传入 EasyVideoRecorder 授权的key进行注册，key和包名绑定，并且必须保持正确，错误的key将导致crash，开发者应该妥善保管key，以防别人盗用

> e.g:  

	public class App extends Application {

		@Override
		public void onCreate() {
			super.onCreate();
			EasyVideoRecorder.getInstance().init(this).regist(getKey());
		}
	
		/**
		 * 获取密钥，正式环境中 应妥善保管密钥以免被盗取,并保证密钥正确，不正确的密钥将导致crash
		 * 
		 * @return key
		 */
		private String getKey() {
 
			return "WWpOS2JqreHRWbWhqTTJ4cldWaEtNMkZYTkhWa2JXeHJXbGM0ZFdOdFZtcGlNbEpzWTJrMWExcFhNWFpSUkVVd1RucFZlVTVVUlhsTlJFSkJUVkZUTw7i";
		}
	}

## 参数配置 
		    
EasyVideoRecorder 的通用参数通过 RecorderConfig 进行设置 支持设置录制时间，录制界面参数，进度条颜色，进度条高度宽度等。支持的配置将不断更新升级。

> e.g:

	RecorderConfig config = RecorderConfig.create()//拍摄SDK的参数配置
	.setRecordTimeMax(15 * 1000)//设置拍摄的最大长度，单位毫秒
	.setRecordTimeMin(2 * 1000)
	.setPreviewSize(RecorderConfig.PREVIEW_SIZE_SMALL)//设置预览页面大小
	.setProgressPostion(RecorderConfig.PROGRESS_POSITION_BOTTOM)；// 设置进度条位置

	EasyVideoRecorder.getInstance().setRecorderConfig(config);


EasyVideoRecorder 的相关回调设置 支持的回调有cancel 事件回调，处理完毕回调，错误事件回调等

> e.g:
		
		EasyVideoRecorder.getInstance()
			.setOnErrorListener(new OnErrorListener() { //设置出错回调函数 
				/**
				 * int code 错误码
				 * String message 错误信息
				 */
				@Override
				public void onError(int code, String message) {
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				}
			})
			.setToastFactory(new ToastFactory() {//设置toast 显示函数  
	
				@Override
				public Toast create(Context arg0, String arg1) {
					return Toast.makeText(arg0, arg1, Toast.LENGTH_LONG);
				}
			})
			.setProgressDialogFactory(new ProgressDialogFactory() {//设置ProgressDialog 显示函数  
	
				@Override
				public ProgressDialog create(Context arg0) {
					return new ProgressDialog(arg0);
				}
			})
			.setOnFinishListener(new OnFinishListener() {// 设置拍摄结果回调函数
				/**
				 * Activity activity 拍摄界面的activity,请注意：这个activity 没有finish，应根据需要及时finish掉
				 * String videoFile 拍摄后的视频文件地址，如果是null 则拍摄失败
				 */
				@Override
				public void onFinish(Activity activity, String videoFile) {
					if (videoFile == null) {
						Toast.makeText(getApplicationContext(), "拍摄失败", Toast.LENGTH_LONG).show();
					} else {//拍摄成功，拿到视频文件地址
						Intent intent = new Intent(getBaseContext(), VideoPlayActivity.class);
						intent.putExtra("path", videoFile);
						startActivity(intent);
						Toast.makeText(getApplicationContext(), videoFile, Toast.LENGTH_LONG).show();
					}
				}
			});


### 启动拍摄

	EasyVideoRecorder.getInstance().start();//启动SDK

### 附：错误码

* 201 存储卡不可用
* 202 存储卡内存不足
* 203 无系统相机权限
* 204 无系统录音权限
* 205 创建文件夹失败
* 206 key无效
* 100 返回事件
* 101 录制结束返回视频


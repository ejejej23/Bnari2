package com.example.bnari;

import java.io.InputStream;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 날씨전달 어플 실습
 *
 */
public class Region extends Activity  {

	Button b;
	Spinner spinner;	//스피너
	Button getBtn;		//날씨 가져오는 버튼
	TextView text;		//날씨 뿌려주는 텍스트창
	String sCategory;	//동네
	String sTm;			//발표시각
	String [] sHour;	//예보시간(총 15개정도 받아옴 3일*5번)
	String [] sDay;		//날짜(몇번째날??)
	String [] sTemp;	//현재온도
	String [] sWdKor;	//풍향
	String [] sReh;		//습도
	String [] sWfKor;	//날씨
	
	String pondo;
	boolean isRain;
	String mrain;
	
	int data=0;	//이건 파싱해서 array로 넣을때 번지
	
	boolean bCategory;	//여긴 저장을 위한 플래그들
	boolean bTm;
	boolean bHour;
	boolean bDay;
	boolean bTemp;
	boolean bWdKor;
	boolean bReh;
	boolean bItem;
	boolean bWfKor;
	
	boolean tCategory;	//이건 text로 뿌리기위한 플래그
	boolean tTm;
	boolean tItem;
	
	Handler handler;	//핸들러
	
	String dongcode[] = {"4136025000","4127359000","1135060000","4127352000","4215066500","1171065000"
			,"5013025000","4131053000"};
	String donglist[] = {"남양주시 와부읍","안산시 선부2동","노원구 공릉2동","안산시 고잔1동","강릉시 경포동","서울 잠실본동"
			,"제주시 마라도","구리시 인창동"};
	
	
	String dong;	//최종적으로 가져다 붙일 동네코드가 저장되는 변수
	String dongname;
	
    SQLiteDatabase sqlitedb;
    DBManager dbmanager;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.region);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent it1 = getIntent();
		
		b = (Button)findViewById(R.id.back);
		
		handler=new Handler();	//스레드&핸들러처리
		spinner=(Spinner)findViewById(R.id.spinner);
		
		bCategory=bTm=bHour=bTemp=bWdKor=bReh=bDay=bWfKor=tCategory=tTm=tItem=false;	//부울상수는 false로 초기화해주자
		
		sHour=new String[20];	//예보시간(사실 15개밖에 안들어오지만 넉넉하게 20개로 잡아놓음)
		sDay=new String[20];	//날짜
		sTemp=new String[20];	//현재온도
		sWdKor=new String[20];	//풍향
		sReh=new String[20];	//습도
		sWfKor=new String[20];	//날씨
		
		spinner = (Spinner) findViewById(R.id.spinner);		//스피너 객체생성
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {	//이부분은 스피너에 나타나는 내용

		@Override
		public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {	//선택시
				dong=dongcode[position];
				dongname=donglist[position];
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {	//미선택시
				dong=dongcode[0];
				dongname=donglist[0];
				
			}
		});
        // 어댑터 객체 생성
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(	
        		this, android.R.layout.simple_spinner_item, donglist);	//어댑터를 통해 스피너에 donglist 넣어줌
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	//dropdown형식
        
        // 어댑터 설정
        spinner.setAdapter(adapter);	
		

		text=(TextView) findViewById(R.id.textView1);	//텍스트 객체생성
		getBtn=(Button) findViewById(R.id.getBtn);		//버튼 객체생성
		
		/*
		network_thread thread=new network_thread();		//스레드생성(UI 스레드사용시 system 뻗는다)
		thread.start();
	    */
	}

 public void weather(View v){
		int id = v.getId();
		
		text.setText("");	//일단 중복해서 누를경우 대비해서 내용 지워줌
		network_thread thread=new network_thread();		//스레드생성(UI 스레드사용시 system 뻗는다)
		thread.start();	//스레드 시작
	   //  startActivity(it2);		
	}
	
	
	/**
	 * 기상청을 연결하여 정보받고 뿌려주는 스레드
	 * 
	 * @author Ans
	 *
	 */
	class network_thread extends Thread{	//기상청 연결을 위한 스레드
		/**
		 * 기상청을 연결하는 스레드
		 * 이곳에서 풀파서를 이용하여 기상청에서 정보를 받아와 각각의 array변수에 넣어줌
		 * @author Ans
		 */
		public void run(){
			
			try{
				XmlPullParserFactory factory=XmlPullParserFactory.newInstance();	//이곳이 풀파서를 사용하게 하는곳
				factory.setNamespaceAware(true);									//이름에 공백도 인식
				XmlPullParser xpp=factory.newPullParser();							//풀파서 xpp라는 객체 생성
				
				String weatherUrl="http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone="+dong;	//이곳이 기상청URL
				URL url=new URL(weatherUrl);		//URL객체생성
				InputStream is=url.openStream();	//연결할 url을 inputstream에 넣어 연결을 하게된다.
				xpp.setInput(is,"UTF-8");			//이렇게 하면 연결이 된다. 포맷형식은 utf-8로
				
				int eventType=xpp.getEventType();	//풀파서에서 태그정보를 가져온다.
					
				while(eventType!=XmlPullParser.END_DOCUMENT){	//문서의 끝이 아닐때
					
					switch(eventType){
					case XmlPullParser.START_TAG:	//'<'시작태그를 만났을때
						
						if(xpp.getName().equals("category")){	//태그안의 이름이 카테고리일떄 (이건 동네이름이 나온다)
							bCategory=true;	
							
						} if(xpp.getName().equals("tm")){		//발표시각정보
							bTm=true;
						
						} if(xpp.getName().equals("hour")){		//예보시간
							bHour=true;
							
						} if(xpp.getName().equals("day")){		//예보날(오늘 내일 모레)
							bDay=true;
							
						} if(xpp.getName().equals("temp")){		//예보시간기준 현재온도
							bTemp=true;
							
						} if(xpp.getName().equals("wdKor")){	//풍향정보
							bWdKor=true;
							
						} if(xpp.getName().equals("reh")){		//습도정보
							bReh=true;
							
						} if(xpp.getName().equals("wfKor")){	//날씨정보(맑음, 구름조금, 구름많음, 흐림, 비, 눈/비, 눈)
							bWfKor=true;
							
						}
						
						break;
					
					case XmlPullParser.TEXT:	//텍스트를 만났을때
												//앞서 시작태그에서 얻을정보를 만나면 플래그를 true로 했는데 여기서 플래그를 보고
												//변수에 정보를 넣어준 후엔 플래그를 false로~
						if(bCategory){				//동네이름
							sCategory=xpp.getText();
							bCategory=false;
						} if(bTm){					//발표시각
							sTm=xpp.getText();	
							bTm=false;	
						}  if(bHour){				//예보시각			
							sHour[data]=xpp.getText();
							bHour=false;
						}  if(bDay){				//예보날짜
							sDay[data]=xpp.getText();
							bDay=false;
						}  if(bTemp){				//현재온도
							sTemp[data]=xpp.getText();
							pondo=sTemp[0];
							bTemp=false;
						}  if(bWdKor){				//풍향
							sWdKor[data]=xpp.getText();
							bWdKor=false;
						}  if(bReh){				//습도
							sReh[data]=xpp.getText();
							bReh=false;
						} if(bWfKor){				//날씨
							sWfKor[data]=xpp.getText();
							//sWfKor[0]="안녕";
							isRain = sWfKor[data].equals("비");
							mrain = sWfKor[0];
							bWfKor=false;
							}
						break;
						
					case XmlPullParser.END_TAG:		//'>' 엔드태그를 만나면 (이부분이 중요)
						
						if(xpp.getName().equals("item")){	//태그가 끝나느 시점의 태그이름이 item이면(이건 거의 문서의 끝
							tItem=true;						//따라서 이때 모든 정보를 화면에 뿌려주면 된다.
							view_text();					//뿌려주는 곳~
						} if(xpp.getName().equals("tm")){	//이건 발표시각정보니까 1번만나오므로 바로 뿌려주자
							tTm=true;
							view_text();
						} if(xpp.getName().equals("category")){	//이것도 동네정보라 바로 뿌려주면 됨
							tCategory=true;
							view_text();
						} if(xpp.getName().equals("data")){	//data태그는 예보시각기준 예보정보가 하나씩이다.
							data++;							//즉 data태그 == 예보 개수 그러므로 이때 array를 증가해주자
						}
						break;
					}
					eventType=xpp.next();	//이건 다음 이벤트로~
				}
				
				
				
			}catch(Exception e){
				e.printStackTrace();
				}
		}
		
		private Context getAndroidContent() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * 이 부분이 뿌려주는곳 
		 * 뿌리는건 핸들러가~
		 * @author Ans
		 */
		private void view_text(){
			
			handler.post(new Runnable() {	//기본 핸들러니깐 handler.post하면됨
				
				@Override
				public void run() {
					
					if(tCategory){	//동네이름 들어왔다
						text.setText(text.getText()+"지역:"+sCategory+"\n");
						tCategory=false;
					}if(tTm){		//발표시각 들어왔다
						text.setText(text.getText()+"발표시각:"+sTm+"\n\n");
						tTm=false;
					}if(tItem){		//문서를 다 읽었다
						
						for(int i=0;i<data;i++){	//array로 되어있으니 for문으로
							if(sDay[i]!=null){		//이건 null integer 에러 예방을 위해(String은 null이 가능하지만intger는 안되니깐)
								if(Integer.parseInt(sDay[i])==0){	//발표시각이 0이면 오늘 
								text.setText(text.getText()+"날짜:"+"오늘"+"\n");
								}else if(Integer.parseInt(sDay[i])==1){	//1이면 내일
									text.setText(text.getText()+"날짜:"+"내일"+"\n");
								}else if(Integer.parseInt(sDay[i])==2){	//2이면 모레
									text.setText(text.getText()+"날짜:"+"모레"+"\n");
								}
							}
							text.setText(text.getText()+"예보시간:"+sHour[i]+"시\n");			//예보시간
							text.setText(text.getText()+"현재시간온도:"+sTemp[i]+"도"+"\n");	//온도
							text.setText(text.getText()+"풍향:"+sWdKor[i]+"풍"+"\n");			//풍향
							text.setText(text.getText()+"습도:"+sReh[i]+"%"+"\n");			//습도
							text.setText(text.getText()+"날씨:"+sWfKor[i]+"\n\n\n");			//날씨
						}
						tItem=false;
						data=0;		//다음에 날씨를 더가져오게 되면 처음부터 저장해야겠지?
						
					}
				}
			});	
		}
	}
	
	//save btn	
	public void back(View v){
		int i = v.getId();
	
		 Intent it2 = new Intent(this,MainActivity.class);
		 
		 it2.putExtra("dong",dongname);
		 it2.putExtra("ondo",pondo);
		 it2.putExtra("rain", mrain);

			try {
				dbmanager = new DBManager(this);
				sqlitedb = dbmanager.getWritableDatabase();

				sqlitedb.execSQL("UPDATE myset SET region='"+dongname+"' where no=1;");
				
				for(int j=0;j<15;j++){
				sqlitedb.execSQL("insert into temp(no,date,time,ondo,weather) values('"+j+"'+1,'"+sDay[j]+"','"+sHour[j]+"','"+sTemp[j]+"','"+sWfKor[j]+"');");
				}
				sqlitedb.close();
				dbmanager.close();
				
				
			} catch (SQLiteException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}

		 
		 startActivity(it2);
		 finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ex, menu);
		 return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if(id==android.R.id.home){
			Intent i = new Intent(this,MainActivity.class);
			startActivity(i);
			finish();
			
		//	return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
}

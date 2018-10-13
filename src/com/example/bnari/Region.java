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
 * �������� ���� �ǽ�
 *
 */
public class Region extends Activity  {

	Button b;
	Spinner spinner;	//���ǳ�
	Button getBtn;		//���� �������� ��ư
	TextView text;		//���� �ѷ��ִ� �ؽ�Ʈâ
	String sCategory;	//����
	String sTm;			//��ǥ�ð�
	String [] sHour;	//�����ð�(�� 15������ �޾ƿ� 3��*5��)
	String [] sDay;		//��¥(���°��??)
	String [] sTemp;	//����µ�
	String [] sWdKor;	//ǳ��
	String [] sReh;		//����
	String [] sWfKor;	//����
	
	String pondo;
	boolean isRain;
	String mrain;
	
	int data=0;	//�̰� �Ľ��ؼ� array�� ������ ����
	
	boolean bCategory;	//���� ������ ���� �÷��׵�
	boolean bTm;
	boolean bHour;
	boolean bDay;
	boolean bTemp;
	boolean bWdKor;
	boolean bReh;
	boolean bItem;
	boolean bWfKor;
	
	boolean tCategory;	//�̰� text�� �Ѹ������� �÷���
	boolean tTm;
	boolean tItem;
	
	Handler handler;	//�ڵ鷯
	
	String dongcode[] = {"4136025000","4127359000","1135060000","4127352000","4215066500","1171065000"
			,"5013025000","4131053000"};
	String donglist[] = {"�����ֽ� �ͺ���","�Ȼ�� ����2��","����� ����2��","�Ȼ�� ����1��","������ ������","���� ��Ǻ���"
			,"���ֽ� ����","������ ��â��"};
	
	
	String dong;	//���������� ������ ���� �����ڵ尡 ����Ǵ� ����
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
		
		handler=new Handler();	//������&�ڵ鷯ó��
		spinner=(Spinner)findViewById(R.id.spinner);
		
		bCategory=bTm=bHour=bTemp=bWdKor=bReh=bDay=bWfKor=tCategory=tTm=tItem=false;	//�ο����� false�� �ʱ�ȭ������
		
		sHour=new String[20];	//�����ð�(��� 15���ۿ� �ȵ������� �˳��ϰ� 20���� ��Ƴ���)
		sDay=new String[20];	//��¥
		sTemp=new String[20];	//����µ�
		sWdKor=new String[20];	//ǳ��
		sReh=new String[20];	//����
		sWfKor=new String[20];	//����
		
		spinner = (Spinner) findViewById(R.id.spinner);		//���ǳ� ��ü����
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {	//�̺κ��� ���ǳʿ� ��Ÿ���� ����

		@Override
		public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {	//���ý�
				dong=dongcode[position];
				dongname=donglist[position];
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {	//�̼��ý�
				dong=dongcode[0];
				dongname=donglist[0];
				
			}
		});
        // ����� ��ü ����
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(	
        		this, android.R.layout.simple_spinner_item, donglist);	//����͸� ���� ���ǳʿ� donglist �־���
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	//dropdown����
        
        // ����� ����
        spinner.setAdapter(adapter);	
		

		text=(TextView) findViewById(R.id.textView1);	//�ؽ�Ʈ ��ü����
		getBtn=(Button) findViewById(R.id.getBtn);		//��ư ��ü����
		
		/*
		network_thread thread=new network_thread();		//���������(UI ��������� system ���´�)
		thread.start();
	    */
	}

 public void weather(View v){
		int id = v.getId();
		
		text.setText("");	//�ϴ� �ߺ��ؼ� ������� ����ؼ� ���� ������
		network_thread thread=new network_thread();		//���������(UI ��������� system ���´�)
		thread.start();	//������ ����
	   //  startActivity(it2);		
	}
	
	
	/**
	 * ���û�� �����Ͽ� �����ް� �ѷ��ִ� ������
	 * 
	 * @author Ans
	 *
	 */
	class network_thread extends Thread{	//���û ������ ���� ������
		/**
		 * ���û�� �����ϴ� ������
		 * �̰����� Ǯ�ļ��� �̿��Ͽ� ���û���� ������ �޾ƿ� ������ array������ �־���
		 * @author Ans
		 */
		public void run(){
			
			try{
				XmlPullParserFactory factory=XmlPullParserFactory.newInstance();	//�̰��� Ǯ�ļ��� ����ϰ� �ϴ°�
				factory.setNamespaceAware(true);									//�̸��� ���鵵 �ν�
				XmlPullParser xpp=factory.newPullParser();							//Ǯ�ļ� xpp��� ��ü ����
				
				String weatherUrl="http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone="+dong;	//�̰��� ���ûURL
				URL url=new URL(weatherUrl);		//URL��ü����
				InputStream is=url.openStream();	//������ url�� inputstream�� �־� ������ �ϰԵȴ�.
				xpp.setInput(is,"UTF-8");			//�̷��� �ϸ� ������ �ȴ�. ���������� utf-8��
				
				int eventType=xpp.getEventType();	//Ǯ�ļ����� �±������� �����´�.
					
				while(eventType!=XmlPullParser.END_DOCUMENT){	//������ ���� �ƴҶ�
					
					switch(eventType){
					case XmlPullParser.START_TAG:	//'<'�����±׸� ��������
						
						if(xpp.getName().equals("category")){	//�±׾��� �̸��� ī�װ��ϋ� (�̰� �����̸��� ���´�)
							bCategory=true;	
							
						} if(xpp.getName().equals("tm")){		//��ǥ�ð�����
							bTm=true;
						
						} if(xpp.getName().equals("hour")){		//�����ð�
							bHour=true;
							
						} if(xpp.getName().equals("day")){		//������(���� ���� ��)
							bDay=true;
							
						} if(xpp.getName().equals("temp")){		//�����ð����� ����µ�
							bTemp=true;
							
						} if(xpp.getName().equals("wdKor")){	//ǳ������
							bWdKor=true;
							
						} if(xpp.getName().equals("reh")){		//��������
							bReh=true;
							
						} if(xpp.getName().equals("wfKor")){	//��������(����, ��������, ��������, �帲, ��, ��/��, ��)
							bWfKor=true;
							
						}
						
						break;
					
					case XmlPullParser.TEXT:	//�ؽ�Ʈ�� ��������
												//�ռ� �����±׿��� ���������� ������ �÷��׸� true�� �ߴµ� ���⼭ �÷��׸� ����
												//������ ������ �־��� �Ŀ� �÷��׸� false��~
						if(bCategory){				//�����̸�
							sCategory=xpp.getText();
							bCategory=false;
						} if(bTm){					//��ǥ�ð�
							sTm=xpp.getText();	
							bTm=false;	
						}  if(bHour){				//�����ð�			
							sHour[data]=xpp.getText();
							bHour=false;
						}  if(bDay){				//������¥
							sDay[data]=xpp.getText();
							bDay=false;
						}  if(bTemp){				//����µ�
							sTemp[data]=xpp.getText();
							pondo=sTemp[0];
							bTemp=false;
						}  if(bWdKor){				//ǳ��
							sWdKor[data]=xpp.getText();
							bWdKor=false;
						}  if(bReh){				//����
							sReh[data]=xpp.getText();
							bReh=false;
						} if(bWfKor){				//����
							sWfKor[data]=xpp.getText();
							//sWfKor[0]="�ȳ�";
							isRain = sWfKor[data].equals("��");
							mrain = sWfKor[0];
							bWfKor=false;
							}
						break;
						
					case XmlPullParser.END_TAG:		//'>' �����±׸� ������ (�̺κ��� �߿�)
						
						if(xpp.getName().equals("item")){	//�±װ� ������ ������ �±��̸��� item�̸�(�̰� ���� ������ ��
							tItem=true;						//���� �̶� ��� ������ ȭ�鿡 �ѷ��ָ� �ȴ�.
							view_text();					//�ѷ��ִ� ��~
						} if(xpp.getName().equals("tm")){	//�̰� ��ǥ�ð������ϱ� 1���������Ƿ� �ٷ� �ѷ�����
							tTm=true;
							view_text();
						} if(xpp.getName().equals("category")){	//�̰͵� ���������� �ٷ� �ѷ��ָ� ��
							tCategory=true;
							view_text();
						} if(xpp.getName().equals("data")){	//data�±״� �����ð����� ���������� �ϳ����̴�.
							data++;							//�� data�±� == ���� ���� �׷��Ƿ� �̶� array�� ����������
						}
						break;
					}
					eventType=xpp.next();	//�̰� ���� �̺�Ʈ��~
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
		 * �� �κ��� �ѷ��ִ°� 
		 * �Ѹ��°� �ڵ鷯��~
		 * @author Ans
		 */
		private void view_text(){
			
			handler.post(new Runnable() {	//�⺻ �ڵ鷯�ϱ� handler.post�ϸ��
				
				@Override
				public void run() {
					
					if(tCategory){	//�����̸� ���Դ�
						text.setText(text.getText()+"����:"+sCategory+"\n");
						tCategory=false;
					}if(tTm){		//��ǥ�ð� ���Դ�
						text.setText(text.getText()+"��ǥ�ð�:"+sTm+"\n\n");
						tTm=false;
					}if(tItem){		//������ �� �о���
						
						for(int i=0;i<data;i++){	//array�� �Ǿ������� for������
							if(sDay[i]!=null){		//�̰� null integer ���� ������ ����(String�� null�� ����������intger�� �ȵǴϱ�)
								if(Integer.parseInt(sDay[i])==0){	//��ǥ�ð��� 0�̸� ���� 
								text.setText(text.getText()+"��¥:"+"����"+"\n");
								}else if(Integer.parseInt(sDay[i])==1){	//1�̸� ����
									text.setText(text.getText()+"��¥:"+"����"+"\n");
								}else if(Integer.parseInt(sDay[i])==2){	//2�̸� ��
									text.setText(text.getText()+"��¥:"+"��"+"\n");
								}
							}
							text.setText(text.getText()+"�����ð�:"+sHour[i]+"��\n");			//�����ð�
							text.setText(text.getText()+"����ð��µ�:"+sTemp[i]+"��"+"\n");	//�µ�
							text.setText(text.getText()+"ǳ��:"+sWdKor[i]+"ǳ"+"\n");			//ǳ��
							text.setText(text.getText()+"����:"+sReh[i]+"%"+"\n");			//����
							text.setText(text.getText()+"����:"+sWfKor[i]+"\n\n\n");			//����
						}
						tItem=false;
						data=0;		//������ ������ ���������� �Ǹ� ó������ �����ؾ߰���?
						
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

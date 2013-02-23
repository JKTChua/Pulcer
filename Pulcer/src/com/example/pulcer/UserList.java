package com.example.pulcer;

import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.me.pulcer.adapter.UserAdapter;
import com.me.pulcer.common.AsyncActivity;
import com.me.pulcer.common.PApp;
import com.me.pulcer.common.SwipeDetector;
import com.me.pulcer.common.SwipeDetector.Action;
import com.me.pulcer.entity.UserDetail;
import com.me.pulcer.parser.GetUserListParser;
import com.me.pulcer.parser.ReminderStatusParser;
import com.me.pulcer.util.Util;
import com.me.pulcer.web.AsyncCall;
import com.me.pulcer.web.AsyncCallListener;
import com.me.pulcer.web.RequestMethod;
import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.the9tcat.hadi.DefaultDAO;


@EActivity(R.layout.userlist)
public class UserList extends AsyncActivity
{
	
	@ViewById
	ListView userList;
	
	@App
	PApp app;
	
	boolean isToLoadData=true;
	UserAdapter adapter;
	
	GetUserListParser userData;
	SwipeDetector swipeDetector;
	
	private final int ADD_USER=1000;
	
	
	@AfterViews
	void loadView()
	{
		//TODO Menu functionality removed as per jermy PLAD-15 
//		initMenu();
		initTitle();
		title.setText(getString(R.string.title_user));
		menuBtn.setVisibility(View.GONE);
		backBtn.setVisibility(View.VISIBLE);
		swipeDetector = new SwipeDetector();
		userList.setOnTouchListener(swipeDetector);
	}
	
	@Click
	protected void right_btn()
	{
		showDialog();
	}
	
	@Click
	protected void back_btn()
	{
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==ADD_USER && resultCode==RESULT_OK && data!=null)
		{

		}
	}
	
	public void showDialog()
	{
		if(Util.chkInternet(this)){
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.user_dialog);
			dialog.findViewById(R.id.dlg_manage_user_btn).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent=new Intent(UserList.this, AddUser_.class);
					intent.putExtra("MODE", PApp.MANAGE_USER);
					startActivityForResult(intent, ADD_USER);
					dialog.dismiss();
				}
			});
			
			dialog.findViewById(R.id.dlg_shareduser_btn).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					Intent intent=new Intent(UserList.this, AddUser_.class);
					intent.putExtra("MODE", PApp.SHARED_USER);
					startActivity(intent);
					dialog.dismiss();
				}
			});
			dialog.show();
		}else{
			showMessageDialog(getString(R.string.dlg_msg_no_internet));
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if(isToLoadData)
		{
			if(Util.chkInternet(UserList.this))
			{
				loadDataFromSerVer();
			}else{
				
			}
		}
	}
	
	private void setUpView()
	{	
		if(userData != null && userData.data != null && userData.data.userList != null)
		{
			adapter=new UserAdapter(this, userData.data.userList,app.imageLoader);
			userList.setAdapter(adapter);
			userList.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
				{
					if(swipeDetector.swipeDetected())
					{
						if(Util.chkInternet(UserList.this))
						{
							infoLog("swipeDetected:"+swipeDetector.getAction()+" isSelected:"+arg1.isSelected());
							if( swipeDetector.getAction()==Action.RL && adapter.getItem(position).isOpen==false)
							{
								adapter.getItem(position).isToAnimateOpen=true;
								adapter.notifyDataSetChanged();
							}
							else if(swipeDetector.getAction()==Action.LR && adapter.getItem(position).isOpen)
							{
								adapter.getItem(position).isToAnimateClose=true;
								adapter.notifyDataSetChanged();
							}
						}
						else
						{
							showMessageDialog(getString(R.string.dlg_msg_no_internet));
						}
					}
					else
					{
						if(Util.chkInternet(UserList.this))
						{
							Intent intent=new Intent(UserList.this, ManageItems_.class);
							intent.putExtra("USER_ID", userData.data.userList.get(position).id);
							intent.putExtra("MANAGE_ID", userData.data.userList.get(position).manageById);
							startActivity(intent);
						}
						else
						{
							showMessageDialog(getString(R.string.dlg_msg_no_internet));
						}
					}
				}
			});
		}
		else
		{
			infoError("Error while setupView data is null");
		}
	}
	
	public void editUser(int position)
	{
		if(userData!=null && userData.data!=null && userData.data.userList!=null)
		{
			if(userData.data.userList.get(position)!=null)
			{
				if(userData.data.userList.get(position).manageById==getIntPref(PApp.Pref_UserID))
				{
					Intent intent=new Intent(UserList.this, AddUser_.class);
					intent.putExtra("MODE", PApp.MANAGE_USER_EDIT);
					intent.putExtra("USER_INFO", userData.data.userList.get(position));
					startActivity(intent);
				}
				else
				{
					Toast.makeText(UserList.this, "You cannot edit shared user", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	
	public void removeUser(final int position)
	{
		try{
			if(userData!=null && userData.data!=null && userData.data.userList!=null){
				final int userId=userData.data.userList.get(position).id;
				if(getIntPref(PApp.Pref_UserID)==userId){
					Toast.makeText(UserList.this, getString(R.string.validate_loggedin_user), Toast.LENGTH_LONG).show();
				}
				else
				{
					infoLog("Remove User::"+position+" userID:"+userId);
					if(userId!=0)
					{
						showConfirmationDialog(getString(R.string.dlg_cnf_delete_user), new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								removeUser(userId, position);
							}
						});
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void loadDataFromSerVer()
	{
		asyncCall=new AsyncCall(this);
		asyncCall.setUrl(PApp.WEB_SERVICE_URL+"getuserlist");
		asyncCall.setMessage(getString(R.string.progress_title));
		asyncCall.addParam("access_token",getStrPref(PApp.Pref_AccessToken));
		asyncCall.setAsyncCallListener(new AsyncCallListener()
		{
			@Override
			public void onResponseReceived(String str) {
				
				infoLog("userResponse:"+str);
				try{
					Gson gson=new Gson();
					userData=gson.fromJson(str, GetUserListParser.class);
					
					if(validateResponse(userData)){
						setUpView();
					}else{
						showErrorMessage(userData.errorMsg, getString(R.string.dialog_title));
					}
				}catch(Exception e){
					infoError("", e);
				}
			}
			@Override
			public void onErrorReceived(String str)
			{
				showErrorMessage(str, getString(R.string.dialog_title));
			}
		});
		asyncCall.execute();
	}
	
	@Background
	protected void loadFromDb()
	{
		DefaultDAO dao=new DefaultDAO(UserList.this);
		String args[]={""+0};
		try{
			List<UserDetail> userList=(List<UserDetail>) dao.select(UserDetail.class, false, "id>", args, null, null, null, null);
			
		}catch(Exception e){
			infoLog("Error while retriving data from db:"+e);
		}
	}
	

	
	private void removeUser(int userId,final int position)
	{
		asyncCall=new AsyncCall(this);
		asyncCall.webMethod=RequestMethod.POST;
		asyncCall.setUrl(PApp.WEB_SERVICE_URL+"deletemanageduser");
		asyncCall.setMessage(getString(R.string.pdlg_msg_removing_user));
		asyncCall.addParam("access_token",getStrPref(PApp.Pref_AccessToken));
		asyncCall.addParam("user_id",""+userId);
		asyncCall.setAsyncCallListener(new AsyncCallListener()
		{
			@Override
			public void onResponseReceived(String str)
			{
				infoLog("delete user response::"+str);
				try{
					Gson gson=new Gson();
					ReminderStatusParser statusParser= gson.fromJson(str, ReminderStatusParser.class);
					if(validateResponse(statusParser))
					{
						if(Util.validateStr(statusParser.data.operation))
						{
							showSuccessDialog(R.drawable.item_removed, R.drawable.msg_user_removed);
							userData.data.userList.remove(position);
							adapter.notifyDataSetChanged();
							app.isRefresUserData=true;
						}
					}else{
						showErrorMessage(statusParser.errorMsg, getString(R.string.dialog_title));
					}
				}catch(Exception e){
					infoError("", e);
				}
				
			}
			@Override
			public void onErrorReceived(String str)
			{
				showErrorMessage(str, getString(R.string.dialog_title));
			}
		});
		asyncCall.execute();
	}
	
	
	
	/*//TODO Menu functionality removed as per jermy PLAD-15
	View.OnClickListener  addUserClick=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(isMenuOut){
				toggleMenu();
			}
		}
	};*/
	
	//TODO Menu functionality removed as per jermy PLAD-15
	/*public OnItemClickListener familyListCLick=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int position,long arg3) {
			Intent intent=new Intent();
			intent.putExtra("USER_ID",response.data.familyList.get(position).id);
			setResult(1001,intent);
			finish();
		}
	};*/
	
	////TODO Menu functionality removed as per jermy PLAD-15
	View.OnClickListener userClick=new View.OnClickListener() {
		
		@Override
		public void onClick(View v)
		{
			try{
				Intent intent=new Intent();
				intent.putExtra("USER_ID",getIntPref(PApp.Pref_UserID));
				setResult(1001,intent);
				finish();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};
	/*@Click
	void menu_btn(){
		toggleMenu();
	}*/

}





/*
 * 
 * @Override
	protected void onResume() {
		super.onResume();
		if(isToLoadData){
			loadDataFromSerVer();
			current_user_container.setOnClickListener(userClick);
			//TODO Menu functionality removed as per jermy PLAD-15
			response=Util.deSerialiseUSerData(UserList.this);
			AddUserBtn.setOnClickListener(addUserClick);
			if(response.data.familyList!=null){
				familyAdapter=new FamilyAdapter(UserList.this, response.data.familyList);
				family_list.setAdapter(familyAdapter);
				family_list.setOnItemClickListener(familyListCLick);
			}
			setUpMenuView();
		}
	}
 */






//Bundle b=data.getExtras();
//Need to chaekc This
/*if(b!=null && b.get("USER_DETAIL")!=null){
	localList.add((UserDetail) b.get("USER_DETAIL"));
	loadData();
}*/


/*private void loadDataFromSerVer(){
asyncCall=new AsyncCall(this);

asyncCall.setUrl(PApp.WEB_SERVICE_URL+"getuserlist");
asyncCall.setMessage("Retriving data please wait...");
asyncCall.addParam("access_token",getStrPref(PApp.Pref_AccessToken));

asyncCall.setAsyncCallListener(new AsyncCallListener() {

	@Override
	public void onResponseReceived(String str) {
		infoLog("UserListResponse:"+ str);
		
		Gson gson=new Gson();
		//LoginParser response=gson.fromJson(str, LoginParser.class);
		
		if(validateResponse(response)){
			
		}else{
			infoLog("Login failed:"+response.errorMsg);
			showErrorMessage(response.errorMsg, "Error");
		}
		
	}
	
	@Override
	public void onErrorReceived(String str) {
		infoLog("Error:"+str);	
		showErrorMessage(str, "Error");
	}
	
});
asyncCall.execute();
}*/
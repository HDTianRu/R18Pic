package com.TianRu.r18;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.util.regex.*;


public class jisuanqi extends Activity {
    private int[] idNum = {R.id.txt0, R.id.txt1, R.id.txt2, R.id.txt3,
            R.id.txt4, R.id.txt5, R.id.txt6, R.id.txt7, R.id.txt8, R.id.txt9};  //数字Number输入
    private int[] idCal = {R.id.txtPlus, R.id.txtMinus, R.id.txtMul, R.id.txtDiv,R.id.txtLeft,R.id.txtRight,R.id.txtDot};  //运算符
    private Button[] buttonsCal = new Button[idCal.length];
    private Button[] buttonsNum = new Button[idNum.length];
    private Button buttonEqu;   //=
    private Button buttonClear;  // AC
    private Button buttonDel;
    private EditText input ;
    private TextView output;
    private static String Text;
				private static String password;
    @Override
    /**
     * EditText为输入字符行，
     * TextView为结果。
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jisuanqi);
								ActionBar actionBar=getActionBar();
								if(actionBar != null){
												actionBar.hide();
										}
								password=spUtil.getString("password");
						input = (EditText)findViewById(R.id.input);
						input.setText("");
						input.setEnabled(false);
						output = (TextView) findViewById(R.id.output);
						output.setText("");
						buttonEqu = (Button)findViewById(R.id.txtIs);
						buttonEqu.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
											if(input.getText().toString().equals(password))
												{
													finish();
													return;
												}
											output.setText(new Calculate(input.getText().toString()).str);
										}
        });
						buttonClear = (Button) findViewById(R.id.txtClear);
						buttonClear.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
											input.setText("");
											output.setText("");
										}
        });
						buttonDel = (Button) findViewById(R.id.txtDel);

						buttonDel.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {

											if (!input.getText().toString().isEmpty() ) {
													Text = input.getText().toString();
													Text = Text.substring(0, Text.length() - 1);
													input.setText(Text);
												}
										}
        });
						/**
							* 注册单击事件
							*/
						for (int idcal = 0; idcal < idCal.length; idcal++) {
								buttonsCal[idcal] = (Button) findViewById(idCal[idcal]);
								buttonsCal[idcal].setOnClickListener(new CalOnClick(buttonsCal[idcal].getText().toString()));

							}
						for (int i = 0; i < idNum.length; i++) {
								buttonsNum[i] = (Button) findViewById(idNum[i]);
								buttonsNum[i].setOnClickListener(new NumberOnClick(buttonsNum[i].getText().toString()));
							}




					}
		private long exitTime = 0;

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event){
    if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){   
						if((System.currentTimeMillis()-exitTime)>2000){  
								Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();                                
								exitTime=System.currentTimeMillis();   
							}else{
								spUtil.getActivity().finish();
								finish();
							}
						return true;   
					}
    return super.onKeyDown(keyCode,event);
			}

    //继承OnClick接口
    class NumberOnClick implements View.OnClickListener {
						String Msg;
						/**
							*
							* @param msg 点击按钮传入字符
							*/
						public NumberOnClick(String msg) {
								Msg = msg;
							}

						@Override
						public void onClick(View v) {
								if (!output.getText().toString().equals("")) {
										input.setText("");
										output.setText("");
									}

								input.append(Msg);
							}
					}
    class CalOnClick implements View.OnClickListener{
						String Msg;
						String[] calSymbol = {"+", "-", "*", "/","."};
						public CalOnClick(String msg) {
								Msg = msg;
							}
						@Override
						public void onClick(View v) {
								if (!output.getText().toString().equals("")) {
										input.setText("");
										output.setText("");
									}
								// 检查是否运算符重复输入
								for (int i = 0; i < calSymbol.length; i++) {
										if (Msg.equals(calSymbol[i])) {
												if (input.getText().toString().split("")
																[input.getText().toString().split("").length - 1].equals(calSymbol[i])) {
														Msg = "";
													}

											}
									}
								input.append(Msg);
							}
					}

    /**
     * 运算类，返回一个String结果
     */
    public class Calculate {
						public String s1;
						StringBuilder str;

						public Calculate(String m) {
								this.s1 = m;
								try {
										eval();
									} catch (Exception e) {
										str.delete(0, str.length());
										str.append("ERROR");
									}
							}

						/**
							*中缀表达式转后缀表达式
							*
							*遍历中缀的list
							*1、数字时，加入后缀list
							*2、“(”时，压栈
							*3、 若为 ')'，则依次弹栈,把弹出的运算符加入后缀表达式中，直到出现'('；
							*4、若为运算符，对做如下处置
							*   1、如果栈为空，则压栈
							*   2、如果栈不为空:
							*     1、stack.peek().equals("(")  则压栈
							*     2、比较str和stack.peek()的优先级
							*        1、如果>,则运算符压栈
							*        2、<=的情况：当栈不为空时:
							*           1、stack.peek()是左括号，压栈
							*           2、<=,把peek加入后缀表达式，弹栈
							*           3、>，把运算符压栈，停止对栈的操作
							*    执行完栈的操作之后，还得判断:如果栈为空,运算符压栈
							*/
						public List<String> midToAfter(List<String> midList)throws EmptyStackException{
								List<String> afterList=new ArrayList<String>();
								Stack<String> stack=new Stack<String>();
								for(String str:midList){
										int flag=this.matchWitch(str);
										switch (flag) {
												case 7:
													afterList.add(str);
													break;
												case 1:
													stack.push(str);
													break;
												case 2:
													String pop=stack.pop();
													while(!pop.equals("(")){
															afterList.add(pop);
															pop=stack.pop();
														}
													break;
												default:
													if(stack.isEmpty()){
															stack.push(str);
															break;
														}
													else{
															if(stack.peek().equals("(")){
																	stack.push(str);
																	break;
																}else{
																	int ji1=this.youxianji(str);
																	int ji2=this.youxianji(stack.peek());
																	if(ji1>ji2){
																			stack.push(str);
																		}else{
																			while(!stack.isEmpty()){
																					String f=stack.peek();
																					if(f.equals("(")){
																							stack.push(str);
																							break;
																						}else{
																							if(this.youxianji(str)<=this.youxianji(f)){
																									afterList.add(f);
																									stack.pop();
																								}else{
																									stack.push(str);
																									break;
																								}
																						}
																				}
																			if(stack.isEmpty()){
																					stack.push(str);
																				}
																		}
																	break;
																}
														}
											}
									}
								while(!stack.isEmpty()){
										afterList.add(stack.pop());
									}
								StringBuffer sb=new StringBuffer();
								for(String s:afterList){
										sb.append(s+" ");
									}
								//System.out.println(sb.toString());
								return afterList;
							}
						/**
							判断运算符的优先级
							*/
						public int youxianji(String str){
								int result=0;
								if(str.equals("+")||str.equals("-")){
										result=1;
									}else{
										result =2;
									}
								return result;
							}
						/**
							*判断字符串属于操作数、操作符还是括号
							*/
						public int matchWitch(String s){
								if(s.equals("(")){
										return 1;
									}else if(s.equals(")")){
										return 2;
									}else if(s.equals("+")){
										return 3;
									}else if(s.equals("-")){
										return 4;
									}else if(s.equals("*")){
										return 5;
									}else if(s.equals("/")){
										return 6;
									}else{
										return 7;
									}
							}
						/**
							*计算a@b的简单方法
							*/
						public Double singleEval(Double pop2,Double pop1,String str){
								Double value=0.0;
								if(str.equals("+")){
										value=pop2+pop1;
									}else if(str.equals("-")){
										value=pop2-pop1;
									}else if(str.equals("*")){
										value=pop2*pop1;
									}else {
										value=pop2/pop1;
									}
								return value;
							}
						private double result;

						public double getResult() {
								return result;
							}
						public void setResult(double result) {
								this.result = result;
							}
						private int state;

						public int getState() {
								return state;
							}
						public void setState(int state) {
								this.state = state;
							}

						public void countHouzhui(List<String> list){
								str = new StringBuilder("");
								state=0;
								result=0;
								Stack<Double> stack=new Stack<Double>();
								for(String str:list){
										int flag=this.matchWitch(str);
										switch (flag) {
												case 3:
												case 4:
												case 5:
												case 6:
													Double pop1=stack.pop();
													Double pop2=stack.pop();
													Double value=this.singleEval(pop2, pop1, str);
													stack.push(value);
													break;
												default:
													Double push=Double.parseDouble(str);
													stack.push(push);
													break;
											}
									}
								if(stack.isEmpty()){
										state=1;
									}else{
										result=stack.peek();
										str.append(stack.pop());
									}


							}

						public void eval() throws Exception{
								List<String> list=new ArrayList<String>();
								//匹配运算符、括号、整数、小数，注意-和*要加\\
								Pattern p = Pattern.compile("[+\\-/\\*()]|\\d+\\.?\\d*");
								Matcher m = p.matcher(s1);
								while(m.find()){
										list.add(m.group());
									}
								List<String> afterList=this.midToAfter(list);
								this.countHouzhui(afterList);
							}
						
 
    }
}

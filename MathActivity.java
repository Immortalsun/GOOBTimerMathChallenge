package com.maashes.timerproject;

import java.util.Random;



import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MathActivity extends Activity {

	//declare UI components
		//declare text for problem
		TextView problem;
		TextView scoreNum;
		TextView multNum;
		//declare text for answer
		EditText answer;
		//declare buttons for action listeners
		Button answerButton;
		Button generateButton;
		//declare random number generator
		static Random generator = new Random();
		static String fullOperation;
		//declare symbols array
		static String[] symbols = {"+", "-", "*", "/"};
		//declare doubles and its for numbers, answers, etc
		static int op1,op2, symbolChoice,numCorrect;
		static double multiplier = 1.0, score, pointValue;
		static int uAnswer,cAnswer,difficulty=20;
		static int gcd =0;
		
		//generate operand 1
		public static  int generateOp1(){
			 int firstOperator = generator.nextInt(difficulty)+1;
			 return firstOperator;
		}
		//generate operand 2
		public static int generateOp2(){
			int secondOperator = generator.nextInt(difficulty)+1;
			return secondOperator;
		}
		//method to return whether use has the correct answer
		public static boolean isCorrect(int userAnswer){
			if(userAnswer == cAnswer){
				return true;
			}
			else
				return false;
		}
		//gcd method for clean division
		public static int GCD(int x, int y){
	        if(y == 0){
	            return x;
	        }
	        else{
	           return GCD(y, x%y);
	        }  
	    }
		//generate an index 0-3 for choice of symbol
		public static int generateSymbolChoice(){
			int newSymbol = generator.nextInt(symbols.length);
			return newSymbol;
			
		}
		//add to score, point values or based on what kind of question
		static void addScore(String symbol){
			if(symbol.equals("+")||symbol.equals("-")){
				pointValue = 2.0;
			}
			else{
				pointValue= 5.0;
			}
			score += pointValue * multiplier;
		} 
		//calculate multiplier based on number correct
		static void calcMult(int numCorrect){
			if(numCorrect>=5){
				multiplier+=.5;
			}
			else if(numCorrect<=2){
				multiplier=1.0;
			}	
			}
		static void incDifficulty(){
			if(multiplier > 3.0){
				difficulty+=5;
			}
		}	
		//generates entire math problem with both operands and operator symbol
		static String generateCompleteProblem(){
			op1= generateOp1();
			op2 = generateOp2();
			symbolChoice = generateSymbolChoice();
			String operatorSymbol = symbols[symbolChoice];
			
			  if(operatorSymbol.equals("+")){
	                cAnswer = op1 + op2;
	             }
	             else if(operatorSymbol.equals("-")){
	                 cAnswer = op1 - op2;
	             }
	             else if(operatorSymbol.equals("*")){
	                 cAnswer = op1 * op2;
	             }
	             //if we get here, obviously division was chosen
	             //if number 1 is greater, and num2 does not evenly divide num1
	             //find GCD of two, then divide the greater by the GCD
	             else {
	                 if(op1 > op2 && op1%op2 != 0 ){
	                     gcd = GCD(op1, op2);
	                     cAnswer = op1/gcd;
	             }
	                 else if(op2 > op1 && op2 % op1 !=0){
	                     gcd = GCD(op2, op1);
	                     cAnswer = op2/gcd;
	                 }
	                 else if(op1>op2){
		            	 cAnswer = op1/op2;
		             }
		             else {
		            	 cAnswer = op2/op1;
		             }
	                
	             }
	             
	            	 
			  //create full operation string and set the text field to that string
			  if(!operatorSymbol.equals("/")||operatorSymbol.equals("/")&&gcd==0&&op1>op2){
			   fullOperation = op1 +" "+operatorSymbol+" "+op2+" =";
			  }
			  //place op2 over op1 if there is division
			  else if(!operatorSymbol.equals("/")||operatorSymbol.equals("/")&&gcd==0&&op2>op1){
				   fullOperation = op2 +" "+operatorSymbol+" "+op1+" =";
				  }
			  else if(op1>op2){
				  fullOperation = op1+" "+operatorSymbol+" "+gcd+" =";  
			  }
			  else{
				  fullOperation = op2+" "+operatorSymbol+" "+gcd+" =";
			  }
			  return fullOperation;
		} 
		
		
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.math_layout);
	        
	        //set variables equal to ui elements
	        problem = (TextView)findViewById(R.id.problem_text);
	        answer = (EditText)findViewById(R.id.answer_message);
	        answerButton = (Button)findViewById(R.id.answer_Button);
	        generateButton = (Button)findViewById(R.id.generate_Button);
	        scoreNum = (TextView)findViewById(R.id.score_number);
	        multNum = (TextView)findViewById(R.id.mult_num);  
	        //set on click listener for generate button
	        generateButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					// generate numbers on click of generate button
					problem.setText(generateCompleteProblem());
					
				}
	        	
	        });
	        //set click listener for answer button
	        answerButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					// pull in user input from text field and change to int for checking
					if(answer.getText().length()>3){
						//if user enters in a huge number, or a number greater than the largest possible answer (20*20) automatically wrong
						new AlertDialog.Builder(MathActivity.this).setMessage("Impossible answer, try again")
						.setPositiveButton("OK",new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							} 
								
					}).create().show();
					}
					uAnswer = Integer.parseInt(answer.getText().toString());
					 if(isCorrect(uAnswer)){
						//create dialog box to show result
						new AlertDialog.Builder(MathActivity.this).setMessage("Correct!")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
							
							
						}).create().show();
						//if user is correct, clear answer field, increment number correct
						//calculate multiplier based on number correct, then display multiplier
						//add and display total score, then auto-generate next problem
						answer.setText("");
						numCorrect++;
						calcMult(numCorrect);
						multNum.setText(Double.toString(multiplier)+"X");
						incDifficulty();
						addScore(symbols[symbolChoice]);
						scoreNum.setText(Double.toString(score));
						problem.setText(generateCompleteProblem());
					}
					else{
						//create dialog box to show incorrect
						new AlertDialog.Builder(MathActivity.this).setMessage("Incorrect, try again.")
						.setPositiveButton("OK",new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							} 
								
					}).create().show();
						answer.setText("");
						if(numCorrect>0){
						numCorrect--;
						}
						else{
							numCorrect=0;
						}
					}
						
				}
	        	
				
	          });
	    }

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.math_layout, menu);
	        return true;
	    }
	    
	   
	    
	}




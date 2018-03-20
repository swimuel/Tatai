package softeng206.tatai;

import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *The Question object class, responsible for the generation of
 *various random questions. A Question has an integer answer (for checking
 *against the number map) and a string to display when showing the question.
 * 
 * @author Sam Broadhead
 */
public class Question {
	private Integer answer;
	private String display;
	
	/**
	 * Constructor for a new question
	 * @param question A string of the operation if random or just the number
	 * if in practice
	 */
	Question(String question){
		switch (question) {
			case "addition":
				randomAdd();
				break;
			case "subtraction":
				randomSub();
				break;
			case "multiplication":
				randomMult();
				break;
			case "division":
				randomDiv();
				break;
			default:
				display = question;
				ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
				ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
				try {
					answer = (int) Double.parseDouble(scriptEngine.eval(question).toString());
				} catch (NumberFormatException | ScriptException e) {
					e.printStackTrace();
				}
				break;
		}
	}
	
	/**
	 * simple score getter
	 * @return the integer score
	 */
	public int getScore() {
		return answer;
	}
	
	/**
	 * simple display getter
	 * @return the question as a string (e.g. "3+4")
	 */
	public String display() {
		return display;
	}
	
	/**
	 * simple display getter but with different name to look better
	 * when called
	 * @return the question as a string (e.g. "3+4")
	 */
	public String csvStyle() {
		return display;
	}
	
	/**
	 * method to generate a random addition question
	 * with answer between 2 and 18
	 */
	private void randomAdd() {
		Random rand = new Random();
		Integer numOne = rand.nextInt(9-1+1)+1;
		Integer numTwo = rand.nextInt(9-1+1)+1;
		display = numOne.toString()+"+"+numTwo.toString();
		answer = numOne+numTwo;
	}
	
	/**
	 * method to generate a random subtraction question
	 * with answer between 9 and 1
	 */
	private void randomSub() {
		Random rand = new Random();
		Integer numOne = rand.nextInt(10-1+1)+1;
		Integer numTwo = rand.nextInt(9-1+1)+1;
		while(numOne-numTwo <1) {
			numOne = rand.nextInt(10-1+1)+1;
			numTwo = rand.nextInt(9-1+1)+1;
		}
		display = numOne.toString()+"-"+numTwo.toString();
		answer = numOne-numTwo;
	}
	
	/**
	 * method to generate a random multiplication question
	 * with answer between 1 and 81 
	 */
	private void randomMult() {
		Random rand = new Random();
		Integer numOne = rand.nextInt(9-1+1)+1;
		Integer numTwo = rand.nextInt(9-1+1)+1;
		display = numOne.toString()+"*"+numTwo.toString();
		answer = numOne*numTwo;
	}
	
	/**
	 * method to generate a random division question
	 * with answer between 30 and 1
	 */
	private void randomDiv() {
		Random rand = new Random();
		Integer numOne = rand.nextInt(30-1+1)+1;
		Integer numTwo = rand.nextInt(10-1+1)+1;
		while(numOne%numTwo != 0) {
			numOne = rand.nextInt(9-1+1)+1;
			numTwo = rand.nextInt(9-1+1)+1;
		}
		display = numOne.toString()+"/"+numTwo.toString();
		answer = numOne/numTwo;
	}
}

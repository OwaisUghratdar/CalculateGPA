import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.io.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Objects;

import java.util.ArrayList;
import java.util.Objects;


/**
 * This class generates a transcript for each student, whose information is in
 * the text file.
 * 
 *
 */

public class Transcript {
	
	/*
	 * The attributes of this Class Transcript
	 */
	private ArrayList<Object> grade = new ArrayList<Object>();
	private File inputFile;
	private String outputFile;

	/**
	 * This the the constructor for Transcript class that initializes its instance
	 * variables and call readFie private method to read the file and construct
	 * this.grade.
	 * 
	 * @param inFile  is the name of the input file.
	 * @param outFile is the name of the output file.
	 */
	public Transcript(String inFile, String outFile) {
		inputFile = new File(inFile);

		grade = new ArrayList<Object>();
		this.readFile();
		ArrayList<Student> list = this.buildStudentArray();
		this.printTranscript(list);

		outFile = outputFile;
		// System.out.println(outFile);

	}// end of Transcript constructor

	/**
	 * This method reads a text file and add each line as an entry of grade
	 * ArrayList.
	 * 
	 * @exception It throws FileNotFoundException if the file is not found.
	 */
	private void readFile() {
		Scanner sc = null;
		try {
			sc = new Scanner(inputFile);
			while (sc.hasNextLine()) {
				grade.add(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
	} // end of readFile

	
	/**
	 * This method creates and returns an ArrayList<Student>
	 * The object at each element is created by aggregating ALL the
	 * information, that is found for one student in the grade Arraylist of class Transcript.
	 * 
	 * @return resultStudentArray is the name of the ArrayList<Student>
	 */
	public ArrayList<Student> buildStudentArray() {
		ArrayList<Student> resultStudentArray = new ArrayList<Student>(); //create an ArrayList<Student> called resultStudentArray
		HashSet<String> set = new HashSet<>();		//hashset to store all the studentIds
		HashMap<String, Student> map = new HashMap<>();
		for (Object obj : grade) {			//enhanced for loop to iterate through ArrayList grade which stores each line from input
			String s = obj.toString();		//this will change the line into a String
			String[] st = s.split(",");		//this will split the string at each comma and the comma will be take out
			int len = st.length;			//get length
			ArrayList<Integer> wts = new ArrayList<>();
			ArrayList<Double> grds = new ArrayList<>();
			// calculating total grade in subject

			for (int i = 3; i < len - 1; i++) {		//this for loop will iterate through all the element which contain a grade an weight for assessments
				String s1 = st[i];					
				String st1[] = s1.split("\\(+");	//split each element by the opening bracket
				int wt = Integer.valueOf(st1[0].substring(1, st1[0].length()));				//get the integer value of the weight
				double grd = Double.valueOf(st1[1].substring(0, st1[1].length() - 1));		//get the double value of the grade
				wts.add(wt);	//add weight to ArrayList that contains all the weights
				grds.add(grd);	//add grade to ArrayList that contains all the grades

			}

			if (!set.contains(st[2])) {			//if statement to check if the hashset contains the studentId
				Student temp = new Student(st[2], st[len - 1], new ArrayList<Course>());	//create the new student with the name and studentId and a new list of courses
				Course course_temp = new Course(st[0], new ArrayList<Assessment>(), Double.valueOf(st[1]));		//create a new course with a list of assessments and credits of the course
				temp.addCourse(course_temp);		//add the course into the arrayList
				map.put(st[2], temp);				//add the studentId into the map
				set.add(st[2]);						//add the studentId into the set
				resultStudentArray.add(temp);		//add the student info into our resultStudentArray ArrayList which we will return at the end
			}
			
			else {							//if the hashset does contain the studentId then add the course under the courses the took
				Student temp = map.get(st[2]);
				Course course_temp = new Course(st[0], new ArrayList<Assessment>(), Double.valueOf(st[1]));
				temp.addCourse(course_temp);	//add the course into the ArrayList
			}

			Student temp = map.get(st[2]);
			temp.addGrade(grds, wts);		//calculate the grade of the course for the student

		}
		return resultStudentArray;		//return the ArrayList<Student> called resultStudentArray that we created in the beginning
	}

	
	
	/**
	 * This method is our printTranscript method. This method will print out the transcript
	 * to the console and to the given file with the expected output. Void since we are only
	 * printing and we don't need to return.
	 * 
	 * @param list is the name of the ArrayList<Student>
	 */
	public void printTranscript(ArrayList<Student> list) {

		//this creates a StringBuilder object so that all of our output can be appended into one object.
		StringBuilder sb = new StringBuilder(); 
		
		
		//An enhanced for loop which will iterate through our parameter list
		//Then for every Student object inside the ArrayList list we will get
		//all relevant information and append it to the StringBuilder
		for (Student stu : list) {			
			sb.append(stu.getName() + "\t" + stu.getStudentID() + "\n"); // here we get the name and studentID
			sb.append("--------------------" + "\n");                    // append characters to match out output
			
			// another for loop to loop through all of the students' courses and their marks
			for (int i = 0; i < stu.getCourseTaken().size(); i++) {      
				String course = stu.getCourseTaken().get(i).getCode(); //get course code
				double final_grade = stu.getFinalGrade().get(i);       //get mark from that course since the two array lists are parallel
				sb.append(course + "\t");							   //append characters to match out output
				sb.append(String.format("%.1f ", final_grade));		   //format double to round to 1 decimal place
				sb.append("\n");									   //append characters to match out output
			}
			
			sb.append("--------------------" + "\n");		//append characters to match out output
			double GPA = stu.weightedGPA();					//this will calculate the student's GPA and store it in the double variable "GPA"
			sb.append(String.format("GPA: %.1f \n", GPA));	//format double to round to 1 decimal place
			sb.append("\n");
		}
		
		System.out.println(sb);				//this will print our StringBuilder to the console
		outputFile = sb.toString();			//this will store our StringBuilder into the String called outputFile
	}

	
	/**
	 * This is our main method. This is what will be started when we run the program.
	 * This will call on Transcript which will allow our program everything it needs to do
	 * 
	 */
	/*I commented out the constructor
	public static void main(String args[]) {
		Transcript temp = new Transcript("input.txt", ""); //This will allow our program to start with the given input.txt file
	}
	*/
 // end of Transcript




//////////////////////////////////////////////////////////////////////////////////////////////////////////////


	

	public class Student {
		
		/*
		 * The attributes of this Class Student
		 */
		private String studentID;
		private String name;
		private ArrayList<Course> courseTaken = new ArrayList<Course>();
		private ArrayList<Double> finalGrade = new ArrayList<Double>();
		
		/**
		 * Initializes the attributes with default entries to make sure the Student has a name,
		 * studentID, and has an ArrayList of courseTaken. So that if this constructor is
		 * called on, our program will not fail and has some values to pass.
		 */
		public Student() {
			this.studentID = "0000";
		    this.name = "default";
		    this.courseTaken = new ArrayList<Course>();
		}
		
		/**
		 * Initializes the attributes so that the Student has a name,
		 * studentID, and has an ArrayList of courseTaken.
		 * 
		 * @param id, the studentId of the Student
		 * @param name, the name of the Student
		 * @param courseList, the courseTaken that the student has (courses that Student is enrolled in)
		 */
		public Student(String id, String na, ArrayList<Course> courseList) {
			this.studentID = id;
			this.name = na;
			this.courseTaken = courseList;
		}
		
		/**
		 * Adds the specified course to this.coursetaken.
		 * 
		 * @param newCourse a Course object to add to this.coursetaken
		 */
		public void addCourse(Course newCourse) {
			this.courseTaken.add(newCourse);        //add the course taken by the Student  into an ArrayList that will hold courses taken by the student
		}
		
		/**
		 * This method will calculate the grade of a given course using an ArrayList
		 * of grades and an ArrayList of weights. These two are ArrayList are parallel
		 * meaning that at element [i] of both ArrayList one will contain the grade
		 * and the other will contain the weight of the activity (assessment/assignment).
		 * We will use these two to calculate how much percent will be going to the student's
		 * final grade for that course and continue to add all of them up.
		 * 
		 * @throws InvalidTotalException if the total weight of the activities is not equal to 100 
		 * 							  or if the total grade of the student is greater than 100 
		 * 
		 * @param grade, an ArrayList that contains all the grades of the Student for a specific course
		 * @param weights, an ArrayList that contains all the weights of each activity for the same specific course
		 */
		public void addGrade(ArrayList<Double> grades, ArrayList<Integer> weights) {
			int total = 0;               //this is the variable to hold the total weight of the course to make sure it is 100
			double gradeResult = 0;		 //this is the variable to hold the actual mark that the student receives in the course
			
			for(int i=0; i < weights.size() ; i++) {	//for loop to calculate the total weight of the activities in the course
				total += weights.get(i);
			}
		
			if(total > 100 || total != 100) { //if statement to throw an InvalidTotalException if the total weight is not equal to 100
				try {						  //try catch block to handle the exception
					throw new InvalidTotalException("Error: Total is not 100!");
				} 
				catch (InvalidTotalException e) {
					e.printStackTrace();			//this will print a stack trace on the error output stream 
													//basically print the exception and line # for us
					System.exit(0);					//this will exit the program
				}
			}
		
			for(int i=0; i < grades.size() ; i++) {		//for loop to calculate the total grade that the Student received in the course
				double markPerAssignment = 0;			//this double will hole the mark per each assignment
				markPerAssignment = (grades.get(i) * weights.get(i)) / 100;		//calculate the mark per each assignment
				gradeResult += markPerAssignment;						//add each assignment mark to the total
			}															//continue to loop until all assignment are calculated and added
		
			if(gradeResult > 100) { //if statement to throw an InvalidTotalException if the grade is greater than 100.
				try {					//try catch block to handle the exception
				throw new InvalidTotalException("Error: Grade exceeds 100!");
				} 
				catch (InvalidTotalException e) {
					e.printStackTrace();			//this will print a stack trace on the error output stream 
													//basically print the exception and line # for us
					System.exit(0);					//this will exit the program
				}
			}
			this.finalGrade.add(gradeResult);		//add the final grade the Student received from each course into an ArrayList that will hold all grades
		}
		
		
		
		/**
		 * Calculates the weighted GPA of the Student using the number of credits that the course is worth
		 * and the mark the student received in each course.
		 * 
		 * @return a double data type which holds the GPA of the student for the courses the Student took
		 */
		public double weightedGPA() {
			double totalCredits = 0;		//this is the variable to hold the total credits of all courses that the Student has taken
			double totalQP = 0;		//this is the variable to hold the total weight of the course to make sure it is 100
			for(int i=0;i<courseTaken.size();i++)		//for loop to iterate through all the courses that the student took
			{
				double creditForCourse = courseTaken.get(i).getCredit();	//this is the variable to hold the number of credits for each course
				double final_grade = finalGrade.get(i);						//this variable will hold the final grade for each course 
				
				double gradePoint = weightedGrade(final_grade);		//this variable will convert each % grade to a grade point on the scale 1 - 9, using the weightedGrade(double) method
				double qualityPoints = gradePoint*creditForCourse;	//this will calculate the quality points for each course, so a 3 credit course, with a 8 gradepoint, 3*8 = 24 quality points
				totalQP += qualityPoints;							//this will calculate the total quality points
				totalCredits += creditForCourse;					//this will calculate the total credits
			}
			double GPA = totalQP/totalCredits;		//by dividing the total quality points by the total credits taken, we will calculate the GPA for all the courses
			return GPA;
		}
		
		
		/**
		 * This method converts the percentage mark to a grade point on the scale of 1 - 9.
		 * To do this conversion I used the table given in the Assignment PDF
		 * 
		 * @param m is a double that holds the percentage mark the student received in a specific course
		 */
		private double weightedGrade(double m)
		{
			if(m<=100.00 && m>=90.00 ) return 9.0;
			if(m<=89.99 && m>=80.00 ) return 8.0;
			if(m>=75 && m<=79.99 ) return 7.0;
			if(m>=70 && m<=74.99 ) return 6.0;
			if(m>=65 && m<=69.99 ) return 5.0;
			if(m>=60 && m<=64.99 ) return 4.0;
			if(m>=55 && m<=59.99 ) return 3.0;
			if(m>=50 && m<=54.99 ) return 2.0;
			if(m>=47 && m<=49.99 ) return 1.0;
			return 0.0;			//if the student did not get a mark within the scale, then they will receive a 0.0 grade point for that course
			
		}
		
		
		//ENCAPSULATION: Getters and Setters for each attribute
		public String getStudentID() {
			return studentID;
		}
		
		public void setStudentID(String studentID) {
			this.studentID = studentID;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public ArrayList<Course> getCourseTaken() {
			return new ArrayList<Course>(this.courseTaken);								//defensive copy
		}
		
		public void setCourseTaken(ArrayList<Course> courseTaken) {
			ArrayList<Course> copycourseTaken = new ArrayList<Course>(courseTaken);		//defensive copy
		}
		
		public ArrayList<Double> getFinalGrade() {
			return new ArrayList<Double>(this.finalGrade);								//defensive copy
		}
		
		public void setFinalGrade(ArrayList<Double> finalGrade) {
			ArrayList<Double> copyfinalGrade = new ArrayList<Double>(finalGrade);		//defensive copy
		}
		
		
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////


	
	public class Course {

		/*
		 * The attributes of this Class Course
		 */
		private String code;
		private double credit;
		private ArrayList<Assessment> assignment = new ArrayList<Assessment>();

		/**
		 * Initializes the attributes with default entries to make sure the Course has a code,
		 * and credit number. So that if this constructor is
		 * called on, our program will not fail and has some values to pass.
		 */
		public Course() {
			this.code = "EECS0000";
			this.credit = 0;
		}

		/**
		 * Initializes the attributes so that the Student has a name,
		 * studentID, and has an ArrayList of courseTaken.
		 * 
		 * @param classCode, the code of the class (eg. EECS2030)
		 * @param allAssignmentsList, an arrayList that holds the assignments of the course
		 * @param numCredits, the number of credits the class is worth
		 */
		public Course(String classCode, ArrayList<Assessment> allAssignmentsList, double numCredits) {
			this.code = classCode;
			this.credit = numCredits;
			this.assignment = allAssignmentsList;
		}

		/**
		 * This is the copy constructor, in which we will copy each field of the courseObj
		 * into the object we are working with. We must create deep copies for objects
		 * which are mutable
		 * 
		 * @param courseObj, which is a Course object, it is the same object type as the constructor
		 */
		public Course(Course courseObj) {
			this.code = courseObj.code;
			this.credit = courseObj.credit;
			this.assignment = new ArrayList<Assessment>(courseObj.getAssignment());
		}
		
		/**
		 * This method overrides the equals method. We check if the this. variables
		 * are equal to the other. variable
		 * 
		 * @param obj which is of the type object
		 * @return true if the two object are equal, otherwise false
		 */
		@Override
		public boolean equals(Object obj) { 	
			if (this == obj) {		//if the 2 objects are equal then return true
				return true;
			}
								
			if (obj == null) {		//check to see if object is null/empty, if so return false
	            return false;
	        }

	        Course other = (Course) obj; 		//creates a course object called other
	        if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {		//checks to see if their code fields are null, if not,
	            return false;																		//check to see if they are equal, if not, then false
	        }

	        if (this.credit != other.credit) {		//check to see if their credit fields are equal, if not then false
	            return false;
	        }
	        
	        if (this.assignment == null) {			//checks to see if their assignment fields are null, if this is and other is not then false
				if (other.assignment != null)
					return false;
			} 
	        
	        if (other.assignment == null) {			//checks to see if their assignment fields are null, if other is and this is not then false
				if (this.assignment != null)
					return false;
			} 
	        
	        if (this.assignment.size() != other.assignment.size()) {		//check to see if their assignment fields have the same size, if not then false
				return false;
	        }
	        for(int i = 0; i < this.assignment.size(); i++) {			//for loop to iterate through the assignment ArrayList
	        	if(this.assignment.get(i) != other.assignment.get(i)) {	//check to see if the assignment fields have same element at each index, if not then false
	        		return false;
	        	}
	        }

	        return true;		//once everything is checked and all tests pass, the two objects are equal, so return true
		}
		

		// ENCAPSULATION: Getters and Setters for each attribute

		public void setCode(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public void setCredit(double credit) {
			this.credit = credit;
		}

		public double getCredit() {
			return credit;
		}

		public void setAssignment(ArrayList<Assessment> assignment) {
			ArrayList<Assessment> copyAssignment = new ArrayList<Assessment>(assignment);	//defensive copy
		}

		public ArrayList<Assessment> getAssignment() {
			return new ArrayList<Assessment>(this.assignment);								//defensive copy
		}
	}



//////////////////////////////////////////////////////////////////////////////////////////////////////////////





	public class Assessment {

		/*
		 * The attributes of this Class Assessment
		 */
		private char type;
		private int weight;
		
		/**
		 * Initializes the attributes with default entries to make sure the Assessment has a type,
		 * and weight. So that if this constructor is
		 * called on, our program will not fail and has some values to pass.
		 */
		private Assessment() {
			this.type = 'A';
			this.weight = 0;
		}

		/**
		 * Initializes the attributes so that the Assessment has a type,
		 * and weight
		 * 
		 * @param x, a char which represents the type of assessment it is
		 * @param y, an integer that holds the weight of the assessment
		 */
		private Assessment(char x, int y) {
			this.type = x;
			this.weight = y;
		}

		/**
		 * This is a static factory method for class Assessment.
		 * 
		 * @param x, a char which represents the type of assessment it is
		 * @param y, an integer that holds the weight of the assessment
		 */
		public Assessment getInstance(char x, int y) {
			return new Assessment(x, y);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)	//if the 2 objects are equal then return true
				return true;
			if (obj == null)	//check to see if object is null/empty, if so return false
				return false;

			Assessment other = (Assessment) obj;	//creates a Assessment object called other
			if (this.type != other.type)			//check to see if their type fields are equal, if not then false
				return false;
			if (this.weight != other.weight)		//check to see if their weight fields are equal, if not then false
				return false;
			return true;					//once everything is checked and all tests pass, the two objects are equal, so return true
		}
		

		// ENCAPSULATION: Getters and Setters for each attribute

		public char getType() {
			return type;
		}

		public void setType(char type) {
			this.type = type;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}

	}



//////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public class InvalidTotalException extends Exception {

		public InvalidTotalException() {
			
		}
		
		public InvalidTotalException(String message) {
			super(message);
		}
		
		public InvalidTotalException(Throwable cause) {
	        super (cause);
	    }

	    public InvalidTotalException(String message, Throwable cause) {
	        super (message, cause);
	    }
	}



}
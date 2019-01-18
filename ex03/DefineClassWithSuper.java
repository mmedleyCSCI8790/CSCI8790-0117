package ex03;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javassist.ClassPool;
import javassist.NotFoundException;

import javassist.CannotCompileException;
import javassist.CtClass;


public class DefineClassWithSuper {
	static String _S = File.separator;
	static String WORK_DIR = System.getProperty("user.dir");
	static String OUTPUT_DIR = WORK_DIR + _S + "output";
	static Scanner scanner = null;

	public static void main(String[] args) {
		   File outDir = new File(OUTPUT_DIR);
		   String superClassName = null;
		   String subClassName = null;
		   String[] clazNames = null;
		   
		   do {
			   System.out.println("Enter superclass and class names:");
			   clazNames = getArguments();
			   if (clazNames != null && clazNames.length != 2) {
				   clazNames = null;
				   System.out.println("[WRN] Invalid Input");
			   }
		   } while (clazNames == null);
		   
		   if (clazNames.length != 2) {
			   return;
		   }
		   
		   if (!outDir.exists()) {
			   outDir.mkdir();
		   }
		   
		   for (int i = 0; i < 2; ++i) {
			   if (clazNames[i].startsWith("Common")) {
				   if (superClassName == null) {
					   superClassName = clazNames[i];
				   } else {
					   if (clazNames[i].length() > superClassName.length()) {
						   subClassName = superClassName;
						   superClassName = clazNames[i];
					   } else {
						   subClassName = clazNames[i];
					   }
				   }
			   } else if (superClassName != null) {
				   subClassName = clazNames[i];
			   }
		   }
		   
		   if (superClassName == null) {
			   superClassName = clazNames[0];
			   subClassName = clazNames[1];
		   }
		   
		   createClassWithSuperClass(superClassName, subClassName);
	}
	
	public static void createClassWithSuperClass(String superClassName, String subClassName) {
	      try {
	          ClassPool pool = ClassPool.getDefault();
	          pool.insertClassPath(OUTPUT_DIR);
	          System.out.println("[DBG] class path: " + OUTPUT_DIR);

	          CtClass ccSuperClass = pool.makeClass(superClassName);
	          ccSuperClass.writeFile(OUTPUT_DIR);
	          System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	          System.out.println("[DBG]\t new class: " + ccSuperClass.getName());

	          CtClass ccSubClass = pool.makeClass(subClassName);
	          ccSubClass.writeFile(OUTPUT_DIR);
	          System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	          System.out.println("[DBG]\t new class: " + ccSubClass.getName());

	          ccSubClass.defrost();
	          System.out.println("[DBG] modifications of the class definition will be permitted.");

	          ccSubClass.setSuperclass(ccSuperClass);
	          System.out.println("[DBG] set super class, " + ccSubClass.getName() + " -> " + ccSuperClass.getName());

	          ccSubClass.writeFile(OUTPUT_DIR);
	          System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	       } catch (NotFoundException | CannotCompileException | IOException e) {
	          e.printStackTrace();
	       }
		
	}
	
	static void insertClassPath(ClassPool pool) throws NotFoundException {
	      String strClassPath = OUTPUT_DIR;
	      pool.insertClassPath(strClassPath);
	      System.out.println("[DBG] insert classpath: " + strClassPath);
	}
	
	public static String[] getArguments() {
			scanner = new Scanner(System.in);
			String input = null;

			while (scanner.hasNextLine()) {
		         input = scanner.nextLine();
		         System.out.println(input);
		         if (input != null)
		            break;
		    }

		    if (input != null && !input.trim().isEmpty()) {
		    	return input.trim().split("\\s+");
		    }
		    
		    System.out.println("[WRN] Invalid Input");
		    return null;
	}

}

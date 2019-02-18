/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import java.text.*;
import java.io.*;
import java.nio.file.*;

/**
 *
 * @author SAHITHI BOINPALLI CS610 prp3821
 */

public class pgrk3821{
  private static List<ArrayList<Integer>> list;
  private static double[] pgeRnk,prevRnk,curRnk;
  private static int[]    outedgeCount;
  private static int      nodeCount,edgeCount,inpiter,intval;
  private static double dmp;
  private static double dmpAdd;

  @SuppressWarnings("empty-statement")
  public static void main(String ... args){
    int c = 0;
    intval = 0;  
    if(args.length < 3){
        System.out.println("ERROR: Please provide 3 arguments.Ex: java pgrk9844 <iterations> <rank> <input File>");
      return;
    }
    try{
       String fileName = args[2]; 
       List<String> lines = Files.readAllLines(Paths.get(fileName));
       List<String> edgeNode = lines;
       String headInformation = lines.get(0); 
       String[] headerInfoStrArray = headInformation.split(" ");
       int[] headerInfoIntArray = new int[headerInfoStrArray.length];
       headerInfoIntArray[0] = Integer.parseInt(headerInfoStrArray[0]);
       headerInfoIntArray[1] = Integer.parseInt(headerInfoStrArray[1]);
       int edgesc = lines.size();
       nodeCount = headerInfoIntArray[0];
       edgeCount = headerInfoIntArray[1];
       if(edgeCount < edgesc-1 ){
            System.out.println("Number of edges are lesser than given in the file");
            return;
       }
       else if(edgeCount > edgesc-1){
            System.out.println("Number of edges are greater than given in the file");
            return;
       }
       lines.remove(intval);
       list = new ArrayList<>(nodeCount);
       curRnk = new double[nodeCount];
       prevRnk = new double[nodeCount];
       outedgeCount = new int[nodeCount];;
       for (int i = 0; i < nodeCount; i++) {
		     	list.add(new ArrayList<>());
	     }
       List<String> edgesInfo = edgeNode;
       String[] edges;
       edges = new String[2];
       for(int i = 0; i < edgesInfo.size(); i++){
       edges = edgesInfo.get(i).split(" ");
       int[] edgeToFrom = new int[2];
       int fromNode,toNode;
       fromNode= Integer.parseInt(edges[0]);
       toNode = Integer.parseInt(edges[1]);
       if(fromNode == toNode){
           System.out.println("ERROR: Node["+fromNode+"] pointing to itself");
           return;
       }
       if(toNode >= nodeCount){
           System.out.println("ERROR: Node["+toNode+"] does not exist");
           return;
       }
      if(fromNode >= nodeCount){
           System.out.println("ERROR: Node["+fromNode+"] does not exist");
           return;
      }
      else{
      list.get(fromNode).add(toNode);
      }
      }
      for(int i = 0; i < list.size(); i++){
      outedgeCount[i] = list.get(i).size();
      }
    } 
    catch(IOException io){
      System.out.println(io);
    }
    intval = Integer.parseInt(args[1]);
    if(nodeCount > 10){ inpiter = 0; intval = -1; }
    
    double initial;
    dmp = 0.85;
    dmpAdd = (1-dmp)/nodeCount;
      switch (intval) {
          case 0:
          case 1:
              initial = intval;
              break;
          case -1:
              initial = (double)1/nodeCount;
              break;
          case -2:
              initial = 1/Math.sqrt(nodeCount);
              break;
          default:
              initial = intval;
              break;
      }
    for(int i = 0; i < nodeCount; i++){
      curRnk[i] = initial;
      prevRnk[i] = initial;
    }
    inpiter = Integer.parseInt(args[0]);
    displayRank9844(c);
    do{
        double temporaryValue = 0;
        double[] calculatedPageRank = new double[nodeCount];
        for (int i = 0; i < curRnk.length; i++) {
          for (int arylist = 0; arylist < list.size(); arylist++) {
            if (list.get(arylist).contains(i)) {
              temporaryValue = temporaryValue + (curRnk[arylist] / outedgeCount[arylist]);
            }
          }
          calculatedPageRank[i] = dmpAdd + dmp * temporaryValue;
          temporaryValue = 0;
        }
        prevRnk = curRnk;
        curRnk = calculatedPageRank;
      c= c + 1;
      if(nodeCount <= 10){ 
        displayRank9844(c);
      }
    }while(!stopIteration9844(c));
    if(nodeCount > 10){
      int inputIter = c;
      System.out.println("Iterat : "+ inputIter + " : ");
      DecimalFormat df = new DecimalFormat("0.0000000");
      for (int i = 0; i < curRnk.length; i++) {
          System.out.println("PR["+ i + "] = "+df.format(curRnk[i]) + " ");
      }
        System.out.println("");
    }
    System.out.println();
}
    public static boolean stopIteration9844(int currentIteration){
      double temp;
      if (inpiter > 0) {
        return currentIteration == inpiter;
      }
      else {
        if (inpiter == 0) {
          temp = 100000;
        }
        else  {
          temp = Math.pow(10, -inpiter);
        }
  
        for (int i = 0; i < curRnk.length; i++) {
          if ((int)Math.floor(curRnk[i]*temp) != (int)Math.floor(prevRnk[i]*temp)) {
            return false;
          }
        }
        return true;
      }
    }
  
    public static void displayRank9844(int inputIter){
      DecimalFormat df = new DecimalFormat("0.0000000");
      if(inputIter == 0){
          System.out.print("Base : "+ inputIter + " : ");
      }
      else{
          System.out.print("Iterat : "+ inputIter + " : ");
      }
      for (int i = 0; i < curRnk.length; i++) {
           System.out.print("PR["+ i + "] = "+df.format(curRnk[i]) + " ");
      }
        System.out.println("");
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import java.io.*;
import static java.lang.Math.floor;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.nio.file.*;
import java.text.DecimalFormat;

/**
 *
 * @author SAHITHI BOINPALLI CS610 prp3821
 */


public class hits3821{
  private static List<ArrayList<Integer>> list;
  private static double[] auth,prevAuth,prevHub,hub;
  private static int nodesCount,edgeCount,inpIter,intvalue;
  public static void main(String ... args){
    int c;
    c = 0;
    intvalue = 0;
    double t1;
    double t2 = 0,s1=0,s2=0;
    if(args.length < 3){
          System.out.println("ERROR: Please provide 3 arguments.Ex: java hits9844 <iterations> <rank> <input File>");
          return;
    }
    try{
    String fileName = args[2];
    List<String> lines = Files.readAllLines(Paths.get(fileName));
    List<String> edgeNode = lines;
    String[] edges = new String[2];
    String headInformation = lines.get(0);
    String[] headerInfoStrArray = headInformation.split(" ");
    int[] headerInfoIntArray = new int[headerInfoStrArray.length];
    headerInfoIntArray[0] = Integer.parseInt(headerInfoStrArray[0]);
    headerInfoIntArray[1] = Integer.parseInt(headerInfoStrArray[1]);  
    int edgesc;
    edgesc = lines.size();
    nodesCount = headerInfoIntArray[0];
    edgeCount = headerInfoIntArray[1];
    if(edgeCount < edgesc-1 ){
      System.out.println("Number of edges are lesser than given in the file");
      System.exit(0);
      return;
    }
    else if(edgeCount > edgesc-1){
      System.out.println("Number of edges are greater than given in the file");
      System.exit(0);
      return;
    }
    lines.remove(intvalue);
    list = new ArrayList<ArrayList<Integer>>(nodesCount);
    hub = new double[nodesCount];
    auth = new double[nodesCount];
    prevAuth = new double[nodesCount];
    prevHub = new double[nodesCount];
    for (int i = 0; i < nodesCount; i++) {
      list.add(new ArrayList<>());
    }
    for(int i = 0; i < edgeNode.size(); i++){
      edges = edgeNode.get(i).split(" ");
          int[] edgeToFrom = new int[2];
          int fromNode,
              toNode;
          fromNode= Integer.parseInt(edges[0]);
          toNode = Integer.parseInt(edges[1]);
          if(fromNode == toNode){
              System.out.println("ERROR: Node["+fromNode+"] pointing to itself");
              return;
          }
          if(toNode >= nodesCount){
              System.out.println("ERROR: Node["+toNode+"] does not exist");
              return;
          }
          if(fromNode >= nodesCount){
              System.out.println("ERROR: Node["+fromNode+"] does not exist");
              return;
          }
          else{
              list.get(fromNode).add(toNode);
          }
    }
    } 
    catch(IOException io){
      System.out.println(io);
    }
    intvalue = Integer.parseInt(args[1]);
    if(nodesCount > 10){ 
      inpIter = 0; 
      intvalue = -1; 
    }
    double initial;
      switch (intvalue) {
          case 0:
          case 1:
              initial = intvalue;
              break;
          case -1:
              initial = (double)1/nodesCount;
              break;
          case -2:
              initial = 1/Math.sqrt(nodesCount);
              break;
          default:
              initial = intvalue;
              break;
      }
    for(int i = 0; i < nodesCount; i++){
      hub[i] = initial;
      auth[i] = initial;
    }
    inpIter = Integer.parseInt(args[0]);
    dispAuthHub3821(c);
    do{
    for (int i = 0; i < auth.length; i++) {
      prevAuth[i] = auth[i];
      t1 = 0;
      for (int j = 0; j < hub.length; j++) {
        if (list.get(j).contains(i)) {
          t1 = t1 + hub[j];
        }
      }
      auth[i] = t1;
    }

    for (int i = 0; i < hub.length; i++) {
      prevHub[i] = hub[i];
      for (int j = 0; j < auth.length; j++) {
        if (list.get(i).contains(j)) {
          t2 = t2 + auth[j];
        }
      }
      hub[i] = t2;
      t2 = 0;
    }

    for (int i = 0; i < auth.length; i++) {
      s1 += pow(auth[i], 2);
    }
    s1 = sqrt(s1);
    for (int i = 0; i < auth.length; i++) {
      auth[i] /= s1;
    }

    for (int i = 0; i < hub.length; i++) {
      s2 += pow(hub[i], 2);
    }
    s2 = sqrt(s2);
    for (int i = 0; i < hub.length; i++) {
      hub[i] /= s2;
    }
      ++c;
      if(nodesCount <= 10){ 
          dispAuthHub3821(c);
      }
    }while(!stopIter3821(c));

    if(nodesCount > 10){ 
      System.out.println("Iterat : "+ c + " : ");
      DecimalFormat df = new DecimalFormat("0.0000000");
      for (int i = 0; i < hub.length; i++) {
        System.out.println("A/H["+ i + "] = "+df.format(Math.floor(auth[i] * 1e7)/1e7) + " / " + df.format(Math.floor(hub[i] * 1e7)/1e7) + " ");
        System.out.println();
      }
    }
      System.out.println();
  }
  public static boolean stopIter3821(int curIter){
    double temp;
    if (inpIter > 0) {
      return curIter == inpIter;
    }
    else {
      if (inpIter == 0) {
        temp = 100000;
      }
      else  {
        temp = Math.pow(10, -inpIter);
      }
      for (int i = 0; i < auth.length; i++) {
        if ((int)Math.floor(auth[i]*temp) != (int)Math.floor(prevAuth[i]*temp)) {
          return false;
        }
      }

      for (int i = 0; i < hub.length; i++) {
        if ((int)Math.floor(hub[i]*temp) != (int)Math.floor(prevHub[i]*temp)) {
          return false;
        }
      }
      return true;
    }
  }
  public static void dispAuthHub3821(int inputIter){
    DecimalFormat df = new DecimalFormat("0.0000000");
      if(inputIter == 0){
        System.out.print("Base : "+ inputIter + " : ");
      }
      else{
        System.out.print("Iterat : "+ inputIter + " : ");
      }
    for (int i = 0; i < auth.length; i++) {
        double authValue;
        double hubValue;
      authValue = floor(auth[i] * 10000000)/10000000;
      hubValue = floor(hub[i] * 10000000)/10000000;
        System.out.print("A/H["+ i + "] = "+df.format(authValue) + " / " + df.format(hubValue) + " ");
    }
    System.out.println();
  }
}

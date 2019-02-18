/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
/**
 *
 * @author SAHITHI BOINPALLI cs610 prp3821
 */
public class henc3821
{
    static HuffmanMinHeap heap = null;
    static ArrayList<DataSet> dataList = new ArrayList<DataSet>();
    static ArrayList<DataSet> builtHuffmanDataList = new ArrayList<DataSet>();
    static String filePath;
    static read3821 reader = null;
    static write3821 writer = null;
    public static void main(String[] args) {
        filePath = args[0];
        heap = new HuffmanMinHeap();
        reader = new read3821(filePath);
        writer = new write3821(filePath + ".huf");
        String input;
        try{
            input = reader.readString();
            for (char var : input.toCharArray()) {
                    int value =   (int)var;
                    boolean found = false;
                    Iterator iterate = dataList.iterator();
                    DataSet dataSet = new DataSet();
                    DataSet ds = null;
                    dataSet.setValue(value);
                    dataSet.setFrequency(1);
                    while(iterate.hasNext())
                    {
                    ds = (DataSet)iterate.next();
                    if(ds.getValue() == value)
                    {
                        ds.setFrequency(ds.getFrequency() + 1);
                        iterate.remove();
                        found = true;
                        break;
                    }
                    }
                    if(found == true)
                    {
                        dataSet = ds;
                    }
                    dataList.add(dataSet);
                  }
             }
            catch(FileNotFoundException ex)
            {
                 System.out.println(ex);
            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        dataList.forEach((ds) -> {
            heap.add(new node3821(ds.getFrequency(), (char)(ds.getValue())));
        });  
        node3821 root = encode();
        String arr = "";
        writeTreeInFile(root);
        printHuffmanCode(root, arr);
        writeEncodedFile();
        try
        {
            reader.close();
            writer.close();
        }
        catch(IOException ioex)
        {
            System.out.println(ioex);
        }       
        File file = new File(filePath);
        System.gc();
        file.delete();
    }
    private static node3821 encode()
    {
        node3821 min1,min2;
        int sumOfFrequencies;
        while(heap.getSize() > 1)
        {
            node3821 newNode;
            sumOfFrequencies = 0;
            min1 = heap.extractMin();
            sumOfFrequencies += min1.getFrequency();

            min2 = heap.extractMin();
            sumOfFrequencies += min2.getFrequency();

            newNode = new node3821(sumOfFrequencies, min1, min2);
            heap.add(newNode);
        }
        return heap.peepMin();
    }
    private static void printHuffmanCode(node3821 root, String arr)
    {
        if(root.getLeftChild()!=null)
        {
            printHuffmanCode(root.getLeftChild(), arr+"0");
        }
        if(root.getRightChild()!=null)
        {
            printHuffmanCode(root.getRightChild(), arr+"1");
        }
        if(root.getLeftChild() == null && root.getRightChild() == null)
        {
 
            DataSet ds = new DataSet();
            ds.setValue((int)(root.getChar()));
            ds.setFrequency(root.getFrequency());
            ds.setHuffmanCode(arr);
            builtHuffmanDataList.add(ds);
        }
    }
    private static void writeTreeInFile(node3821 node)
    {
        if(node.isInternalNode)
        {
            writer.write(true);
            if(node.getRightChild() != null)
            {
                writeTreeInFile(node.getRightChild());
            }
            if(node.getLeftChild() != null)
            {
                writeTreeInFile(node.getLeftChild());
            }
        }
        else
        {
            writer.write(false);
            writer.write(node.getChar());
        }
    }
    private static void writeEncodedFile()
    {
        String input;
        Iterator iterate;
        reader = new read3821(filePath);
        try{
            input = reader.readString();
            writer.write(input.length());
            // System.out.print("length of input file = " + input.length());
            for (char var : input.toCharArray()) {
                iterate = builtHuffmanDataList.iterator();
                while(iterate.hasNext())
                {
                    DataSet ds = (DataSet)iterate.next();
                    if((char)ds.getValue() == var)
                    {
                        String codes = ds.getHuffmanCode();
                        for (char ch : codes.toCharArray()) {
                            if(ch == '0')
                            {
                                writer.write(false);
                            }
                            else
                            {
                                writer.write(true);
                            }
                        }
                        break;
                    }
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
class DataSet
{
    private int value;
    private long frequency;
    private String huffmanCode;
    public String getHuffmanCode()
    {
        return huffmanCode;
    }
    public void setHuffmanCode(String huffmanCode)
    {
        this.huffmanCode = huffmanCode;
    }
    public int getValue()
    {
        return value;
    }
    public long getFrequency()
    {
        return frequency;
    }
    public void setValue(int value)
    {
        this.value = value;
    }
    public void setFrequency(long frequency)
    {
        this.frequency = frequency;
    }
}
class HuffmanMinHeap
{
    private int capacity = 10;
    private int size = 0;

    node3821[] items = new node3821[capacity];

    public int getSize()
    {
        return size;
    }

    private node3821 leftChild(int index){
        return items[(2 * index + 1)];
    }
    private node3821 rightChild(int index){
        return items[( 2 * index + 2)];}
    private node3821 parent(int index){
        return items[((index - 1) / 2)];
    }
    public node3821 peepMin()
    {
        if(size == 0)
        {
            return null;
        }
        return items[0];
    }
    public node3821 extractMin()
    {
        if(size == 0)
        {
            return null;
        }
        node3821 item = items[0];
        items[0] = items[size - 1];
        size--;
        int index = 0;
        while((2 * index + 1) < size)
        {
            int smallerChildIndex = (2 * index + 1);
            if(( 2 * index + 2)<size && rightChild(index).getFrequency() < leftChild(index).getFrequency())
            {
                smallerChildIndex = ( 2 * index + 2);
            }
            if(items[index].getFrequency() < items[smallerChildIndex].getFrequency())
            {
                break;
            }
            else{
                node3821 temp = items[index];
                items[index] = items[smallerChildIndex];
                items[smallerChildIndex] = temp;
                index = smallerChildIndex;
            }
        }
        return item;
    }
    public void add(node3821 item)
    {
        if(size==capacity)
        {
            items = Arrays.copyOf(items, capacity * 2);
            capacity *= 2;
        }
        items[size] = item;
        size++;
        int index = size - 1;
        while(((index - 1) / 2)>=0 && parent(index).getFrequency() > items[index].getFrequency())
        {
            node3821 temp = items[((index - 1) / 2)];
            items[((index - 1) / 2)] = items[index];
            items[index] = temp;
            index = ((index - 1) / 2);
        }
    }
}
final class node3821
{
    private long frequency;
    private char ch;
    public boolean isInternalNode = false;
    private node3821 leftChild;
    private node3821 rightChild;
    public void setFrequency(long frequency){
        this.frequency = frequency;
    }
    public void setChar(char ch){
        this.ch = ch;
    }
    public void setLeftChild(node3821 leftChild){
        this.leftChild = leftChild;
    }
    public void setRightChild(node3821 rightChild){
        this.rightChild = rightChild;
    }
    public long getFrequency(){
        return this.frequency;
    }
    public char getChar(){
        return this.ch;
    }
    public node3821 getLeftChild(){
        return this.leftChild;
    }
    public node3821 getRightChild(){
        return this.rightChild;
    }
    public node3821(long frequency, char ch)
    {
        setFrequency(frequency);
        setChar(ch);
        setLeftChild(null);
        setRightChild(null);
    }
    public node3821(long frequency, char ch, node3821 leftChild, node3821 rightChild)
    {
        setFrequency(frequency);
        setChar(ch);
        setLeftChild(leftChild);
        setRightChild(rightChild);
    }
    public node3821(long frequency, node3821 leftChild, node3821 rightChild)
    {
        if(!(leftChild == null && rightChild == null))
        {
            isInternalNode = true;
        }
        setFrequency(frequency);
        setLeftChild(leftChild);
        setRightChild(rightChild);
    }
}
class read3821 {
	private static BufferedInputStream inputStream;

	private static int buffer;
	private static int bufferSize;

	public read3821() {
		inputStream = new BufferedInputStream(System.in);
	}
	public read3821(String filePath) {
		try {
			inputStream = new BufferedInputStream(new FileInputStream(filePath));
		} 
                catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	public boolean readBit() throws Exception {
		if (bufferSize == 0) {
			readBuffer();
		} 
                else if (bufferSize == -1) {
			throw new Exception("File ended!");
		}
		bufferSize--;
		return  ((buffer >> bufferSize) & 1) == 1;
	}
	public void readBuffer() {
		try {
			buffer = inputStream.read();
			if(buffer == -1) {
				return;
			}
			bufferSize = 8;
		} 
                catch (IOException e) {
			System.out.println(e.getMessage());
			buffer = -1;
			bufferSize = -1;
		}
	}
	public char readByte() throws Exception {
            switch (bufferSize) {
                case 0:
                {
                    readBuffer();
                    int thisByte = (buffer & 0xFF);
                    readBuffer();
                    return (char)(thisByte & 0xFF);
                }
                case -1:
                    throw new Exception("File ended!");
                case 8:
                {
                    int thisByte = buffer;
                    readBuffer();
                    return (char)(thisByte & 0xFF);
                }
                default:
                    int oldLength = bufferSize;
                    int bits = buffer;
                    bits <<= (8-oldLength);
                    readBuffer();
                    if (bufferSize == -1 || buffer == -1) {
                        throw new Exception("File ended!");
                    }
                    bufferSize = oldLength;
                    bits |= buffer >>> bufferSize;
                    return (char)(bits & 0xFF);
            }
	}
	public char readChar() throws Exception {
		return (char)(readByte() & 0xFF);
	}
	public String readString() throws Exception {
		StringBuilder sb = new StringBuilder();
		do {
			sb.append(readChar());
		}while (buffer != -1);
		return sb.toString();
	}
	public void close() throws IOException {
		inputStream.close();
	}
	public int readInt() throws Exception {
		int number = 0;
		for (int i = 0; i < 4; i++) {
			number<<=8;
			number |= (readByte() & 0xFF);
		}
		return number;
	}
}
class write3821 {
	private static BufferedOutputStream outputStream;
	private static int buffer;
	private static int bufferSize;
	public write3821(String filename) {
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	public void write(boolean bit) {
		buffer <<= 1;
		if (bit) 
                    buffer |= 1;
		bufferSize++;
        if (bufferSize == 8) writeBuffer();

	}
	public void write(char ch) {
		writeByte(ch);
	}
	public void write(int number) {
        writeByte((number >>> 24) & 0xff);
        writeByte((number >>> 16) & 0xff);
        writeByte((number >>>  8) & 0xff);
        writeByte((number) & 0xff);
	}
	private void writeByte(int myByte) {
		if (bufferSize == 0) {
			try {
				outputStream.write(myByte);
			} 
                        catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		else {
		    for (int i = 0; i < 8; i++) {
	               boolean bit = ((myByte >>> (7-i)) & 1) == 1;
	               write(bit);
	        }
		}
	}
	private static void writeBuffer() {
		if(bufferSize == 0)
			return;
		if (bufferSize > 0) {
			buffer<<= 8 - bufferSize;
		}
		try {
			outputStream.write(buffer);
		} 
                catch (IOException e) {
			System.out.println(e.getMessage());
		}
		bufferSize = 0;
		buffer = 0;
	}
	public void close() {
		writeBuffer();
		try {
			outputStream.flush();
		} 
                catch (IOException e) {
			System.out.println(e.getMessage());
		}
		try {
			outputStream.close();
		} 
                catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
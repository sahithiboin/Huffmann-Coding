/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
/**
 *
 * @author SAHITHI BOINPALLI CS610 prp3821
 */

public class hdec3821
{
    private static read3821 reader;
    private static write3821 writer;
    private static String filePath;
    public static void main(String args[])
    {
        if(args.length != 1)
        {
            System.out.println("Invalid parameters received!");
            return;
        }
        else
        {
            filePath = args[0];
        }
        reader = new read3821(filePath);
        writer = new write3821(filePath.substring(0,filePath.length()-4));
        node3821 rootNode = traceTreeFromFile();
        node3821 node ;
        boolean huffmanBit;
        int count;
        try
        {
            int fileLength = reader.readInt();
            count = 0;
            for(int index=0;index<fileLength-1;index++)
            {
                node = rootNode;
                count++;
                while(node.isInternalNode == true)
                {
                    huffmanBit = reader.readBit();
                    if(huffmanBit == true)
                    {
                        node = node.getRightChild();
                    }
                    else
                    {
                        node = node.getLeftChild();
                    }
                }
                writer.write(node.getChar());
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }      
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
    private static node3821 traceTreeFromFile()
    {
        node3821 left , right;
        boolean isInternalNode;
        try
        {
            isInternalNode = reader.readBit();
            if(isInternalNode == false)
            {
                return new node3821(-1, reader.readChar());
            }
            else
            {
                left = traceTreeFromFile();
                right = traceTreeFromFile();
                return new node3821(-1, right, left);
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return null;
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

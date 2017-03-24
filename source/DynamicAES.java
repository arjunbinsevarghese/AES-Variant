import java.io.DataInputStream;
import java.io.IOException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


public class modAES {

	
	private static String[] selm={"63","7c","77","7b","f2","6b","6f","c5","30","01","67","2b","fe","d7","ab","76",    
				   				  "ca","82","c9","7d","fa","59","47","f0","ad","d4","a2","af","9c","a4","72","c0",
				   				  "b7","fd","93","26","36","3f","f7","cc","34","a5","e5","f1","71","d8","31","15",
				   				  "04","c7","23","c3","18","96","05","9a","07","12","80","e2","eb","27","b2","75",
				   				  "09","83","2c","1a","1b","6e","5a","a0","52","3b","d6","b3","29","e3","2f","84",
				   				  "53","d1","00","ed","20","fc","b1","5b","6a","cb","be","39","4a","4c","58","cf",
				   				  "d0","ef","aa","fb","43","4d","33","85","45","f9","02","7f","50","3c","9f","a8",
				   				  "51","a3","40","8f","92","9d","38","f5","bc","b6","da","21","10","ff","f3","d2",
				   				  "cd","0c","13","ec","5f","97","44","17","c4","a7","7e","3d","64","5d","19","73",
				   				  "60","81","4f","dc","22","2a","90","88","46","ee","b8","16","de","5e","0b","db",
				   				  "e0","32","3a","0a","49","06","24","5c","c2","d3","ac","62","91","95","e4","79",
				   				  "e7","c8","37","6d","8d","d5","4e","a9","6c","56","f4","ea","65","7a","ae","08",
				   				  "ba","78","25","2e","1c","a6","b4","c6","e8","dd","74","1f","4b","bd","8b","8a",
				   				  "70","3e","b5","66","48","03","f6","0e","61","35","57","b9","86","c1","1d","9e",
				   				  "e1","f8","98","11","69","d9","8e","94","9b","1e","87","e9","ce","55","28","df",
				   				  "8c","a1","89","0d","bf","e6","42","68","41","99","2d","0f","b0","54","bb","16",
								};
	
	
	private static String[] selmRot=new String[256];
	
	private static String[] Rcon={"8d", "01", "02", "04", "08", "10", "20", "40", "80", "1b", "36"};
	
	private static String[][] sbox=new String[16][16];
	private static String[][] sboxRot=new String[16][16];
	
	private static void generateSBox(){
		int k=0;
		for(int i=0;i<16;i++)
			for(int j=0;j<16;j++)
				{
				sbox[i][j]=selmRot[k];
				k++;
				}
		/*
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				System.out.print(sbox[i][j]+"\t");
			}
			System.out.println();
		}
		*/
				
	}
	
	private static void rotateSBox(String[][] roundKey) {
		// TODO Auto-generated method stub
		
		int w = 0,i,r;
		System.out.println("-----------------------------\n\n");

		/*for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				System.out.println(roundKey[i][j]+" ");
			}
		}
		*/
		for(i=0;i<4;i++){
			for(int j=0;j<4;j++){
				w=(Integer.valueOf(roundKey[i][j], 16))^w;
			}
		}
		
		System.out.println("r value="+w);
		
		for(i=0; i<256; i++){
			r=(i+w)%256;
			selmRot[r]=selm[i];
		}
		
		
		
		generateSBox();
		
	}
	
	// to split the message and key into 128 bit blocks 
	private static void encryptBlock(String message, String secret) {
		int msgLen, secLen;
		String plain = null, key = null;
		
		do{
			msgLen=message.length();
			secLen=secret.length();
			
			
			
			//Split if the string length is > 16
			
			if(msgLen>16){
				plain=message.substring(0, 16);
				message=message.substring(16,msgLen);
			}
			
			
			if(secLen>16){
				key=secret.substring(0, 16);
				secret=secret.substring(16,secLen);
			}
			
			if(msgLen<=16)
			{
				plain=message;
				msgLen=0;
			}
			if(secLen<=16){
				key=secret;
				secLen=0;
			}
			
																				/*
																				System.out.println("plain= "+plain);
																				System.out.println("message= "+message);
																				System.out.println("key= "+key);
																				System.out.println("secret= "+secret);
																				*/
			
			encrypt128(plain,key);	//encrypt each block
			
		}while(msgLen!=0);	//for each and every block
	}
	
	
	
	
	
	
		//to encrypt each 128 bit block
		private static String encrypt128(String plain, String key){
			
			int plainLen = plain.length();
			int keyLen = key.length();
			
			//character array of plaintext & key
			
			char[] plainArr=new char[16];
			plainArr=plain.toCharArray();
			
			char[] keyArr=new char[16];
			keyArr=key.toCharArray();
			
																				//System.out.println(plain+" "+plainLen+" and "+key+" "+keyLen);
			
			//INTEGER array of plaintext & key
			
			int[] stateELM=new int[16];
			int[] keyELM=new int[16];
			
			
			/*
			//HEX array of plaintext & key
			
			String[] statehexELM=new String[16];
			String[] keyhexELM=new String[16];
			*/
			
			
			
			//entry
			for(int i=0;i<plainLen;i++){
				stateELM[i]=Integer.valueOf(plainArr[i]);
				//statehexELM[i]=Integer.toHexString(plainArr[i]);	
																				//System.out.println(plainArr[i]+" = "+stateELM[i]);
			}
			
			for(int i=0;i<keyLen;i++){
				keyELM[i]=Integer.valueOf(keyArr[i]);
				//keyhexELM[i]=Integer.toHexString(keyArr[i]);		
																				//System.out.println(keyArr[i]+" = "+keyELM[i]);
			}
			

			//padding with 0's. 
			
			if(plainLen<16){
				for(int i=plainLen;i<16;i++){
				//statehexELM[i]="00";
				stateELM[i]=0;
				}
			}
			
			if(keyLen<16){
				for(int i=keyLen;i<16;i++){
					//keyhexELM[i]="00";
					keyELM[i]=0;
				}
			}
			
			
			//creating the state and key matrix 4x4
				
			
			/*
			 *  String[][] keyhexMat=new String[4][4];
			*/
			 int[][] stateMat=new int[4][4];
			 int[][] keyMat=new int[4][4];
			
			int k=0;	// counter for elements
				
			for(int i=0;i<4;i++)
				for(int j=0;j<4;j++){
					stateMat[j][i]=stateELM[k];	//coloumwise entry
					keyMat[j][i]=keyELM[k];		//do
					
					
					/*
					statehexMat[j][i]=statehexELM[k];	//coloumwise entry
					keyhexMat[j][i]=keyhexELM[k];		//do
					*/
					k++;
				}
			
			/*
			 
			 //displaying state matrix
			System.out.println("Original State Matrix");
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					System.out.print(stateMat[i][j]+"\t");
				}
				System.out.println();
			}
			
			System.out.println("\n\n\n");

			//displaying key matrix
			System.out.println("Original Key Matrix");
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					System.out.print(keyMat[i][j]+"\t");
				}
				System.out.println();
			}
			
			*/
			
			/*
			System.out.println("\n\n\n");
			
			
			//displaying state HEX matrix
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					System.out.print(statehexMat[i][j]+"\t");
				}
				System.out.println();
			}
			
			System.out.println("\n\n\n");
			
			
			//displaying key HEX matrix
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					System.out.print(keyhexMat[i][j]+"\t");
				}
				System.out.println();
			}
			
			
			
			System.out.println("\n\n\n");
			
			
			
			*/
			
			
			
			/* --------------------------------------------ENCRYPTION BEIGNS-----------------------------------------*/
			String[][] roundKey=new String[4][4];
			String[][] state=new String[4][4];
			
			for(int i=0;i<4;i++)
				for(int j=0;j<4;j++)		
					roundKey[i][j]=Integer.toHexString(keyMat[i][j]);
			
			
			for(int i=0;i<4;i++)
				for(int j=0;j<4;j++)		
					state[i][j]=Integer.toHexString(stateMat[i][j]);
			
			
			System.out.println("Key Matrix");
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					System.out.print(roundKey[i][j]+"\t");
				}
				System.out.println();
			}

			System.out.println("State Matrix");
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					System.out.print(state[i][j]+"\t");
				}
				System.out.println();
			}
		

			System.out.println("-----------------------------------Rounds Begins---------------------------------------");

			
			//Add Round Key before round 1
			state=addkey(state,roundKey);
																	/*
																	for(int i=0;i<4;i++)
																	for(int j=0;j<4;j++)		
																	{
																	stateMat[i][j]=keyMat[i][j]^stateMat[i][j];
					 												state[i][j]=Integer.toHexString(stateMat[i][j]);
																	}
																	*/
			
																	
																	
																	// After Adding roundkey0 || before round 1
																	System.out.println("\n\nADD KEY b4 FIRST ROUND");
			
																	for(int i=0;i<4;i++){
																	for(int j=0;j<4;j++){
																	System.out.print(state[i][j]+"\t");
																	}
																	System.out.println();
																	}
																	 
		//9 rounds
																	
		for(int z=1;z<10;z++){
		
			//rounds started
			System.out.println("\n\n---------Round "+z+"------------");																
		
			//Substitute Bytes
			rotateSBox(roundKey);
			state=subbytes(state);
			
																	System.out.println("\nState Matrix after subbytes\n");
																	for(int i=0;i<4;i++){
																	for(int j=0;j<4;j++){
																	System.out.print(state[i][j]+"\t");
																	}
																	System.out.println();
																	}
			
			//Shift Rows
			state=shiftrows(state);
			
																	System.out.println("\nState Matrix after shiftrows\n");
																	for(int i=0;i<4;i++){
																	for(int j=0;j<4;j++){
																	System.out.print(state[i][j]+"\t");
																	}
																	System.out.println();
																	}
																	
			//Mix Coloumns
			state=mixcol(state);
			
																	System.out.println("\nState Matrix after Mix Coloumn\n");
																	for(int i=0;i<4;i++){
																	for(int j=0;j<4;j++){
																	System.out.print(state[i][j]+"\t");
																	}
																	System.out.println();
																	}
																	
																	/*
																	System.out.println("Key Matrix before Round Key generation");
																	for(int i=0;i<4;i++){
																	for(int j=0;j<4;j++){
																	System.out.print(roundKey[i][j]+"\t");
																	}
																	System.out.println();
																	}
																	*/
			//Add Round Key
			
			roundKey=subkey(roundKey, z);
			
																	System.out.println("\nKEY MATRIX after Round Key Generation\n");
																	for(int i=0;i<4;i++){
																	for(int j=0;j<4;j++){
																	System.out.print(roundKey[i][j]+"\t");
																	}
																	System.out.println();
																	}
			
		
			state=addkey(state, roundKey);
			
			System.out.println("\nState Matrix after Add Round Key\n");
			for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
			System.out.print(state[i][j]+"\t");
			}
			System.out.println();
			}
		
		}	
		
		//10th round
		
		
		System.out.println("\n\n---------Round 10------------");
		
		state=subbytes(state);
		
		System.out.println("\nState Matrix after subbytes\n");
		for(int i=0;i<4;i++){
		for(int j=0;j<4;j++){
		System.out.print(state[i][j]+"\t");
		}
		System.out.println();
		}

		//Shift Rows
		
		state=shiftrows(state);

		System.out.println("\nState Matrix after shiftrows\n");
		for(int i=0;i<4;i++){
		for(int j=0;j<4;j++){
		System.out.print(state[i][j]+"\t");
		}
		System.out.println();
		}
		
		
		//Add Round Key
		
		roundKey=subkey(roundKey, 10);
		
																System.out.println("\nKEY MATRIX after Round Key Generation\n");
																for(int i=0;i<4;i++){
																for(int j=0;j<4;j++){
																System.out.print(roundKey[i][j]+"\t");
																}
																System.out.println();
																}
		
	
		state=addkey(state, roundKey);
		
		System.out.println("\nState Matrix after Add Round Key\n");
		for(int i=0;i<4;i++){
		for(int j=0;j<4;j++){
		System.out.print(state[i][j]+"\t");
		}
		System.out.println();
		}
		
		
		
		
			return null;
		}
	
		
		
		//Substitute Bytes function
		
		private static String[][] subbytes(String[][] state){
			int row = 0,col;
			String temp;
			
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					
					temp=state[i][j];
					
					//finds row and coloumn of SBox by splitting the hex element of state
					if(temp.length()==2){
						row=Integer.valueOf(temp.substring(0, 1),16);
						col=Integer.valueOf(temp.substring(1, 2),16);
					}
					
					else
					{
						row=0;
						col=Integer.valueOf(temp.substring(0, 1),16);
					}
					
					
					state[i][j]=sbox[row][col];
					
				}
			}
			
			return state;
			
		}
		
		
		
		
		//Shifting the rows
		private static String[][] shiftrows(String[][] state){
			String temp;
			
			temp=state[1][0];
			state[1][0]=state[1][1];
			state[1][1]=state[1][2];
			state[1][2]=state[1][3];
			
			state[1][3]=temp;
			temp=state[2][0];
			state[2][0]=state[2][2];
			state[2][2]=temp;
			
			temp=state[2][1];
			state[2][1]=state[2][3];
			state[2][3]=temp;
						
			temp=state[3][3];
			state[3][3]=state[3][2];
			state[3][2]=state[3][1];
			state[3][1]=state[3][0];
			state[3][0]=temp;
				
			return state;
			
		}
		
		
		
		
		
		
		
		
		private static int gal2(String hVal){
			
			char drop;
			
			int iVal=Integer.valueOf(hVal, 16);
			String bVal=Integer.toBinaryString(iVal);
			
			
			int len=bVal.length();
			while(len!=8){
				bVal="0"+bVal;
				len=bVal.length();
			}
			

			char[] bArr=bVal.toCharArray();
			
			drop=bArr[0];
			
			for(int i=0;i<7;i++){
				bArr[i]=bArr[i+1];
				//System.out.println(bArr[i]);
			}
			bArr[7]='0';
			bVal=new String(bArr);
			
			if(drop=='1'){
				iVal=(Integer.valueOf(bVal,2))^(Integer.valueOf("00011011",2));
			}
			
			else
				iVal=Integer.valueOf(bVal,2);
			
			return iVal;
			
		}
		
		private static int gal3(String hVal){
			
			int iVal=(Integer.valueOf(hVal,16))^(gal2(hVal));
			return iVal;
			
		}
		
		private static String[][] mixcol(String[][] state){
				
			int t1,t2,t3,t4;
			
				for(int j=0;j<4;j++){
					t1=gal2(state[0][j])^gal3(state[1][j])^Integer.valueOf(state[2][j],16)^Integer.valueOf(state[3][j],16);
					t2=Integer.valueOf(state[0][j],16)^gal2(state[1][j])^gal3(state[2][j])^Integer.valueOf(state[3][j],16);
					t3=Integer.valueOf(state[0][j],16)^Integer.valueOf(state[1][j],16)^gal2(state[2][j])^gal3(state[3][j]);
					t4=gal3(state[0][j])^Integer.valueOf(state[1][j],16)^Integer.valueOf(state[2][j],16)^gal2(state[3][j]);
				
				state[0][j]=Integer.toHexString(t1);
				state[1][j]=Integer.toHexString(t2);
				state[2][j]=Integer.toHexString(t3);
				state[3][j]=Integer.toHexString(t4);
				
				}
			return state;
		}
		
		
		//the operation G
		private static String G(String K, int round){
			String[] Karr=new String[4];
			String[] subKarr=new String[4];
			String subK = "";
			int pos=0;
			int row,col;
			//Splitting the 4byte word to four 1 byte strings
			for(int i=0;i<4;i++){
				Karr[i]=K.substring(pos, pos+2);
				pos+=2;
				//System.out.print(Karr[i]);
			}
			
			//One byte left circular ROTATION
			String temp=Karr[0];
			for(int i=0;i<3;i++){
				Karr[i]=Karr[i+1];	
			}
			Karr[3]=temp;
			
			
			/*
			 System.out.println();
			 for(int i=0;i<4;i++){
				System.out.print(Karr[i]);
			 }
			*/
			
			//BYTE SUBSTITUTION for each byte of the word
			
			for(int i=0;i<4;i++){
				
				row=Integer.valueOf(Karr[i].substring(0, 1),16);
				col=Integer.valueOf(Karr[i].substring(1, 2),16);
				
				
				subKarr[i]=sbox[row][col];
			}
			
			/*
			System.out.println();
			for(int i=0;i<4;i++)
			System.out.print(subKarr[i]);
			*/
			
			
			//Adding with Round Constants RCON
			
			int tmp=(Integer.valueOf(subKarr[0], 16))^(Integer.valueOf(Rcon[round], 16));
			subKarr[0]=Integer.toHexString(tmp);
			
			for(int i=0;i<4;i++)
				subK+=subKarr[i];
			/*
			System.out.println();
			System.out.println(subK);
			*/
			
			return subK;
		}
		
		
		private static String makeOneByte(String s){
			if(s.length()<2){
				s="0"+s;
			}
			return s;
		}
		
		
		
		private static String[][] subkey(String[][] preKey, int round){
		
			String[][] roundkey = new String[4][4];
			String[] wp=new String[4];
			String[] wn=new String[4];
			
			int k=0;
			
			for(int i=0;i<4;i++){
				wp[k]="";
				for(int j=0;j<4;j++){
					wp[k]+=preKey[j][i];
					
				}
				k++;
			}
			
			
			/*
			for(int i=0;i<4;i++){
				System.out.print(wp[i]+"\t");
			}
			
			System.out.println();
			*/
			
			//Apply Function G to the last word of the previous Key
			
			String temp=G(wp[3], round);
			

			String a1=temp.substring(0, 2);
			String a2=temp.substring(2, 4);
			String a3=temp.substring(4, 6);
			String a4=temp.substring(6, 8);
			String b1=wp[0].substring(0, 2);
			String b2=wp[0].substring(2, 4);
			String b3=wp[0].substring(4, 6);
			String b4=wp[0].substring(6, 8);
			
			
			
			
			String t1=Integer.toHexString((Integer.valueOf(a1, 16))^(Integer.valueOf(b1, 16)));
			t1=makeOneByte(t1);
			String t2=Integer.toHexString((Integer.valueOf(a2, 16))^(Integer.valueOf(b2, 16)));
			t2=makeOneByte(t2);
			String t3=Integer.toHexString((Integer.valueOf(a3, 16))^(Integer.valueOf(b3, 16)));
			t3=makeOneByte(t3);
			String t4=Integer.toHexString((Integer.valueOf(a4, 16))^(Integer.valueOf(b4, 16)));
			t4=makeOneByte(t4);
			
			//Deriving the First word of the Round Key
			wn[0]=t1+t2+t3+t4;
			
			/*
			System.out.println("\n\n\n");
			System.out.println(wn[0]);
			*/
			
			//Deriving the rest of the words of the Round Key
			for(int i=1;i<4;i++){
				a1=wn[i-1].substring(0, 2);
				a2=wn[i-1].substring(2, 4);
				a3=wn[i-1].substring(4, 6);
				a4=wn[i-1].substring(6, 8);
				
			    b1=wp[i].substring(0, 2);
			    b2=wp[i].substring(2, 4);
				b3=wp[i].substring(4, 6);
				b4=wp[i].substring(6, 8);
				
				
				
				t1=Integer.toHexString((Integer.valueOf(a1, 16))^(Integer.valueOf(b1, 16)));
				t1=makeOneByte(t1);
				t2=Integer.toHexString((Integer.valueOf(a2, 16))^(Integer.valueOf(b2, 16)));
				t2=makeOneByte(t2);
				t3=Integer.toHexString((Integer.valueOf(a3, 16))^(Integer.valueOf(b3, 16)));
				t3=makeOneByte(t3);
				t4=Integer.toHexString((Integer.valueOf(a4, 16))^(Integer.valueOf(b4, 16)));
				t4=makeOneByte(t4);
				
				wn[i]=t1+t2+t3+t4;
				//System.out.println(wn[i]+"\t");
			}
			
			//Converting into bytes to make Matrix
			int l=0;
			String[] keyElm=new String[16];
			for(int i=0;i<4;i++){
				for(int j=0;j<8;j+=2){
				keyElm[l]=wn[i].substring(j, j+2);
				l++;
			}}
			
			/*
			for(int i=0;i<16;i++){
			System.out.print(keyElm[i]);
			}
			*/
			
			
			//Making the Round Key MATRIX
			l=0;
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					roundkey[j][i]=keyElm[l];
					l++;
				}
			}
			
			
			/*
			System.out.println();
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					System.out.print(roundkey[i][j]+"\t");
				}
				System.out.println();
			}
			*/
			
			return roundkey;
			
		}
		
		
		private static String[][] addkey(String[][] state , String[][] roundKey){
			
			int[][] stateMat=new int[4][4];
			int[][] keyMat=new int[4][4];
			for(int i=0;i<4;i++)
				for(int j=0;j<4;j++)		
					{
					 stateMat[i][j]=Integer.valueOf(state[i][j],16);
					 keyMat[i][j]=Integer.valueOf(roundKey[i][j],16);
					 stateMat[i][j]=keyMat[i][j]^stateMat[i][j];
					 state[i][j]=Integer.toHexString(stateMat[i][j]);
					}
			return state;
		}
		
		public static void main(String args[]) throws IOException{
		
			
			//generateSBox();
			/*
		DataInputStream in=new DataInputStream(System.in);
		System.out.println("Enter the Message: ");
		String message=in.readLine();
		message=message.toUpperCase();
		System.out.println("Enter the Secret: ");
		String secret=in.readLine();
		secret=secret.toUpperCase();
		
		int msgLen = message.length();
		int secLen = secret.length();
		
		if(msgLen<secLen){
			System.out.println("Key Length should be less than Message Length: ");
			
		}
		
		else{*/
			String message="NETWORK SECURITY";
			String secret="MANIPALINSTITUTE";
			encryptBlock(message, secret);
			
		//}
	}

}

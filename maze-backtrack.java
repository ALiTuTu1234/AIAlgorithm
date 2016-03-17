import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * by rice
 * 2015-9-27
 */
public class Main {
	
	static int arr[][]=null;	//存数独
	
	public static void main(String[] args) {
		int len=args.length;
		
		if(len==2){
			arr=new int [9][9];	
			
			//read file
			String inputFileName=args[0];
			File fileR=new File(inputFileName);
			BufferedReader br=null;
			int index=0;
			try {
				br=new BufferedReader(new FileReader(fileR));
				String line="";
				while((line=br.readLine())!=null){
					for(int i=0;i<9;i++){
						arr[index][i]=line.charAt(i)-'0';
					}
					index++;
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if(br!=null){
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			//找到第一个不是0的点，是填数字的开始点
			int start_x=0;
			int start_y=0;
			if(arr[0][0]==0){	//因为findXY()中是do-while，所以要判断一下
				start_x=0;
				start_y=0;
			}else{
				int temp=findXY(0,0);
				start_x=temp/10;
				start_y=temp%10;
			}
			
			//solve sudoku
			sudoku(start_x,start_y);
			
			//write file
			String outputFileName=args[1];
			File fileW=new File(outputFileName);
			BufferedWriter bw=null;
			try {
				bw=new BufferedWriter(new FileWriter(fileW));
				
				for(int i=0;i<9;i++){
					for(int j=0;j<9;j++){
						bw.write(arr[i][j]+"");
					}
//					bw.write("\r\n");
					bw.newLine();
					
				}
					
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(bw!=null){
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			System.out.println("Wrong args!");	
			System.exit(0);	//exit
		}
	}

	//解决数独问题的主方法
	private static boolean sudoku(int x,int y) {
		int can[]=getCan(x,y);
		
		if(x==8 && y==8){	//结束条件
			for(int i=1;i<10;i++){
				if(can[i]!=1){
					arr[x][y]=i;
					return true;
				}
			}
		}
		
		int sum=0;
		for(int i=1;i<10;i++){	//i一定要从1开始
			sum+=can[i];
		}
		if(sum==9)	//没有可填项了
			return false;
		
		for(int ans=1;ans<10;ans++){
			if(can[ans]==0){
				can[ans]=1;
				arr[x][y]=ans;

				//找到下一个不是0的点
				int temp=findXY(x,y);
				int new_x=temp/10;
				int new_y=temp%10;
				
				if(sudoku(new_x,new_y)){
 					return true;	//返回true说明成功了
				}else{
					can[ans]=0;
					arr[x][y]=0;	//返回false一定要赋值为0，因为如果都不行，最后还是要变为最初的0值的。操作全局变量一定要注意
				}
			}
		}
		
		return false;
	}

	//找到下一个需要填写数字的索引
	private static int findXY(int x, int y) {
		int x1=x,y1=y;
		do{
			if(y1==8 && x1!=8){
				x1=x1+1;
				y1=0;
			//}else if(y1!=8){	
			//一定要加上else，因为y1的值已经变了！！！其实这时已经不需要加上if(y1!=8)判断了
			}else {	
				y1=y1+1;
			}
		}while(arr[x1][y1]!=0);
		
		return x1*10+y1;
	}

	//找到某一待填位置的备选数字(要考虑到所有情况，bug都在getCan()上)
	private static int[] getCan(int x, int y) {	
		int can[]=new int[10];	
		for(int i=0;i<9;i++){
			can[arr[x][i]]=1;
			can[arr[i][y]]=1;
		}
		
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++){
				can[arr[x/3*3+i][y/3*3+j]]=1;	//是y/3*3+j不是y/3+i  记得乘3
			}

		return can;
	}

}
